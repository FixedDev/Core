package us.sparknetwork.basics;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.sparknetwork.basics.bungee.BungeeServerInformation;
import us.sparknetwork.basics.events.SyncEvent;

public class Listeners implements Listener {

	@EventHandler
	public void onProxyPing(ProxyPingEvent event) {
		event.getResponse().setDescription(ChatColor.translateAlternateColorCodes('&', ConfigurationService.MOTD));
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onServerSwitch(ServerConnectEvent e) {
		if (BasicsPlugin.getPlugin().getServersHandler().servers.containsKey(e.getTarget().getName())) {
			BungeeServerInformation info = BasicsPlugin.getPlugin().getServersHandler().servers
					.get(e.getTarget().getName());
			if (info != null && (info.isMaintenance() && !e.getPlayer().hasPermission("basics.maintenance.bypass"))) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + info.getMaintenancemsg());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PostLoginEvent e) {
		if (BasicsPlugin.getPlugin().getServersHandler().isGlobalMaintenance()) {
			if (!e.getPlayer().hasPermission("basics.maintenance.bypass.global")) {
				e.getPlayer().disconnect(ChatColor.RED + ConfigurationService.MAINTENANCE_MESSAGE);
			}
		}
	}

	@EventHandler
	public void onDataReceive(SyncEvent e){
		// PlayerOnline|{PLAYER}
		if(e.getType().equalsIgnoreCase("PlayerOnline") && e.getArgs().length == 1){
			String playerName = e.getArgs()[0];
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
			if(player != null){
				BasicsPlugin.getPlugin().getServersHandler().sendData(e.getServer(), "PlayerOnline", "true");
			}
		} else if (e.getType().equalsIgnoreCase("PlayerServer") && e.getArgs().length == 1){
			String playerName = e.getArgs()[0];
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
			if(player != null){
				BasicsPlugin.getPlugin().getServersHandler().sendData(e.getServer(), "PlayerServer", player.getServer().getInfo().getName());
			}
		}
	}
}
