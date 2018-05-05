package us.sparknetwork.core.handlers;

import java.util.List;
import java.util.stream.Collectors;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.scheduler.BukkitTask;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.user.UserManager;
import us.sparknetwork.util.SpigotUtils;

public class FreezeManager implements Listener {

    public JavaPlugin plugin;
    public BukkitTask freezeTask;

    public FreezeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        freezeTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Bukkit.getOnlinePlayers().stream().map(p -> UserManager.getInstance().getUser(p.getUniqueId())).forEach(user -> {
                if (user.isFreezed()) {
                    List<String> message = CorePlugin.getPlugin().getServerHandler().getFreezeMessage().stream().map(str -> {
                        str = ChatColor.translateAlternateColorCodes('&', str);
                        String finalMessage = String.format(str, CoreConstants.TEAMSPEAK).replace("%YELLOW%", CoreConstants.YELLOW.toString()).replace("%GRAY%", CoreConstants.GRAY.toString()).replace("%GOLD%", CoreConstants.GOLD.toString());
                        return finalMessage;
                    }).collect(Collectors.toList());
                    user.getPlayer().sendMessage(message.toArray(new String[message.size()]));
                }
            });
        }, 0, 20 * 60 * 5);
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        if (!user.isFreezed()) {
            return;
        }
        ComponentBuilder builder = new ComponentBuilder("The player ").color(SpigotUtils.toBungee(CoreConstants.YELLOW)).bold(true)
                .append(e.getPlayer().getName()).color(net.md_5.bungee.api.ChatColor.RED).bold(true)
                .append(" leaved the server in SS").color(SpigotUtils.toBungee(CoreConstants.YELLOW)).bold(true)
                .append("(Click here to ban him).").color(SpigotUtils.toBungee(CoreConstants.GRAY)).bold(true).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + e.getPlayer().getName() + " Refused to SS"));
        e.getPlayer().spigot().sendMessage(builder.create());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        if (!user.isFreezed()) {
            return;
        }

        if (e.getFrom().getZ() != e.getTo().getZ() || e.getFrom().getX() != e.getTo().getX()) {
            e.setTo(e.getFrom());

            List<String> message = CorePlugin.getPlugin().getServerHandler().getFreezeMessage().stream().map(str -> {
                str = ChatColor.translateAlternateColorCodes('&', str);
                String finalMessage = String.format(str, CoreConstants.TEAMSPEAK).replace("%YELLOW%", CoreConstants.YELLOW.toString()).replace("%GRAY%", CoreConstants.GRAY.toString()).replace("%GOLD%", CoreConstants.GOLD.toString());
                return finalMessage;
            }).collect(Collectors.toList());
            e.getPlayer().sendMessage(message.toArray(new String[message.size()]));

        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        if (!user.isFreezed()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        if (!user.isFreezed()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        if (!user.isFreezed()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventory(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();

            IUser user = UserManager.getInstance().getUser(player.getUniqueId());
            if (!user.isFreezed()) {
                return;
            }

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void freezedDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            IUser damager = UserManager.getInstance().getUser(e.getDamager().getUniqueId());
            IUser entity = UserManager.getInstance().getUser(e.getEntity().getUniqueId());
            if (entity.isFreezed() || damager.isFreezed()) {
                e.setCancelled(true);
            }
            if (entity.isFreezed()) {
                damager.getPlayer().sendMessage(CoreConstants.YELLOW + "You can't damage freezed players.");
            }
            if(damager.isFreezed()){
                entity.getPlayer().sendMessage(CoreConstants.YELLOW+"You can't damage entities while you are freezed.");
            }
        } else if (e.getEntity() instanceof Player){
            IUser player = UserManager.getInstance().getUser(e.getEntity().getUniqueId());
            if(player.isFreezed()){
                e.setCancelled(true);
            }
        }
    }

}
