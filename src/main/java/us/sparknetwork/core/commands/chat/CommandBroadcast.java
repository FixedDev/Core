package us.sparknetwork.core.commands.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableList;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;

public class CommandBroadcast extends CommandModule {
    public CommandBroadcast(JavaPlugin plugin) {
        super("broadcast", 1, 900, "Usage /(command) <message>", ImmutableList.of("bc"));
    }

    public String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        StringBuilder mensaje = new StringBuilder();
        for (String arg : args) {
            mensaje.append(arg + " ");
        }
        Bukkit.getServer().broadcastMessage(
                ChatColor.DARK_GRAY + "[" + CoreConstants.GOLD + CoreConstants.NAME + ChatColor.DARK_GRAY + "] "
                        + ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', mensaje.toString()));
        return true;
    }

}
