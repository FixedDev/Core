package us.sparknetwork.basics.bungee;

import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import us.sparknetwork.basics.BasicsPlugin;

public abstract class BungeeHandler {

	public BungeeHandler(BasicsPlugin plugin, Map<String, BungeeServerInformation> servers) {
		this.plugin = plugin;
		this.servers = servers;
	}

	BasicsPlugin plugin;
	public Map<String, BungeeServerInformation> servers;
	@Getter
	@Setter
	boolean GlobalMaintenance;

	public abstract void loadServers();

	public abstract void saveServers();

	public abstract BungeeServerInformation addServer(String name, String address);
	public abstract void removeServer(String name);
	
	public void registerServers() {
		plugin.getProxy().getServers().putAll(servers);
	}

	public abstract void sendData(String server, String type, String... args);

	public void unregisterServers() {
		for (Entry<String, BungeeServerInformation> ent : servers.entrySet()) {
			plugin.getProxy().getServers().remove(ent.getKey());
		}
		this.servers.clear();
	}
}
