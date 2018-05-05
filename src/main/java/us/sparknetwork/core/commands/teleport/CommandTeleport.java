package us.sparknetwork.core.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;

public class CommandTeleport extends CommandModule {

    public CommandTeleport(CorePlugin plugin) {
        super("teleport", 1, 4, "Usage /(command) <target> [player] | [target] <x> <y> <z>", "tp");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player pl = Bukkit.getPlayer(args[0]);
                if (pl == null) {
                    sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                    return true;
                }
                ((Player) sender).teleport(pl, TeleportCause.COMMAND);
                sender.sendMessage(CoreConstants.YELLOW + "Teleported to " + pl.getName() + ".");
                return true;
            }
            return false;
        }
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            Player pl = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                return true;
            }
            if (pl == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[1]));
                return true;
            }
            pl.teleport(target, TeleportCause.COMMAND);
            sender.sendMessage(CoreConstants.YELLOW + "Teleported " + pl.getName() + " to " + target.getName() + ".");
            return true;
        }
        if (args.length == 3) {
            if (sender instanceof Player) {
                try {
                    Double x = Double.parseDouble(args[0]);
                    Double y = Double.parseDouble(args[1]);
                    Double z = Double.parseDouble(args[2]);
                    Location loc = new Location(((Player) sender).getWorld(), x, y, z);
                    ((Player) sender).teleport(loc, TeleportCause.COMMAND);
                    sender.sendMessage(CoreConstants.YELLOW + "Teleported to " + loc.getX() + ", " + loc.getY() + ", "
                            + loc.getZ() + ".");
                    return true;

                } catch (NumberFormatException e) {
                    sender.sendMessage(CoreConstants.GOLD + "Invalid coordinates you must provide valid coordinates.");
                    return true;
                }

            } else {
                return false;
            }
        }
        if (args.length == 4) {
            Player pl = Bukkit.getPlayer(args[0]);
            if (pl == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                return true;
            }
            try {

                Double x = Double.parseDouble(args[1]);
                Double y = Double.parseDouble(args[2]);
                Double z = Double.parseDouble(args[3]);
                Location loc = new Location(pl.getWorld(), x, y, z);
                pl.teleport(loc, TeleportCause.COMMAND);
                sender.sendMessage(CoreConstants.YELLOW + "Teleported " + pl.getName() + " to " + loc.getX() + ", "
                        + loc.getY() + ", " + loc.getZ() + ".");
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage(CoreConstants.GOLD + "Invalid coordinates you must provide valid coordinates.");
                return true;
            }
        }
        return false;
    }

}
