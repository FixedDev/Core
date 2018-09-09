package us.sparknetwork.hub.listeners;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.server.Server;
import us.sparknetwork.hub.ConfigurationService;
import us.sparknetwork.hub.HubPlugin;
import us.sparknetwork.hub.server.SelectorParser;
import us.sparknetwork.util.ItemBuilder;

public class ServerSelector implements Listener {

	Inventory menu;
	HubPlugin plugin;

	public ServerSelector(HubPlugin plugin) {
		this.plugin = plugin;
		plugin.getLogger().info("Registered server selector listeners");

		menu = plugin.getServer().createInventory(null, 9,
				CoreConstants.GOLD + ChatColor.BOLD.toString() + CoreConstants.NAME);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSelector(PlayerInteractEvent e) {
		Player player = (Player) e.getPlayer();
		if (player.getItemInHand().getType() == Material.COMPASS) {

			for (int i = 0; i < menu.getSize(); i++) {
				menu.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 7).displayName("").build());
			}
			for (Entry<String, SelectorParser> server : ConfigurationService.getSELECTOR_SERVERS().entrySet()) {
				Server selectserver = CorePlugin.getInstance().getServerManager().getServerData(server.getKey());
				ItemStack serversel = new ItemBuilder(server.getValue().getItem())
						.displayName(CoreConstants.GOLD + ChatColor.BOLD.toString() + server.getKey())
						.lore(CoreConstants.YELLOW + "Players" + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE
								+ selectserver.getPlayersOnline(),
								CoreConstants.YELLOW + "Information" + ChatColor.DARK_GRAY + ": " + CoreConstants.GRAY
										+ server.getValue().getInformation())
						.build();
				menu.setItem(server.getValue().getPosition(), serversel);
			}
			player.openInventory(menu);
			return;
		}
		return;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSelect(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		if (e.getClickedInventory().getName() != null && e.getClickedInventory().getName()
				.equalsIgnoreCase(CoreConstants.GOLD + ChatColor.BOLD.toString() + CoreConstants.NAME)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}
			Player player = (Player) e.getWhoClicked();
			if (!e.getCurrentItem().hasItemMeta())
				return;
			if (ConfigurationService.getSELECTOR_SERVERS()
					.containsKey(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))) {
				plugin.sendToServer(player, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
			}
		}

	}

}
