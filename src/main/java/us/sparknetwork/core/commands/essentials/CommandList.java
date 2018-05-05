package us.sparknetwork.core.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;

public class CommandList extends CommandModule {

    public CommandList() {
        super("list", 0, 0, "Usage /(command)");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.DARK_GRAY + CorePlugin.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.DARK_GRAY + " » " + CoreConstants.YELLOW
                + String.format("There are %1$s of %2$s players connected.", Bukkit.getOnlinePlayers().size(),
                CorePlugin.getPlugin().getServerHandler().getMaxPlayers()));
        sender.sendMessage(ChatColor.DARK_GRAY + CorePlugin.STRAIGHT_LINE_DEFAULT);
        return true;
    }

}
