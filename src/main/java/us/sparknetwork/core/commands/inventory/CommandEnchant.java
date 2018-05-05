package us.sparknetwork.core.commands.inventory;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;

public class CommandEnchant extends CommandModule {

    public CommandEnchant() {
        super("enchant", 1, 2, "Usage /enchant <enchantment> [level]");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Enchantment enc = this.getEnchantment(args[0]);
        if (enc == null) {
            sender.sendMessage(
                    CoreConstants.GOLD + String.format("Enchantment with name or id %1$s not found.", args[0]));
            return true;
        }
        Player target = (Player) sender;
        ItemStack item = target.getItemInHand();
        int level = 1;
        if (args.length == 2) {
            level = Integer.parseInt(args[1]);
        }
        item.addUnsafeEnchantment(enc, level);
        Command.broadcastCommandMessage(sender,
                CoreConstants.YELLOW + String.format("Sucessfully added a enchantment to the item in the hand of %1$s.", sender.getName()));
        return true;
    }

    @SuppressWarnings("deprecation")
    public Enchantment getEnchantment(String string) {
        Enchantment enc = Enchantment.getByName(string.toUpperCase());
        if (enc == null) {
            try {
                enc = Enchantment.getById(Integer.parseInt(string));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return enc;
    }
}
