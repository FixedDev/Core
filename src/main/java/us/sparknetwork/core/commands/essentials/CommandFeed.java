package us.sparknetwork.core.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.PlayerUtils;

public class CommandFeed extends CommandModule {

    public CommandFeed(CorePlugin plugin) {
        super("feed", 0, 1, "Usage /(command) [player]");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && args.length < 1) {
            return false;
        }
        if (args.length == 0) {
            Player pl = (Player) sender;
            FoodLevelChangeEvent e = new FoodLevelChangeEvent(pl, 20);
            Bukkit.getServer().getPluginManager().callEvent(e);
            pl.setFoodLevel(20);
            pl.setSaturation(10);
            pl.setExhaustion(0F);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Sucessfully feeded the player %1$s", sender.getName()));
            return true;
        }
        if (args.length == 1) {
            if (!sender.hasPermission("core.command.feed.others")) {
                sender.sendMessage(this.getPermissionMessage());
                return true;
            }
            Player target = PlayerUtils.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                return true;
            }
            FoodLevelChangeEvent e = new FoodLevelChangeEvent(target, 20);
            Bukkit.getServer().getPluginManager().callEvent(e);
            target.setFoodLevel(20);
            target.setSaturation(10);
            target.setExhaustion(0F);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Sucessfully feeded the player %1$s", target.getName()));
            return true;
        }
        return false;
    }

}
