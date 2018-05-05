package us.sparknetwork.core.commands.inventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableList;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.PlayerUtils;

public class CommandGive extends CommandModule {
    public CorePlugin plugin;

    public CommandGive(CorePlugin plugin) {
        super("give", 2, 3, "Usage: /(command) <player> <item> [quantity]", ImmutableList.of("gave"));
        this.plugin = plugin;
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (args.length == 2) {
            Player target = PlayerUtils.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                return true;
            }
            ItemStack item = plugin.getItemDb().getItem(args[1]);
            if (item == null) {
                sender.sendMessage(CoreConstants.GOLD + String.format("You must provide a valid item name or id.", args[0]));
                return true;
            }
            target.getInventory().addItem(item);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Sucessfully gave the item %1$s with quantity %2$s to %3$s.",
                            plugin.getItemDb().getName(item), item.getAmount() + "", target.getName()));
            return true;
        }
        if (args.length == 3) {
            Player target = PlayerUtils.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                return true;
            }
            ItemStack item = plugin.getItemDb().getItem(args[1]);
            if (item == null) {
                sender.sendMessage(CoreConstants.GOLD + String.format("Item with name or id %1$s not found.", args[0]));
                return true;
            }
            item.setAmount(parseInt(args[2]));
            target.getInventory().addItem(item);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Sucessfully gave the item %1$s with quantity %2$s to %3$s.",
                            plugin.getItemDb().getName(item), item.getAmount() + "", target.getName()));
            return true;
        }
        return false;
    }

    public Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

}
