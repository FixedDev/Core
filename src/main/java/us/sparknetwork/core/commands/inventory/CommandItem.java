package us.sparknetwork.core.commands.inventory;

import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;

public class CommandItem extends CommandModule {

    public CommandItem() {
        super("item", 0, 1, "Usage: /(command) [quantity]", ImmutableList.of("i"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();
        int amount = item.getMaxStackSize();
        if (args.length == 1) {
            try {
                amount = Integer.parseInt(args[0]);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.GOLD + "You must specify a valid quantity.");
                return true;
            }
        }
        amount = amount > 127 ? 127 : amount;
        item.setAmount(amount);
        broadcastCommandMessage(sender, CoreConstants.YELLOW + String.format("Sucessfully setted the quantity of the item in the hand of %1$s to %2$s.", player.getName(), amount));
        return true;
    }
}
