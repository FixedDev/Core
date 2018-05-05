package us.sparknetwork.core;

import com.google.common.base.Strings;
import com.mongodb.MongoException;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.DefaultCreator;
import sun.misc.Unsafe;
import us.sparknetwork.BanManager.BanManager;
import us.sparknetwork.api.bans.Punishment;
import us.sparknetwork.api.command.CommandHandler;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.kit.IKitManager;
import us.sparknetwork.core.backend.MongoCredentials;
import us.sparknetwork.core.backend.RedisCredentials;
import us.sparknetwork.core.backend.mongo.connection.MongoConnectionManager;
import us.sparknetwork.core.backend.redis.connection.RedisPoolManager;
import us.sparknetwork.core.commands.ReflectionCommandHandler;
import us.sparknetwork.core.commands.chat.*;
import us.sparknetwork.core.commands.essentials.*;
import us.sparknetwork.core.commands.inventory.*;
import us.sparknetwork.core.commands.teleport.*;
import us.sparknetwork.core.economy.EconomyManager;
import us.sparknetwork.core.handlers.FreezeManager;
import us.sparknetwork.core.handlers.StaffModeManager;
import us.sparknetwork.core.listeners.*;
import us.sparknetwork.core.schedulers.AnnouncerRunnable;
import us.sparknetwork.core.schedulers.ClearLagRunnable;
import us.sparknetwork.core.server.GlobalMessager;
import us.sparknetwork.core.server.GlobalPlayerAPI;
import us.sparknetwork.core.server.ServerManager;
import us.sparknetwork.core.spawn.SpawnManager;
import us.sparknetwork.core.user.Participator;
import us.sparknetwork.core.user.User;
import us.sparknetwork.core.user.UserManager;
import us.sparknetwork.core.user.mongo.MongoUserManager;
import us.sparknetwork.core.user.yaml.YAMLUserManager;
import us.sparknetwork.core.vault.EconomyVaultConnector;
import us.sparknetwork.util.Config;
import us.sparknetwork.util.ISignHandler;
import us.sparknetwork.util.PersistableLocation;
import us.sparknetwork.util.SignHandler;
import us.sparknetwork.util.cuboid.Cuboid;
import us.sparknetwork.util.cuboid.NamedCuboid;
import us.sparknetwork.util.itemdb.ItemDb;
import us.sparknetwork.util.itemdb.SimpleItemDb;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class CorePlugin extends JavaPlugin {

    public static String STRAIGHT_LINE_DEFAULT;
    @Getter
    private static CorePlugin plugin;
    @Getter
    private static Chat chat = null;
    @Getter
    private static Permission perms = null;
    public static String STRAIGHT_LINE_TEMPLATE;
    @Getter
    private PluginManager pluginManager;
    @Getter
    private CommandHandler cmdhandler;
    @Getter
    private SpawnManager spawnManager;
    @Getter
    private EconomyManager economyManager;
    @Getter
    private UserManager userManager;
    @Getter
    private ServerHandler serverHandler;
    @Getter
    private RedisPoolManager redisManager;
    @Getter
    private ServerManager serverManager;
    @Getter
    private ItemDb itemDb;
    @Getter
    private ISignHandler signHandler;
    private ClearLagRunnable clearLagTask;
    private AnnouncerRunnable announcerTask;
    @Getter
    private BanManager manager;
    @Getter
    private MongoConnectionManager mongoConnectionManager;
    @Getter
    private boolean mongo;
    @Getter
    private Datastore datastore;
    @Getter
    private Thread mainThread;
    @Getter
    private FApi api;
    @Getter
    private Config databaseConfig;
    @Getter
    private GlobalMessager messager;
    @Getter
    private GlobalPlayerAPI playerApi;

    public static void AsyncCatcher() {
        if (CorePlugin.getPlugin().getMainThread() == Thread.currentThread()) {
            getUnsafe().throwException(new IllegalArgumentException("This method can't be used in the main thread, try using it in another thread."));
        }
    }

    public static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            // ignored
        }
        return null;
    }


    @Override
    public void onLoad() {
        plugin = this;
        mainThread = Thread.currentThread();
        pluginManager = getServer().getPluginManager();
        ConfigurationSerialization.registerClass(PersistableLocation.class);
        ConfigurationSerialization.registerClass(Participator.class);
        ConfigurationSerialization.registerClass(User.class);
        ConfigurationSerialization.registerClass(Cuboid.class);
        ConfigurationSerialization.registerClass(NamedCuboid.class);
    }

    @Override
    public void onEnable() {
        STRAIGHT_LINE_TEMPLATE = ChatColor.STRIKETHROUGH.toString() + Strings.repeat("-", 256);
        STRAIGHT_LINE_DEFAULT = STRAIGHT_LINE_TEMPLATE.substring(0, 55);
        this.itemDb = new SimpleItemDb(this);
        this.signHandler = new SignHandler(this);
        this.databaseConfig = new Config(this, "database");
        registerEconomyProvider();
        this.initDatabases();
        registerManagers(false);

        registerCommands(false);

        api = new FApi(this);


        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            saveData();
            getLogger().warning("The plugin is saving data, please don't stop or restart the server");
        }, 20 * 60, 20 * 60 * 5);
        if (this.mongo && this.mongoConnectionManager != null) {
            (manager = new BanManager()).onEnable(this);
        } else {
            this.getLogger().severe("Can't connect to mongodb, disabling bans module");
        }

    }

    @Override
    public void onDisable() {
        saveData();
        serverManager.disableServerManager();
        cmdhandler.unregisterPluginCommands();
        redisManager.destroy();

        if (this.mongo) {
            mongoConnectionManager.closeMongo();
        }
        signHandler.cancelTasks(null);
        plugin = null;
    }

    private void initDatabases() {
        redisManager = new RedisPoolManager(new RedisCredentials(this.databaseConfig.getString("redis.host"),
                this.databaseConfig.getInt("redis.port"), this.databaseConfig.getString("redis.password")), this);
        if (this.databaseConfig.getConfigurationSection("mongo") != null && this.databaseConfig.getBoolean("mongo.enabled")) {
            try {
                this.mongoConnectionManager = new MongoConnectionManager(
                        new MongoCredentials(this.databaseConfig.getString("mongo.host"), this.databaseConfig.getInt("mongo.port"),
                                this.databaseConfig.getString("mongo.username"), this.databaseConfig.getString("mongo.database"),
                                this.databaseConfig.getString("mongo.password")));
                this.mongoConnectionManager.getMorphia().map(Participator.class, User.class, Punishment.class);
                this.datastore = this.mongoConnectionManager.registerDatastore(this.databaseConfig.getString("mongo.database"));
                this.getLogger().info("Sucessfully connected to mongodb, using it");
                mongo = true;
            } catch (MongoException e) {
                this.getLogger().severe("Failed to connect to mongodb, stopping server (note: maybe you want to disable mongodb?)");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            }
        }
    }

    private boolean registerEconomyProvider() {
        getLogger().info("Registering economy into Vault.");
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            final ServicesManager sm = getServer().getServicesManager();
            sm.register(Economy.class, new EconomyVaultConnector(this), this, ServicePriority.Highest);
            getLogger().info("Registered economy into Vault interface sucessfully.");
            return true;
        }
        getLogger().warning("Failed to register the economy because vault isn't in the server.");
        return false;
    }

    public void saveData() {
        long it = System.nanoTime();
        Bukkit.broadcastMessage(CoreConstants.YELLOW + "Please wait, the server is saving data.");
        economyManager.saveEconomyData();
        spawnManager.saveData();
        serverHandler.saveServerData();
        IKitManager kitManager = null;
        try {
            kitManager = api.getKitManager();
        } catch (RuntimeException e) {
            // ignore this exception, is expected
        }
        if (kitManager != null) {
            kitManager.saveKitData();
        }
        userManager.getUserMap().values().forEach(user -> userManager.saveUser((User) user));
        long ft = System.nanoTime();
        long tt = ft - it;
        tt /= 1000000;
        Bukkit.broadcastMessage(CoreConstants.YELLOW + String.format("Sucessfully saved data economy data and user data of %1$s players in %2$s ms.",
                userManager.getUserMap().size(), tt));
    }

    private void registerCommands(boolean bool) {
        registerBasicCommands();
    }

    private void registerBasicCommands() {
        List<CommandModule> cmds = new ArrayList<>();
        cmdhandler = new ReflectionCommandHandler(this);
        cmds.add(new CommandReply(this));
        cmds.add(new CommandBroadcast(this));
        cmds.add(new CommandBroadcastRaw(this));
        cmds.add(new CommandGamemode(this));
        cmds.add(new CommandFly(this));
        cmds.add(new CommandCore(this));
        cmds.add(new CommandKill(this));
        cmds.add(new CommandTeleport(this));
        cmds.add(new CommandTeleportHere(this));
        cmds.add(new CommandTeleportAll(this));
        cmds.add(new CommandTell(this));
        cmds.add(new CommandTogglePm(this));
        cmds.add(new CommandHeal(this));
        cmds.add(new CommandFeed(this));
        cmds.add(new CommandVanish(this));
        cmds.add(new CommandFreeze(this));
        cmds.add(new CommandInvsee(this));
        cmds.add(new CommandAmivis(this));
        cmds.add(new CommandClearInventory(this));
        cmds.add(new CommandGive(this));
        cmds.add(new CommandRepair(this));
        cmds.add(new CommandStaffMode(this));
        cmds.add(new CommandToggleGlobalChat());
        cmds.add(new CommandClearChat());
        cmds.add(new CommandStaffChat());
        cmds.add(new CommandToggleStaffChat());
        cmds.add(new CommandRename());
        cmds.add(new CommandHelpOp());
        cmds.add(new CommandEnchant());
        cmds.add(new CommandSudo());
        cmds.add(new CommandSetMaxPlayers());
        cmds.add(new CommandList());
        cmds.add(new CommandIpHistory());
        cmds.add(new CommandTop());
        cmds.add(new CommandCraft());
        cmds.add(new CommandSocialSpy());
        cmds.add(new CommandWorld());
        cmds.add(new CommandClearLag());
        cmds.add(new CommandAnnouncement());
        cmds.add(new CommandBack());
        cmds.add(new CommandSpeed());
        cmds.add(new CommandItem());
        cmds.add(new CommandCopyInventory());
        cmds.add(new CommandRam());
        cmds.add(new CommandMuteChat());
        cmds.add(new CommandSlowChat());
        cmds.add(new CommandSetChatDelay());
        cmds.forEach(cmd -> cmd
                .setPermissionMessage(
                        CoreConstants.GOLD + ChatColor.BOLD.toString() + CoreConstants.NAME + ChatColor.DARK_GRAY + "»"
                                + CoreConstants.GRAY + "You don't have permission to execute this command")
                .setPermission("core.command." + cmd.getName()));
        cmdhandler.registerAll(cmds);
    }

    private void registerBasicManagers() {
        // Managers
        if (this.datastore != null) {
            this.userManager = new MongoUserManager(this);
        } else {
            this.userManager = new YAMLUserManager(this);
        }
        this.serverHandler = new ServerHandler(this);
        this.serverManager = new ServerManager(this);
        economyManager = new EconomyManager(this);
        spawnManager = new SpawnManager(this);
        new StaffModeManager(this);
        new FreezeManager(this);
        setupPermissions();
        setupChat();
        this.messager = new GlobalMessager(this.getServerManager());
        this.playerApi = new GlobalPlayerAPI(this);

        // Listeners
        pluginManager.registerEvents(new UserManagerListener(), this);
        pluginManager.registerEvents(new SecurityListener(this), this);
        pluginManager.registerEvents(new SignListener(), this);
        pluginManager.registerEvents(new VanishListeners(), this);

        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new TeleportListener(), this);
        pluginManager.registerEvents(new HSCServerListener(), this);
        pluginManager.registerEvents(new MainCallbacks(this), this);
    }

    public void registerSchedulers() {
        if (clearLagTask != null) {
            clearLagTask.cancel();
        }
        if (announcerTask != null) {
            announcerTask.cancel();
        }

        long clearLagDelay = this.getServerHandler().getClearLagDelay() * 20L;
        long announcerDelay = this.getServerHandler().getAnnouncerDelay() * 20L;
        this.announcerTask = new AnnouncerRunnable(this);
        this.clearLagTask = new ClearLagRunnable();
        announcerTask.runTaskTimerAsynchronously(this, announcerDelay, announcerDelay);
        clearLagTask.runTaskTimerAsynchronously(this, clearLagDelay, clearLagDelay);
    }

    private void registerManagers(Boolean bool) {
        registerBasicManagers();
        registerSchedulers();
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public ClassLoader getBukkitClassLoader(){
        return this.getClassLoader();
    }

}