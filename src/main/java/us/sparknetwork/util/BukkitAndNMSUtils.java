package us.sparknetwork.util;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitAndNMSUtils {

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        String version = "net.minecraft.server."
                + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        Class<?> clazz = Class.forName(version + "." + name);
        return clazz;
    }

    public static Class<?> getCraftClass(String name) throws ClassNotFoundException {
        String version = "org.bukkit.craftbukkit."
                + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        Class<?> clazz = Class.forName(version + "." + name);
        return clazz;
    }

    public static Field getCraftPlayerField(Player player, String fieldname)
            throws NoSuchMethodException, SecurityException, NoSuchFieldException {
        Field r = player.getClass().getField(fieldname);
        r.setAccessible(true);
        return r;
    }


}
