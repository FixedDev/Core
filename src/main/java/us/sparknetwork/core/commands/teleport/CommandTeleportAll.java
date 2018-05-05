package us.sparknetwork.core.commands.teleport;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.PlayerUtils;

public class CommandTeleportAll extends CommandModule {

    public CommandTeleportAll(CorePlugin plugin) {
        super("teleportall", 0, 1, "Usage: /(command) [target]", "tpall");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && args.length < 1) {
            return false;
        }
        if (args.length == 0) {
            Player player = (Player) sender;
            Bukkit.getOnlinePlayers().stream().filter(p -> !p.equals(player)).forEach(p -> p.teleport(player));
            Command.broadcastCommandMessage(sender,
                    ChatColor.YELLOW + String.format("Teleported all players to %1$s.", sender.getName()));
            return true;

        }
        if (args.length == 1) {
            Player target = PlayerUtils.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                return true;
            }
            Bukkit.getOnlinePlayers().stream().filter(p -> !p.equals(target)).forEach(p -> p.teleport(target));
            Command.broadcastCommandMessage(sender,
                    ChatColor.YELLOW + String.format("Teleported all players to %1$s", target.getName()));
            return true;
        }
        return false;
    }

}
