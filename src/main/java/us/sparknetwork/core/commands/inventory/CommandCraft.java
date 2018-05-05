package us.sparknetwork.core.commands.inventory;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;

public class CommandCraft extends CommandModule {

    public CommandCraft() {
        super("craft", 0, 0, "Usage /(command)", Arrays.asList("workbench", "crafttable"));
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        player.openWorkbench(null, true);
        broadcastCommandMessage(sender, CoreConstants.YELLOW + "Sucessfully openned a workbench.");
        return true;
    }


}
