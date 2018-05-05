package us.sparknetwork.core.commands.essentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;

public class CommandSetMaxPlayers extends CommandModule {

    public CommandSetMaxPlayers() {
        super("setmaxplayers", 1, 1, "Usage /(command) <number>");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        int maxplayers;
        try {
            maxplayers = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "You must provide a valid quantity of max players.");
            return true;
        }
        CorePlugin.getPlugin().getServerHandler().setMaxPlayers(maxplayers);
        Command.broadcastCommandMessage(sender,
                CoreConstants.YELLOW + String.format("Setted the max players of the server to %1$s.", maxplayers));
        return true;
    }

}
