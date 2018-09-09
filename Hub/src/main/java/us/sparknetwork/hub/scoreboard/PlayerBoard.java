package us.sparknetwork.hub.scoreboard;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import us.sparknetwork.hub.HubPlugin;

public class PlayerBoard {

	private boolean sidebarVisible = false;
	private SidebarProvider defaultProvider;
	private SidebarProvider temporaryProvider;
	private BukkitRunnable runnable;

	private final AtomicBoolean removed = new AtomicBoolean(false);

	private final BufferedObjective bufferedObjective;
	private final Scoreboard scoreboard;
	private final Player player;

	private final HubPlugin plugin;

	public PlayerBoard(HubPlugin plugin, Player player) {
		this.plugin = plugin;
		this.player = player;

		this.scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
		this.bufferedObjective = new BufferedObjective(scoreboard);

		player.setScoreboard(scoreboard);
	}

	/**
	 * Removes this {@link PlayerBoard}.
	 */
	public void remove() {
		if (!this.removed.getAndSet(true) && scoreboard != null) {
			for (Team team : scoreboard.getTeams()) {
				team.unregister();
			}

			for (Objective objective : scoreboard.getObjectives()) {
				objective.unregister();
			}
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}

	public boolean isSidebarVisible() {
		return this.sidebarVisible;
	}

	public void setSidebarVisible(boolean visible) {
		this.sidebarVisible = visible;
		this.bufferedObjective.setDisplaySlot(visible ? DisplaySlot.SIDEBAR : null);
	}

	public void setDefaultSidebar(final SidebarProvider provider, long updateInterval) {
		if (provider != this.defaultProvider) {
			this.defaultProvider = provider;
			if (this.runnable != null) {
				this.runnable.cancel();
			}

			if (provider == null) {
				this.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
				return;
			}

			(this.runnable = new BukkitRunnable() {
				public void run() {
					if (removed.get()) {
						cancel();
						return;
					}

					if (provider == defaultProvider) {
						updateObjective();
					}
				}
			}).runTaskTimerAsynchronously(plugin, updateInterval, updateInterval);
		}
	}

	public void setTemporarySidebar(final SidebarProvider provider, final long expiration) {
		if (this.removed.get()) {
			throw new IllegalStateException("Cannot update whilst board is removed");
		}

		this.temporaryProvider = provider;
		this.updateObjective();
		new BukkitRunnable() {
			public void run() {
				if (removed.get()) {
					cancel();
					return;
				}

				if (temporaryProvider == provider) {
					temporaryProvider = null;
					updateObjective();
				}
			}
		}.runTaskLaterAsynchronously(plugin, expiration);
	}

	private void updateObjective() {
		if (this.removed.get()) {
			throw new IllegalStateException("Cannot update whilst board is removed");
		}

		SidebarProvider provider = this.temporaryProvider != null ? this.temporaryProvider : this.defaultProvider;
		if (provider == null) {
			this.bufferedObjective.setVisible(false);
		} else {
			this.bufferedObjective.setTitle(provider.getTitle());
			this.bufferedObjective.setAllLines(provider.getLines(player));
			this.bufferedObjective.flip();
		}
	}

}
