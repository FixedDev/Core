package us.sparknetwork.basics;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

public class ConfigurationService {

	public static ChatColor COLOR_1 = ChatColor.YELLOW, COLOR_2 = ChatColor.GOLD;
	public static String NETWORK_NAME = "SparkNetwork";
	public static List<String> GLOBAL_ANNOUNCEMENTS = new ArrayList<>();
	public static boolean ANNOUNCER_ENABLED = false;
	public static long ANNOUNCER_DELAY = 300;
	public static String MOTD = "A Minecraft server";
	public static String MAINTENANCE_MESSAGE = "This network is currently in maintenance, join later for news.";
	public static String BUNGEE_NAME = "Principal-Bungee";

	public static void init() {
		COLOR_1 = ChatColor.valueOf(BasicsPlugin.getPlugin().getConfig().getString("color1", "YELLOW"));
		COLOR_2 = ChatColor.valueOf(BasicsPlugin.getPlugin().getConfig().getString("color2", "GOLD"));
		NETWORK_NAME = BasicsPlugin.getPlugin().getConfig().getString("network-name", "SparkNetwork");
		GLOBAL_ANNOUNCEMENTS = BasicsPlugin.getPlugin().getConfig().getStringList("global-announcer.announcements");
		ANNOUNCER_ENABLED = BasicsPlugin.getPlugin().getConfig().getBoolean("global-announcer.enabled", false);
		ANNOUNCER_DELAY = BasicsPlugin.getPlugin().getConfig().getLong("global-announcer.delay", 300);
		MOTD = BasicsPlugin.getPlugin().getConfig().getString("motd");
		MAINTENANCE_MESSAGE = BasicsPlugin.getPlugin().getConfig().getString("network-maintenance-msg");
		BUNGEE_NAME = BasicsPlugin.getPlugin().getConfig().getString("bungee-name");
	}

}
