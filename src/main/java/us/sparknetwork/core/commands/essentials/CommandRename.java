package us.sparknetwork.core.commands.essentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;

public class CommandRename extends CommandModule {

    public CommandRename() {
        super("rename", 1, -1, "Usage /rename <name>");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            message.append(args[i]);
            if(i < args.length){
                message.append(" ");
            }
        }
        String itemName = ChatColor.translateAlternateColorCodes('&',message.toString());
        Player pl = (Player) sender;
        ItemStack it = pl.getItemInHand();
        ItemMeta im = it.getItemMeta();
        im.setDisplayName(itemName);
        it.setItemMeta(im);
        Command.broadcastCommandMessage(sender,
                CoreConstants.YELLOW + String.format("Changed the name of the item in the hand of %1$s to %2$s.",
                        pl.getName(), itemName));
        return true;
    }

}
