package us.sparknetwork.core.server;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.sparknetwork.api.backend.Callback;
import us.sparknetwork.api.event.ReceiveDataEvent;
import us.sparknetwork.core.CorePlugin;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalPlayerAPI {

    private CorePlugin plugin;

    public GlobalPlayerAPI(CorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param playerName - The nickname of the player to be checked
     * @param callback - The callback of what happends if the player is connected
     *                 The callback timeout after 500 ms and returns false
     */
    public void isPlayerConnected(String playerName, Callback<Boolean> callback) {
        plugin.getServerManager().sendData("**", "playerOnline", "playerName");
        AtomicBoolean called = new AtomicBoolean(false);
        plugin.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void ReceiveData(ReceiveDataEvent e) {
                if (e.getType().equalsIgnoreCase("playerOnlineR")) {
                    String[] args = e.getArgs();
                    boolean response = Boolean.getBoolean(args[0]);
                    callback.call(response);
                    called.set(true);
                }
            }
        }, plugin);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            callback.call(false);
            called.set(true);
        }, TimeUnit.MILLISECONDS.toMillis(500));

    }

    public void sendMessageToPlayer(String playerName, String message) {
        plugin.getMessager().sendMessageToPlayer(playerName, message);
    }

}
