package us.sparknetwork.core.commands.essentials;

import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;

public class CommandRepair extends CommandModule {

    public CommandRepair(JavaPlugin plugin) {
        super("repair", 0, 1, "Usage: /(command) [all]", ImmutableList.of("fix"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        if (args.length == 0) {
            Player pl = (Player) sender;
            ItemStack it = pl.getItemInHand();
            if (!isRepairable(it)) {
                sender.sendMessage(CoreConstants.GOLD + "You can't repair that item.");
                return true;
            }
            it.setDurability((short) 0);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Repaired the item in the hand of %1$s.", pl.getName()));
            return true;
        }
        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("all")) {
                if (!sender.hasPermission(this.getPermission() + ".all")) {
                    sender.sendMessage(this.getPermissionMessage());
                    return true;
                }
                Player pl = (Player) sender;
                for (ItemStack it : pl.getInventory().getContents()) {
                    if (isRepairable(it)) {
                        it.setDurability((short) 0);
                        continue;
                    }

                }
                for (ItemStack it : pl.getInventory().getArmorContents()) {
                    if (isRepairable(it)) {
                        it.setDurability((short) 0);
                        continue;
                    }
                }
                Command.broadcastCommandMessage(sender, CoreConstants.YELLOW
                        + String.format("Repaired all the repairable items in the inventory of %1$s.", pl.getName()));
            }
            return false;
        }
        return false;
    }

    public boolean isRepairable(ItemStack item) {
        if (item == null) {
            return false;
        }
        Material material = item.getType();
        if (material == null)
            return false;
        if (material == Material.AIR) {
            return false;
        }

        if (material == Material.WOOD_AXE || material == Material.WOOD_HOE || material == Material.WOOD_SWORD
                || material == Material.WOOD_SPADE || material == Material.WOOD_PICKAXE
                || material == Material.STONE_AXE || material == Material.STONE_HOE || material == Material.STONE_SWORD
                || material == Material.STONE_SPADE || material == Material.STONE_PICKAXE
                || material == Material.GOLD_AXE || material == Material.GOLD_HOE || material == Material.GOLD_SWORD
                || material == Material.GOLD_SPADE || material == Material.GOLD_PICKAXE || material == Material.IRON_AXE
                || material == Material.IRON_HOE || material == Material.IRON_SWORD || material == Material.IRON_SPADE
                || material == Material.IRON_PICKAXE || material == Material.DIAMOND_AXE
                || material == Material.DIAMOND_HOE || material == Material.DIAMOND_SWORD
                || material == Material.DIAMOND_SPADE || material == Material.DIAMOND_PICKAXE ||

                material == Material.DIAMOND_BOOTS || material == Material.DIAMOND_CHESTPLATE
                || material == Material.DIAMOND_HELMET || material == Material.DIAMOND_LEGGINGS
                || material == Material.IRON_BOOTS || material == Material.IRON_CHESTPLATE
                || material == Material.IRON_HELMET || material == Material.IRON_LEGGINGS
                || material == Material.GOLD_BOOTS || material == Material.GOLD_CHESTPLATE
                || material == Material.GOLD_HELMET || material == Material.GOLD_LEGGINGS
                || material == Material.DIAMOND_BOOTS || material == Material.DIAMOND_CHESTPLATE
                || material == Material.DIAMOND_HELMET || material == Material.DIAMOND_LEGGINGS
                || material == Material.CHAINMAIL_BOOTS || material == Material.CHAINMAIL_CHESTPLATE
                || material == Material.CHAINMAIL_HELMET || material == Material.CHAINMAIL_LEGGINGS ||

                material == Material.FISHING_ROD || material == Material.SHEARS || material == Material.FLINT_AND_STEEL
                || material == Material.BOW)
            return true;

        return false;
    }
}
