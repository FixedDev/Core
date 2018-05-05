package us.sparknetwork.core.commands.teleport;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.util.LocationUtils;

public class CommandTop extends CommandModule {

    public CommandTop() {
        super("top", 0, 0, "Usage /(command)");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        Player player = (Player) sender;
        Location high = LocationUtils.getHighestLocation(player.getLocation());
        if (high != null && !high.equals(player.getLocation()) && (high.getY() - player.getLocation().getY()) > 1) {
            player.teleport(high.add(0, 1, 0));
            sender.sendMessage(CoreConstants.YELLOW + "Sucessfully teleported to the heighest location.");
        } else {
            sender.sendMessage(CoreConstants.GOLD + "No highest location found.");
        }
        return true;
    }

}
