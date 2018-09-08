package us.sparknetwork.basics.bungee;

import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import redis.clients.jedis.Jedis;
import us.sparknetwork.basics.BasicsPlugin;
import us.sparknetwork.basics.ConfigurationService;

public class RedisBungeeHandler extends BungeeHandler {

	public RedisBungeeHandler(BasicsPlugin plugin) {
		super(plugin, new HashMap<>());
		this.GlobalMaintenance = plugin.getConfig().getBoolean("network-maintenance", false);
	}

	public void loadServers() {
		try (Jedis jedis = plugin.getRedis().getConnection()) {
			jedis.keys("bungee.server.*").forEach(servername -> {
				String[] splitname = servername.split("\\.");
				String name = splitname[2];
				Map<String, String> smap = jedis.hgetAll(servername);
				BungeeServerInformation server = new BungeeServerInformation(smap.get("name"),
						Util.getAddr(smap.get("address")), Boolean.parseBoolean(smap.get("maintenance")),
						smap.get("maintenance-msg"));
				servers.put(name, server);
			});
		}
	}

	public BungeeServerInformation addServer(String name, String address) {
		BungeeServerInformation info = new BungeeServerInformation(name, Util.getAddr(address), false,
				"This server is currently in maintenance, join later.");
		plugin.getProxy().getServers().put(info.getName(), info);
		return servers.put(info.getName(), info);
	}

	public boolean serverExists(String name) {
		for (BungeeServerInformation server : servers.values()) {
			if (server.getName().equals(name)) {
				return server.getName().equals(name);
			}
		}
		return false;
	}

	public void saveServers() {
		try (Jedis jedis = plugin.getRedis().getConnection()) {
			jedis.keys("bungee.server.*").forEach(servername -> {
				String[] splitname = servername.split("\\.");
				String name = splitname[2];
				if (!this.serverExists(name)) {
					jedis.del(servername);
				}
			});
			servers.values().forEach(sv -> {
				jedis.hmset("bungee.server." + sv.getName(), sv.toMap());
			});
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void removeServer(String name) {
		Map<String, BungeeServerInformation> map = BasicsPlugin.getPlugin().getServersHandler().servers;
		if (!map.containsKey(name)) {
			return;
		}
		BungeeServerInformation info = map.get(name);
		info.getPlayers().forEach(p -> {
			ServerInfo sinfo = ProxyServer.getInstance().getServerInfo(ProxyServer.getInstance()
					.getConfigurationAdapter().getListeners().iterator().next().getFallbackServer());
			p.connect(sinfo);
			p.sendMessage(ChatColor.RED
					+ "You we're moved to the fallback server due the server you were on was removed from the network.");
		});
		map.remove(name);
	}

	public void sendData(String server, String type, String... args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			if (i < args.length) {
				sb.append("|");
			}
		}
		this.plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
			@Override
			public void run() {
				try (Jedis jedis = plugin.getRedis().getConnection()) {
					// from| to |type|args
					jedis.publish("SparkNetwork", String.format("%1$s|%2$s|%3$s|%4$s", ConfigurationService.BUNGEE_NAME, server, type, args));
				}
			}
		});
	}
}
