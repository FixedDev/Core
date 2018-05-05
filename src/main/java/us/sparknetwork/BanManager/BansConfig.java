package us.sparknetwork.BanManager;

import java.util.ArrayList;
import java.util.List;

import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.Config;

public class BansConfig {

	public static boolean IS_LOBBY = false;
	public static String SERVER_NAME = "SparkNetwork";
	public static List<String> UNPUNISHABLE_PLAYERS = new ArrayList<>();
	public static Integer AUTO_REFRESH_TICKS = 5;

	public static void load() {

		Config config = new Config(CorePlugin.getPlugin(), "config");
		IS_LOBBY = config.getBoolean("bans.is-lobby");
		SERVER_NAME = config.getString("bans.display-server-name");
		UNPUNISHABLE_PLAYERS = config.getStringList("bans.unpunishable-players");
		AUTO_REFRESH_TICKS = config.getInt("bans.auto-refresh-ticks");
	}

}
