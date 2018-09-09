package us.sparknetwork.hub.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.hub.ConfigurationService;
import us.sparknetwork.hub.HubPlugin;
import us.sparknetwork.util.ItemBuilder;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;

public class BasicListeners implements Listener {

	public BasicListeners(HubPlugin plugin) {
		plugin.getLogger().info("Registered basic listeners");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		if (e.getPlayer().hasPermission("hub.vip")) {
			e.setJoinMessage(CoreConstants.YELLOW + "(VIP)" + e.getPlayer().getName() + " joined to the server.");
		} else {
			e.setJoinMessage(null);
		}
		e.getPlayer().getInventory().clear();
		e.getPlayer().getInventory().setItem(4,
				new ItemBuilder(Material.COMPASS).displayName(CoreConstants.YELLOW + "Server Selector").build());
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 3));
		e.getPlayer().teleport(CorePlugin.getInstance().getSpawnManager().getLoc());
		e.getPlayer().sendMessage(CoreConstants.YELLOW + "Sucessfully teleported to the spawn.");
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(CorePlugin.getInstance().getSpawnManager().getLoc());
	}

	@EventHandler
	public void onPick(PlayerPickupItemEvent e) {
		if (ConfigurationService.isBUILDER_MODE()) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (ConfigurationService.isBUILDER_MODE()) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (e.getPlayer().hasPermission("hub.staff")) {
			return;
		}
		if (e.getPlayer().hasPermission("hub.vip")) {
			return;
		}

		e.setCancelled(true);
		e.getPlayer().sendMessage(CoreConstants.YELLOW + "You can't chat while you are in a hub.");

	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		if (e.getWorld().getEnvironment() == Environment.NORMAL && e.toWeatherState()) {
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		e.setFoodLevel(20);
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLeave(PlayerQuitEvent e) {
		e.setQuitMessage(null);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockbreak(BlockBreakEvent e) {
		Player player = (Player) e.getPlayer();

		if (ConfigurationService.isBUILDER_MODE()) {
			return;
		}

		if (!player.hasPermission("hub.build") && player.getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockplace(BlockPlaceEvent e) {
		Player player = (Player) e.getPlayer();

		if (ConfigurationService.isBUILDER_MODE()) {
			return;
		}

		if (!player.hasPermission("hub.build") && player.getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventory(InventoryClickEvent e) {

		if (ConfigurationService.isBUILDER_MODE()) {
			return;
		}

		if (e.getWhoClicked().getGameMode() != GameMode.CREATIVE && !e.getWhoClicked().hasPermission("hub.build")) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamage(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBurn(BlockBurnEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFade(BlockFadeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockGrow(BlockGrowEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLeavesDecay(LeavesDecayEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignChange(SignChangeEvent e) {
		if (ConfigurationService.isBUILDER_MODE()) {
			return;
		}

		if (!e.getPlayer().hasPermission("hub.build") && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e) {
		if (ConfigurationService.isBUILDER_MODE()) {
			return;
		}

		if (!e.getPlayer().hasPermission("hub.build") && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}
}
