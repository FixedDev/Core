package us.sparknetwork.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.user.UserManager;

public class VanishListeners implements Listener {

    @EventHandler
    public void dropEvent(PlayerDropItemEvent e) {
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        if (user.isVanished()) {
            e.getPlayer().sendMessage(CoreConstants.YELLOW + "You can't drop items in vanish mode".replace("&", "§"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        if (user.isVanished()) {
            e.getPlayer().sendMessage(CoreConstants.YELLOW + "You can't break blocks in vanish mode".replace("&", "§"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        if (user.isVanished()) {
            e.getPlayer().sendMessage(CoreConstants.YELLOW + "You can't place blocks in vanish mode".replace("&", "§"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        Player pl = e.getPlayer();
        Action ac = e.getAction();
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        if (user.isVanished()) {
            if (ac.equals(Action.RIGHT_CLICK_BLOCK) && !pl.isSneaking()) {
                Block bl = e.getClickedBlock();
                if (bl.getType().equals(Material.CHEST) || bl.getType().equals(Material.TRAPPED_CHEST)) {
                    e.setCancelled(true);
                    Chest cs = (Chest) bl.getState();
                    Inventory inv = Bukkit.createInventory(null, cs.getInventory().getSize());
                    inv.setContents(cs.getInventory().getContents());
                    pl.openInventory(inv);
                }
            }
            if (ac.equals(Action.PHYSICAL)) {
                e.setCancelled(true);
            }
            if (ac.equals(Action.RIGHT_CLICK_BLOCK)) {
                Block bl = e.getClickedBlock();
                if (bl.getType().equals(Material.LEVER) || bl.getType().equals(Material.STONE_BUTTON)
                        || bl.getType().equals(Material.WOOD_BUTTON)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void thisDamage(EntityDamageByEntityEvent e) {
        Entity en = e.getDamager();
        if (en instanceof Player) {
            Player pl = (Player) en;
            IUser user = UserManager.getInstance().getUser(pl.getUniqueId());
            if (user.isVanished()) {
                pl.sendMessage(CoreConstants.YELLOW + "You can't damage entities in vanish mode");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void thisDamage2(EntityDamageEvent e) {
        Entity en = e.getEntity();
        if (en instanceof Player) {
            Player pl = (Player) en;
            IUser user = UserManager.getInstance().getUser(pl.getUniqueId());
            if (user.isVanished()) {
                e.setCancelled(true);
            }
        }
    }

}
