package us.sparknetwork.core.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.PlayerUtils;

public class CommandKill extends CommandModule {

    public CommandKill(CorePlugin plugin) {
        super("kill", 0, 1, "Usage /(command) [player]");
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && args.length < 1) {
            return false;
        }
        if (args.length == 0) {
            Player player = (Player) sender;
            EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
            Bukkit.getPluginManager().callEvent(ede);
            ede.getEntity().setLastDamageCause(ede);
            player.setHealth(0);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Killed the player %1$s.", player.getName()));
            return true;
        }
        if (args.length == 1) {
            if (!sender.hasPermission("core.command.kill.others")) {
                sender.sendMessage(this.getPermissionMessage());
                return true;
            }
            Player player = PlayerUtils.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                return true;
            }
            EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
            Bukkit.getPluginManager().callEvent(ede);
            ede.getEntity().setLastDamageCause(ede);
            player.setHealth(0);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Killed the player %1$s.", player.getName()));
            return true;
        }
        return false;

    }
}
