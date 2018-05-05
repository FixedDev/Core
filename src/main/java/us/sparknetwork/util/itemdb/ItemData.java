package us.sparknetwork.util.itemdb;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ItemData implements ConfigurationSerializable {

    private Material material;
    private short itemData;

    public ItemData(Material material, short itemData) {
        this.material = material;
        this.itemData = itemData;
    }

    @Override
	public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("itemType", this.material.name());
        map.put("itemData", this.itemData);
        return map;
    }

    public Material getMaterial() {
        return this.material;
    }

    @Deprecated
    public short getItemData() {
        return this.itemData;
    }

    @Override
    public String toString() {
        return String.valueOf(this.material.name()) + "(" + String.valueOf(this.itemData) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ItemData itemData1 = (ItemData) o;
        return this.itemData == itemData1.itemData && this.material == itemData1.material;
    }

    @Override
    public int hashCode() {
        int result = (this.material != null) ? this.material.hashCode() : 0;
        result = 31 * result + this.itemData;
        return result;
    }

}