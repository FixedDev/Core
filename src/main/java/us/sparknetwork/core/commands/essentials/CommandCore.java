package us.sparknetwork.core.commands.essentials;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CorePlugin;

public class CommandCore extends CommandModule {

    public final CorePlugin plugin;

    public CommandCore(CorePlugin plugin) {
        super("core", 1, 1, "Usage /core [reload]");
        this.plugin = plugin;
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                try {
                    sender.sendMessage(ChatColor.GREEN + "The plugin doesn't have a native reload, restart the serverr.");
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "SparkNetworkCore reloaded with errors check console");
                    e.printStackTrace();
                }
                return true;

            }
            if (args[0].equalsIgnoreCase("savedata")) {
                plugin.saveData();
                sender.sendMessage(ChatColor.GREEN + "Saved data of SparkNetworkCore");
                return true;
            }
        }
        return false;
    }
}
