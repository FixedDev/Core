package us.sparknetwork.core.commands.inventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableList;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.util.PlayerUtils;

public class CommandClearInventory extends CommandModule {

    public CommandClearInventory(JavaPlugin plugin) {
        super("clearinventory", 0, 1, "Usage: /(command) [player]", ImmutableList.of("ci"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if(!(sender instanceof Player) && args.length < 1){
            return false;
        }
        if (args.length == 0) {
            Player player = (Player) sender;
            player.getInventory().clear();
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Sucessfully cleared the inventory of %1$s.", sender.getName()));
        }
        if (args.length == 1) {
            Player target = PlayerUtils.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                return true;
            }
            target.getInventory().clear();
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Sucessfully cleared the inventory of %1$s.", target.getName()));
        }
        return true;
    }

}
