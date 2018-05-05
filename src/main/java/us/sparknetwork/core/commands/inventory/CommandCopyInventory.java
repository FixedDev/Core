package us.sparknetwork.core.commands.inventory;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.util.PlayerUtils;

public class CommandCopyInventory extends CommandModule {

    public CommandCopyInventory() {
        super("copyinventory", 1, 1, "Usage /(command) <player>", "copyinv");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        Player player = (Player) sender;
        Player target = PlayerUtils.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
            return true;
        }
        ItemStack[] armor = target.getInventory().getArmorContents();
        ItemStack[] inventory = target.getInventory().getContents();
        player.getInventory().setArmorContents(armor);
        player.getInventory().setContents(inventory);
        player.updateInventory();
        broadcastCommandMessage(sender, CoreConstants.YELLOW + String.format("Sucessfully copied the inventory of %1$s.", target.getName()));
        return true;
    }

}
