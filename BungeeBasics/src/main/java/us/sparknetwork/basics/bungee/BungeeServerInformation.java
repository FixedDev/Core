package us.sparknetwork.basics.bungee;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.BungeeServerInfo;
import us.sparknetwork.basics.ConfigurationService;

@Data
@EqualsAndHashCode(callSuper = true)
public class BungeeServerInformation extends BungeeServerInfo {

	boolean maintenance;
	String maintenancemsg;

	public BungeeServerInformation(String name, InetSocketAddress ip, boolean maintenance, String maintenancemsg) {
		super(name, ip, ConfigurationService.MOTD, false);
		this.maintenance = maintenance;
		this.maintenancemsg = maintenancemsg;
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		map.put("name", getName());
		map.put("address", this.getAddress().getHostString() + ":" + this.getAddress().getPort());
		map.put("maintenance", maintenance+"");
		map.put("maintenance-msg", maintenancemsg);
		return map;
	}
}
