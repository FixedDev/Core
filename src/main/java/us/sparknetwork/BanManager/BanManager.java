package us.sparknetwork.BanManager;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import us.sparknetwork.BanManager.commands.*;
import us.sparknetwork.BanManager.listeners.onChatListener;
import us.sparknetwork.BanManager.listeners.onJoinListener;
import us.sparknetwork.api.bans.BansAPI;
import us.sparknetwork.api.bans.Database;
import us.sparknetwork.api.bans.Punishment;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.event.PlayerPunishEvent;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.StaffPriority;
import us.sparknetwork.core.commands.ReflectionCommandHandler;
import us.sparknetwork.core.server.ServerSettings;
import us.sparknetwork.util.PlayerUtils;
import us.sparknetwork.util.TimeParser;
import us.sparknetwork.util.TimeUtils;

import javax.annotation.Nullable;
import java.util.*;

public class BanManager implements BansAPI {

    @Getter
    private static BanManager instance;
    @Getter
    private Database database;
    @SuppressWarnings("unused")
    private BukkitTask autorefresh;
    @Getter
    private CorePlugin plugin;
    @Getter
    private boolean enabled;
    @Getter
    private Datastore datastore;

    public void onEnable(CorePlugin plugin) {
        this.plugin = plugin;
        instance = this;
        BansConfig.load();
        ReflectionCommandHandler cmdhandler = (ReflectionCommandHandler) plugin.getCmdhandler();
        List<CommandModule> cmdlist = new ArrayList<>();
        cmdlist.add(new BanCommand(this));
        cmdlist.add(new KickCommand(this));
        cmdlist.add(new UnbanCommand(this));
        cmdlist.add(new BlacklistCommand(this));
        cmdlist.add(new MuteCommand(this));
        for (CommandModule cmd : cmdlist) {
            cmd.setPermission("banmanager.command." + cmd.getLabel());
        }
        datastore = plugin.getMongoConnectionManager().registerDatastore("bans");
        cmdhandler.registerAll(cmdlist);
        plugin.getServer().getPluginManager().registerEvents(new onJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new onChatListener(), plugin);
        this.database = new BMDatabase(datastore);
        autorefresh = (new AutoRefreshScheduler()).runTaskTimer(plugin, BansConfig.AUTO_REFRESH_TICKS,
                BansConfig.AUTO_REFRESH_TICKS);
        this.enabled = true;
    }

