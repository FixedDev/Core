package us.sparknetwork.basics.schedulers;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import us.sparknetwork.basics.BasicsPlugin;
import us.sparknetwork.basics.ConfigurationService;

public class KickMaintenanceRunnable {

	int count = 0;
	ScheduledTask task;

	public void start() {
		if (BasicsPlugin.getPlugin().getServersHandler().isGlobalMaintenance()) {
			task = BasicsPlugin.getPlugin().getProxy().getScheduler().schedule(BasicsPlugin.getPlugin(),
					new kmRunnable(), 1, 1, TimeUnit.SECONDS);
		}

	}

	public class kmRunnable implements Runnable {

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			if (count == 0) {
				ProxyServer.getInstance().broadcast(ConfigurationService.COLOR_1
						+ "The network is now in maintenance, kicking all players in 5 seconds");
				count++;
				return;
			}
			if (count == 1) {
				ProxyServer.getInstance().broadcast(ConfigurationService.COLOR_1 + "Kicking all players in 4 seconds");
				count++;
				return;

			}
			if (count == 2) {
				ProxyServer.getInstance().broadcast(ConfigurationService.COLOR_1 + "Kicking all players in 3 seconds");
				count++;
				return;

			}
			if (count == 3) {
				ProxyServer.getInstance().broadcast(ConfigurationService.COLOR_1 + "Kicking all players in 2 seconds");
				count++;
				return;

			}
			if (count == 4) {
				ProxyServer.getInstance().broadcast(ConfigurationService.COLOR_1 + "Kicking all players in 1 seconds");
				count++;
				return;

			}
			if (count == 5) {
				ProxyServer.getInstance().broadcast(ConfigurationService.COLOR_1 + "Kicking all players now.");
				ProxyServer.getInstance().getPlayers().forEach(p -> {
					if (!p.hasPermission("basics.staff")) {
						p.disconnect(ConfigurationService.COLOR_2 + "The network is now in maintenance, back later.");
					}
				});
				task.cancel();
				return;
			}
		}
	}

}
