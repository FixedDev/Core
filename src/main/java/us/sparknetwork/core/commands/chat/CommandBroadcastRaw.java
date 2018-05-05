package us.sparknetwork.core.commands.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableList;

import us.sparknetwork.api.command.CommandModule;

public class CommandBroadcastRaw extends CommandModule {
    public CommandBroadcastRaw(JavaPlugin plugin) {
        super("broadcastraw", 1, 900, "Usage /(command) <message>", ImmutableList.of("bcraw", "rawcast"));
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
        Bukkit.getServer().broadcastMessage(color(mensaje.toString()));

        return true;
    }

}
