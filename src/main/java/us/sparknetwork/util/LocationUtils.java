package us.sparknetwork.util;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LocationUtils {

    public static Location getHighestLocation(Location origin) {
        Preconditions.checkNotNull((Object) origin, "The location cannot be null");
        Location cloned = origin.clone();
        World world = cloned.getWorld();
        int x = cloned.getBlockX();
        int y = world.getMaxHeight();
        int z = cloned.getBlockZ();
        while (y > origin.getBlockY()) {
            Block block = world.getBlockAt(x, --y, z);
            if (!block.isEmpty()) {
                Location next = block.getLocation();
                next.setPitch(origin.getPitch());
                next.setYaw(origin.getYaw());
                return next;
            }
        }
        return null;
    }

    public static Location teleportSpot(Location loc, int min, int max, boolean down) {
        if (!down) {
            int k = min;

            while (k < max) {
                Material m1 = new Location(loc.getWorld(), loc.getBlockX(), k + 0.5D, loc.getBlockZ()).getBlock().getType();
                Material m2 = new Location(loc.getWorld(), loc.getBlockX(), k + 1 + 0.5D, loc.getBlockZ()).getBlock()
                        .getType();

                if (m1.equals(Material.AIR) && m2.equals(Material.AIR)) {
                    return new Location(loc.getWorld(), loc.getBlockX(), k + 0.5D, loc.getBlockZ());
                }

                ++k;
            }

            return new Location(loc.getWorld(), loc.getBlockX(),
                    loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) + 0.5D, loc.getBlockZ());
        } else {
            int k = min;

            while (k < max) {
                Material m1 = new Location(loc.getWorld(), loc.getBlockX(), k + 0.5D, loc.getBlockZ()).getBlock().getType();
                Material m2 = new Location(loc.getWorld(), loc.getBlockX(), k + 1 + 0.5D, loc.getBlockZ()).getBlock()
                        .getType();

                if (m1.equals(Material.AIR) && m2.equals(Material.AIR)) {
                    return new Location(loc.getWorld(), loc.getBlockX(), k + 0.5D, loc.getBlockZ());
                }

                ++k;
            }

            return new Location(loc.getWorld(), loc.getBlockX(),
                    loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) + 0.5D, loc.getBlockZ());
        }

    }
}
