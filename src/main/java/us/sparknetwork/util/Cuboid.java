package us.sparknetwork.util;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public class Cuboid implements ConfigurationSerializable {

    World world;
    int xmax;
    int xmin;
    int ymax;
    int ymin;
    int zmax;
    int zmin;

    public Cuboid(int xmax, int xmin, int ymax, int ymin, int zmax, int zmin, World world) {
        this.xmax = xmax;
        this.xmin = xmin;
        this.ymax = ymax;
        this.ymin = ymin;
        this.zmax = zmax;
        this.zmin = zmin;
        this.world = world;
    }

    public Cuboid(Cuboid c) {
        this.xmax = c.getXmax();
        this.xmin = c.getXmin();
        this.ymax = c.getYmax();
        this.ymin = c.getYmin();
        this.zmax = c.getZmax();
        this.zmin = c.getZmin();
        this.world = c.getWorld();
    }

    public Cuboid(Location loc1, Location loc2) {
        this.xmax = loc1.getBlockX();
        this.xmin = loc2.getBlockX();
        this.ymax = loc1.getBlockY();
        this.ymin = loc2.getBlockY();
        this.zmax = loc1.getBlockZ();
        this.zmin = loc2.getBlockZ();
        this.world = loc1.getWorld();
    }

    public Cuboid(Map<String, Object> map) {
        if (map.containsKey("XMAX") && map.containsKey("XMIN") && map.containsKey("YMAX") && map.containsKey("YMIN")
                && map.containsKey("ZMAX") && map.containsKey("ZMIN") && map.containsKey("WORLD")) {
            xmax = (int) map.get("XMAX");
            xmin = (int) map.get("XMIN");
            zmax = (int) map.get("ZMAX");
            zmin = (int) map.get("ZMIN");
            ymax = (int) map.get("YMAX");
            ymin = (int) map.get("YMIN");
            world = (World) map.get("WORLD");
        }
    }

    public World getWorld() {
        return world;
    }

    public int getXmax() {
        return xmax;
    }

    public int getXmin() {
        return xmin;
    }

    public int getYmax() {
        return ymax;
    }

    public int getYmin() {
        return ymin;
    }

    public int getZmax() {
        return zmax;
    }

    public int getZmin() {
        return zmin;
    }

    public boolean isLocInside(Location l) {
        int x = l.getBlockX();
        int z = l.getBlockZ();
        int y = l.getBlockY();
        if (xmin <= x && xmax >= x && ymax >= y && ymin <= y && zmax >= z && zmin <= z) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPlayerInside(Player p) {
        int x = p.getLocation().getBlockX();
        int z = p.getLocation().getBlockZ();
        int y = p.getLocation().getBlockY();
        if (xmin <= x && xmax >= x && ymax >= y && ymin <= y && zmax >= z && zmin <= z) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBlockInside(Block b) {
        int x = b.getLocation().getBlockX();
        int z = b.getLocation().getBlockZ();
        int y = b.getLocation().getBlockY();
        if (xmin <= x && xmax >= x && ymax >= y && ymin <= y && zmax >= z && zmin <= z) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }

}
