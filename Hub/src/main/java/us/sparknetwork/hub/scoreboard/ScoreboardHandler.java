package us.sparknetwork.hub.scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import us.sparknetwork.hub.HubPlugin;
import us.sparknetwork.hub.scoreboard.provider.HubProvider;

public class ScoreboardHandler implements Listener {

	@Getter
	private Map<UUID, PlayerBoard> playerBoards = new HashMap<>();
	private SidebarProvider SidebarProvider;

	public ScoreboardHandler() {
		Bukkit.getPluginManager().registerEvents(this, HubPlugin.getPlugin());

		Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
		this.SidebarProvider = new HubProvider();
		for (Player player : players) {
			this.applyBoard(player);
		}
	}

	public PlayerBoard getPlayerBoard(UUID uuid) {
		return this.playerBoards.get(uuid);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		new BukkitRunnable() {

			@Override
			public void run() {
				applyBoard(e.getPlayer());

			}

		}.runTaskLater(HubPlugin.getPlugin(), 2l);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.playerBoards.remove(event.getPlayer().getUniqueId()).remove();
	}

	public PlayerBoard applyBoard(Player player) {
		PlayerBoard board = new PlayerBoard(HubPlugin.getPlugin(), player);
		PlayerBoard previous = this.playerBoards.put(player.getUniqueId(), board);

		if (previous != null && previous != board) {
			previous.remove();
		}

		board.setSidebarVisible(true);
		board.setDefaultSidebar(this.SidebarProvider, 2l);

		return board;
	}

}
