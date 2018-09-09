package us.sparknetwork.hub;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import lombok.Getter;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.commands.CommandModule;
import us.sparknetwork.core.commands.ReflectionCommandHandler;
import us.sparknetwork.hub.commands.SetSpawnCommand;
import us.sparknetwork.hub.commands.SpawnCommand;
import us.sparknetwork.hub.listeners.BasicListeners;
import us.sparknetwork.hub.listeners.ServerSelector;
import us.sparknetwork.hub.scoreboard.ScoreboardHandler;

public class HubPlugin extends JavaPlugin implements PluginMessageListener {

	static {
		STRAIGHT_LINE_TEMPLATE = ChatColor.STRIKETHROUGH.toString() + Strings.repeat("-", 256);
		STRAIGHT_LINE_DEFAULT = HubPlugin.STRAIGHT_LINE_TEMPLATE.substring(0, 55);
	}
	private static final String STRAIGHT_LINE_TEMPLATE;
	public static final String STRAIGHT_LINE_DEFAULT;

	int PlayersInNetwork = 0;

	static @Getter private HubPlugin plugin;
	@Getter
	private ScoreboardHandler scoreboardhandler;
	@Getter
	public PluginManager pm;
	BukkitRunnable worldrunnable;
	@Getter
	ReflectionCommandHandler cmdhandler;


	@Override
	public void onLoad() {
		plugin = this;
		

		pm = this.getServer().getPluginManager();

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

		ConfigurationService.init();
	}

	@Override
	public void onEnable() {
		
		if(!Bukkit.getPluginManager().isPluginEnabled("Core")) {
			Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("Core"));
		}
		
		getLogger().info("Registering basic listeners");
		pm.registerEvents(new BasicListeners(this), this);
		getLogger().info("Registering server selector listeners");
		pm.registerEvents(new ServerSelector(this), this);
		
		getLogger().info("Registering commands");
		List<CommandModule> cmds = new ArrayList<>();
		cmdhandler = new ReflectionCommandHandler(this);
		cmds.add(new SetSpawnCommand());
		cmds.add(new SpawnCommand());
		for(CommandModule cmd : cmds) {
			cmd.setPermissionMessage(CoreConstants.GOLD + ChatColor.BOLD.toString() + CoreConstants.NAME
					+ ChatColor.DARK_GRAY + "�" + CoreConstants.GRAY + "You don't have permission to execute this command").setPermission("hub.command." + cmd.getName());
		}
		cmdhandler.registerAll(cmds);

		scoreboardhandler = new ScoreboardHandler();
		setup();
	}

	public void setup() {
		Bukkit.getWorlds().forEach(world -> {
			world.setDifficulty(Difficulty.PEACEFUL);
			world.setTime(0);
			world.setPVP(false);
			world.setAmbientSpawnLimit(0);
			world.setAnimalSpawnLimit(0);
			world.setMonsterSpawnLimit(0);
			worldrunnable = new BukkitRunnable() {

				@Override
				public void run() {
					World world = Bukkit.getWorld("world");
					world.setTime(1000);
				}

			};
			worldrunnable.runTaskTimer(this, 0, 20 * 1 * 60);
		});
	}

	public void sendToServer(Player player, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendMessage(
				CoreConstants.YELLOW + String.format("Sending to %1$s server.", WordUtils.capitalizeFully(server)));
		player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}

	public int getPlayersOnline() {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF("ALL");

		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
		return PlayersInNetwork;

	}

	@SuppressWarnings("unused")
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("PlayerCount")) {
			String server = in.readUTF();
			int playercount = in.readInt();
			PlayersInNetwork = playercount;
		}
	}

}
