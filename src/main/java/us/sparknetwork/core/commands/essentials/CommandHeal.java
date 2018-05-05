package us.sparknetwork.core.commands.essentials;

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

public class CommandHeal extends CommandModule {

    public CommandHeal(CorePlugin plugin) {
        super("heal", 0, 1, "Usage /(command) [player]");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && args.length < 1) {
            return false;
        }
        if (args.length == 0) {
            Player pl = (Player) sender;
            pl.setFoodLevel(20);
            pl.setSaturation(10);
            pl.setExhaustion(0F);
            pl.setHealth(20.0);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Sucessfully healed the player %1$s", pl.getName()));
            return true;
        }
        if (args.length == 1) {
            if (!sender.hasPermission("core.command.heal.others")) {
                sender.sendMessage(this.getPermissionMessage());
                return true;
            }
            Player pl = PlayerUtils.getPlayer(args[0]);
            if (pl == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                return true;
            }
            pl.setFoodLevel(20);
            pl.setSaturation(10);
            pl.setExhaustion(0F);
            pl.setHealth(20.0);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Sucessfully healed the player %1$s", pl.getName()));
            return true;
        }
        return false;
    }

}
