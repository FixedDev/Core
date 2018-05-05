package us.sparknetwork.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.sparknetwork.api.API;
import us.sparknetwork.api.bans.BansAPI;
import us.sparknetwork.api.command.CommandHandler;
import us.sparknetwork.api.economy.IEconomyManager;
import us.sparknetwork.api.kit.IKitManager;
import us.sparknetwork.api.server.IServerManager;
import us.sparknetwork.api.user.IUserManager;
import us.sparknetwork.util.ISignHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FApi extends API {

    private CorePlugin plugin;

    public FApi(CorePlugin plugin) {
        this.plugin = plugin;
        this.api = this;
    }

    public IUserManager getUserManager() {
        return plugin.getUserManager();
    }

    public IEconomyManager getEconomyManager() {
        return plugin.getEconomyManager();
    }

    public IKitManager getKitManager() {
        try {
            if (Bukkit.getServer().getPluginManager().isPluginEnabled("Kits")) {
                JavaPlugin plugin = (JavaPlugin) Bukkit.getServer().getPluginManager().getPlugin("Kits");
                Method field = plugin.getClass().getMethod("getKitManager");
                IKitManager manager = (IKitManager) field.invoke(plugin);
                return manager;
            }
        } catch (Exception e) {
            CorePlugin.getUnsafe().throwException(e);
        }
        CorePlugin.getUnsafe().throwException(new RuntimeException("Failed to get KitsAPI, maybe the plugin isn't in the server"));
        return null;
    }

    public IServerManager getServerManager() {
        return plugin.getServerManager();
    }

    public CommandHandler getCommandHandler() {
        return plugin.getCmdhandler();
    }

    public BansAPI getBansAPI() {
        if (plugin.getManager() == null) {
            CorePlugin.getUnsafe().throwException(new RuntimeException("Failed to get BansAPI, maybe the server isn't connected with mongo"));
        }
        return plugin.getManager();
    }

    @Override
    public ISignHandler getSignHandler() {
        return CorePlugin.getPlugin().getSignHandler();
    }
}
