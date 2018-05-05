package us.sparknetwork.core;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import us.sparknetwork.util.ChatColorUtils;

public class CoreConstants {

    public static final String PLAYER_WITH_NAME_OR_UUID;
    public static ChatColor YELLOW;
    public static ChatColor GOLD;
    public static ChatColor GRAY;
    public static String NAME;
    public static String TEAMSPEAK;
    public static String SITE;
    public static String STORE;

    static {
        YELLOW = ChatColor.YELLOW;
        GOLD = ChatColor.GOLD;
        GRAY = ChatColor.GRAY;

        PLAYER_WITH_NAME_OR_UUID = ChatColor.GOLD + "Player with name or uuid" + ChatColor.GREEN + " %1$s "
                + ChatColor.GOLD + "not found";

        NAME = "SparkNetwork";
        TEAMSPEAK = "ts.sparknetwork.us";
        SITE = "www.sparknetwork.us";
        STORE = "store.sparknetwork.us";
    }

    public void getConstants(ConfigurationSection sec) {
        YELLOW = ChatColorUtils.fromString(sec.getString("colors.yellow", "YELLOW"));
        GOLD = ChatColorUtils.fromString(sec.getString("colors.gold", "GOLD"));
        GRAY = ChatColorUtils.fromString(sec.getString("colors.gray", "GRAY"));

        NAME = sec.getString("info.name", "SparkNetwork");
        TEAMSPEAK = sec.getString("info.teamspeak", "ts.sparknetwork.us");
        SITE = sec.getString("info.site", "www.sparknetwork.us");
        STORE = sec.getString("info.store", "store.sparknetwork.us");

    }

}
