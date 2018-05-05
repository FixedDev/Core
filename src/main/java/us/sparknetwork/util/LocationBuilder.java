package us.sparknetwork.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationBuilder {

    public static String LocationToString(Location loc) {
        StringBuilder location = new StringBuilder();
        String world = loc.getWorld().getName();
        Double x = loc.getX();
        Double y = loc.getY();
        Double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        location.append(world).append(",");
        location.append(x).append(",");
        location.append(y).append(",");
        location.append(z).append(",");
        location.append(yaw).append(",");
        location.append(pitch);
        return location.toString();
    }

    public static Location StringToLocation(String s) {
        if (s == null || s.equals("unset") || s.equals("")) {
            return null;
        }

        String[] data = s.split(",");

        World world = Bukkit.getWorld(data[0]);

        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        double z = Double.parseDouble(data[3]);

        Float yaw = Float.parseFloat(data[4]);
        Float pitch = Float.parseFloat(data[5]);

        return new Location(world, x, y, z, yaw, pitch);

    }
}
