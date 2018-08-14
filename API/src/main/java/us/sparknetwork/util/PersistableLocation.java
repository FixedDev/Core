package us.sparknetwork.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.mongodb.morphia.annotations.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Data @Entity
public class PersistableLocation implements ConfigurationSerializable {

    private String worldName;
    private UUID worldUUID;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public PersistableLocation(Location loc) {
        this(loc.getWorld().getName(), loc.getWorld().getUID(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),
                loc.getPitch());
    }

    public PersistableLocation(World world, double x, double y, double z, float yaw, float pitch) {
        this.worldName = world.getName();
        this.worldUUID = world.getUID();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static PersistableLocation fromString(String string) {
        String[] args = string.replace("PersistableLocation:{", "").replace("}", "").split(",");
        String worldname = args[0];
        UUID worlduuid = UUID.fromString(args[1]);
        String x = args[2];
        String y = args[3];
        String z = args[4];
        String yaw = args[5];
        String pitch = args[6];
        return new PersistableLocation(worldname, worlduuid, Double.parseDouble(x), Double.parseDouble(y),
                Double.parseDouble(z), Float.parseFloat(yaw), Float.parseFloat(pitch));
    }

    public static PersistableLocation deserialize(Map<String, Object> map) {
        String world = (String) map.get("worldName");
        UUID worlduuid = UUID.fromString((String) map.get("worldUUID"));
        Double x = (Double) map.get("x");
        Double y = (Double) map.get("y");
        Double z = (Double) map.get("z");
        Float yaw;
        Float pitch;
        yaw = map.get("yaw") instanceof String ? Float.parseFloat((String) map.get("yaw"))
                : ((Double) map.get("yaw")).floatValue();
        pitch = map.get("pitch") instanceof String ? Float.parseFloat((String) map.get("pitch"))
                : ((Double) map.get("pitch")).floatValue();
        return new PersistableLocation(world, worlduuid, x, y, z, yaw, pitch);
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z, pitch, pitch);
    }

    @Override
    public String toString() {
        return String.format("PersistableLocation:{%1$s,%2$s,%3$s,%4$s,%5$s,%6$s,%7$s}", this.worldName, this.worldUUID,
                this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("worldName", this.getWorldName());
        map.put("worldUUID", this.worldUUID.toString());
        map.put("x", this.getX());
        map.put("y", this.getY());
        map.put("z", this.getZ());
        map.put("yaw", this.getYaw());
        map.put("pitch", this.getPitch());
        return map;
    }

}
