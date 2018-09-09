package us.sparknetwork.hub;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import us.sparknetwork.hub.server.SelectorParser;
import us.sparknetwork.util.Config;

public class ConfigurationService {

	@Getter
	static Map<String, SelectorParser> SELECTOR_SERVERS = new HashMap<>();
	@Getter
	static int SELECTOR_SIZE = 9;
	@Getter
	static boolean BUILDER_MODE = false;

	public static void init() {
		Config cfg = new Config(HubPlugin.getPlugin(), "config");
		SELECTOR_SIZE = cfg.getInt("selector-size");
		BUILDER_MODE = cfg.getBoolean("builder-mode");
		for(String key : cfg.getConfigurationSection("servers").getKeys(false)) {
			SelectorParser server = new SelectorParser(cfg.getConfigurationSection("servers."+key).getValues(false));
			SELECTOR_SERVERS.put(key, server);
		}
	}

}