    public void displayPunishmentMessage(Punishment punish) {
        String basicPunishMessage = ChatColor.AQUA + "%1$s " + ChatColor.GREEN + "was %2$s by " + ChatColor.AQUA + "%3$s" + ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "%4$s" + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + ".";
        String silentMessage = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Silent" + ChatColor.DARK_GRAY + "]";
        String message = "";
        if (punish.isSilent()) {
            message = silentMessage + " ";
        }
        message = message + basicPunishMessage;
        message = String.format(message, Bukkit.getOfflinePlayer(punish.getPlayer()).getName(), punish.getType().getDisplayName(), punish.getPunisher(), StringUtils.isBlank(punish.getReason()) ? "None" : punish.getReason());
        if(punish.isSilent()){
            Bukkit.broadcast(message, "banmanager.punishment.silent");
        }
        Bukkit.broadcastMessage(message);
    }

    public List<Punishment> getLastBans(Long lasttime) {
        if (!this.isEnabled()) {
            return Collections.emptyList();
        }
        List<Punishment> lastbans = new ArrayList<>();
        Database.get().getUnsafeConnection().find(Punishment.class).disableValidation().field("punishtime").greaterThan(lasttime).field("active").equal(true).asList().stream().sorted((p1, p2) -> {
            long a = p1.getExpire() - p2.getExpire();
            int i = a < 0 ? -1 : a > 0 ? 1 : 0;
            return i;
        }).forEachOrdered(lastbans::add);
        return lastbans;
    }

    public void overridePunishment(Punishment punish, UUID player, Type type) {
        Query<Punishment> query = Database.get().getUnsafeConnection().createQuery(Punishment.class).disableValidation().field("_id").equal(player).field("type").equal(type).field("active").equal(true);
        UpdateOperations<Punishment> update = Database.get().getUnsafeConnection().createUpdateOperations(Punishment.class)
                .set("punisher", punish.getPunisher())
                .set("reason", punish.getReason())
                .set("expire", punish.getExpire())
                .set("ip", punish.getIp())
                .set("active", punish.isActive())
                .set("ippunishment", punish.isIppunishment())
                .set("silent", punish.isSilent());
        Database.get().getConnection().update(query, update);
    }

    public Long getActualTime() {
        if (!this.isEnabled()) {
            return System.currentTimeMillis();
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Monterrey"));
        Date currentDate = calendar.getTime();
        return currentDate.getTime();
    }

    public void unpunishPlayer(UUID player, Type type) {
        if (!this.isEnabled()) {
            return;
        }
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            Query<Punishment> query = Database.get().getConnection().createQuery(Punishment.class).disableValidation().field("_id").equal(player).field("active").equal(true).field("type").equal(type.hasBase() ? type.getBase() : type);
            UpdateOperations<Punishment> update = Database.get().getConnection().createUpdateOperations(Punishment.class).set("active", false);
            Database.get().getConnection().update(query, update);
        });
    }
    private void insertPunishment(Punishment punish) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> Database.get().getConnection().save(punish));
        Bukkit.getPluginManager().callEvent(new PlayerPunishEvent(punish.getPlayer(), punish));
        this.displayPunishmentMessage(punish);
    }

    public void insertPermanentBan(final UUID punished, String punisher, String reason, boolean silent) {
        if (!this.isEnabled()) {
            return;
        }
        if (punished == null)
            return;
        punisher = StringUtils.isBlank(punisher) ? "Console" : punisher;
        reason = StringUtils.isBlank(punisher) ? "None" : reason;
        String ip = Bukkit.getPlayer(punished) != null
                ? Bukkit.getPlayer(punished).getAddress().getAddress().getHostAddress()
                : "##offline##";
        Punishment punish = new Punishment(punished, punisher, reason, 0L, this.getActualTime(), ip, Type.BAN, true, false, silent, ServerSettings.NAME);
        this.insertPunishment(punish);
    }

    public void insertPermanentMute(final UUID punished, String punisher, String reason, boolean silent) {
        if (!this.isEnabled()) {
            return;
        }
        if (punished == null)
            return;
        punisher = StringUtils.isBlank(punisher) ? "Console" : punisher;
        reason = StringUtils.isBlank(punisher) ? "None" : reason;
        String ip = Bukkit.getPlayer(punished) != null
                ? Bukkit.getPlayer(punished).getAddress().getAddress().getHostAddress()
                : "##offline##";
        Punishment punish = new Punishment(punished, punisher, reason, 0l, this.getActualTime(), ip, Type.MUTE, true, false, silent, ServerSettings.NAME);
        this.insertPunishment(punish);

    }

    public void insertTemporalMute(final UUID punished, long expire, String punisher,
                                   String reason, boolean silent) {
        if (!this.isEnabled()) {
            return;
        }
        if (punished == null)
            return;
        punisher = StringUtils.isBlank(punisher) ? "Console" : punisher;
        reason = StringUtils.isBlank(punisher) ? "None" : reason;
        long expiration = this.getActualTime() + expire;

        String ip = Bukkit.getPlayer(punished) != null
                ? Bukkit.getPlayer(punished).getAddress().getAddress().getHostAddress()
                : "##offline##";
        Punishment punish = new Punishment(punished, punisher, reason, expiration, this.getActualTime(), ip, Type.MUTE, true, false, silent, ServerSettings.NAME);
        this.insertPunishment(punish);
    }

    public void insertIpMute(final UUID punished, long expire, String punisher, String reason, boolean silent) {
        if (!this.isEnabled()) {
            return;
        }
        if (punished == null)
            return;
        punisher = StringUtils.isBlank(punisher) ? "Console" : punisher;
        reason = StringUtils.isBlank(punisher) ? "None" : reason;
        long expiration = this.getActualTime() + expire;

        String ip = Bukkit.getPlayer(punished) != null
                ? Bukkit.getPlayer(punished).getAddress().getAddress().getHostAddress()
                : "##offline##";
        Punishment punish = new Punishment(punished, punisher, reason, expiration, this.getActualTime(), ip, Type.MUTE, true, true, silent, ServerSettings.NAME);
        this.insertPunishment(punish);
    }

    public void insertKick(final UUID punished, String punisher, String reason, boolean silent) {
        if (!this.isEnabled()) {
            return;
        }
        if (punished == null)
            return;
        punisher = StringUtils.isBlank(punisher) ? "Console" : punisher;
        reason = StringUtils.isBlank(reason) ? "None" : reason;
        Punishment punish = new Punishment(punished, punisher, reason, 0l, this.getActualTime(), "##offline##", Type.KICK, true, false, silent, ServerSettings.NAME);
        this.insertPunishment(punish);
    }

    public void insertIpban(final UUID punished, String ip, String punisher, String reason, boolean silent) {
        if (!this.isEnabled()) {
            return;
        }
        if (punished == null)
            return;
        punisher = StringUtils.isBlank(punisher) ? "Console" : punisher;
        reason = StringUtils.isBlank(punisher) ? "None" : reason;

        ip = Bukkit.getPlayer(punished) != null
                ? Bukkit.getPlayer(punished).getAddress().getAddress().getHostAddress()
                : "##offline##";
        Punishment punish = new Punishment(punished, punisher, reason, 0l, this.getActualTime(), ip, Type.BAN, true, true, silent, ServerSettings.NAME);
        this.insertPunishment(punish);
    }

    public void insertTemporalBan(final UUID punished, long expire, @Nullable String punisher,
                                  String reason, boolean silent) {
        if (!this.isEnabled()) {
            return;
        }
        if (punished == null)
            return;
        punisher = StringUtils.isBlank(punisher) ? "Console" : punisher;
        reason = StringUtils.isBlank(punisher) ? "None" : reason;
        long expiration = this.getActualTime() + expire;

        String ip = Bukkit.getPlayer(punished) != null
                ? Bukkit.getPlayer(punished).getAddress().getAddress().getHostAddress()
                : "##offline##";
        Punishment punish = new Punishment(punished, punisher, reason, expiration, this.getActualTime(), ip, Type.BAN, true, false, silent, ServerSettings.NAME);
        this.insertPunishment(punish);
    }

}