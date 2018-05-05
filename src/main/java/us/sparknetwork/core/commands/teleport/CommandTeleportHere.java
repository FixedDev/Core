package us.sparknetwork.core.commands.teleport;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.PlayerUtils;

public class CommandTeleportHere extends CommandModule {

    public CommandTeleportHere(CorePlugin plugin) {
        super("teleporthere", 1, 1, "/<command> <target>", "tphere");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        Player target = PlayerUtils.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
            return true;
        }
        target.teleport(player, TeleportCause.COMMAND);
        sender.sendMessage(CoreConstants.YELLOW + String.format("Teleported player %1$s to you.", target.getName()));
        return true;

    }

}
