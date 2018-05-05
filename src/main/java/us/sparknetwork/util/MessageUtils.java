package us.sparknetwork.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static void sendMessageToStaff(String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("network.staff")) {
                player.sendMessage(msg);
            }
        }
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
