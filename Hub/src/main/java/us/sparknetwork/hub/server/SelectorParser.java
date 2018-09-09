package us.sparknetwork.hub.server;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SelectorParser {
	
	@Getter
	int position;
	@Getter
	ItemStack item;
	@Getter
	String information;

	public SelectorParser(int position, Material item, String info) {
		this.position = position;
		this.item = new ItemStack(item);
		this.information = info;
	}

	public SelectorParser(Map<String, Object> map) {
		this.position = (int) map.get("position");
		this.item = new ItemStack(Material.valueOf((String) map.get("inventory")));
		this.information = (String) map.get("information");
	}

}
