package us.sparknetwork.core.server;

import org.bukkit.configuration.file.FileConfiguration;

import us.sparknetwork.core.CorePlugin;

public class ServerSettings {

    public static String NAME = CorePlugin.getPlugin().getConfig().getString("internal-server-name", "SparkNetwork");

    public static void setName(String name) {
        FileConfiguration configuration = CorePlugin.getPlugin().getConfig();
        configuration.set("internal-server-name", name);
        CorePlugin.getPlugin().saveConfig();
    }

    public static boolean HasName() {
        return NAME != null;
    }

}
