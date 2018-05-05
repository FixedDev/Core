package us.sparknetwork.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.sparknetwork.api.event.ReceiveDataEvent;
import us.sparknetwork.core.CorePlugin;

public class MainCallbacks implements Listener {

    private CorePlugin plugin;

    public MainCallbacks(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void receiveDataEvent(ReceiveDataEvent e) {
        String from = e.getServer();
        String type = e.getType();
        String[] args = e.getArgs();
        String[] response = {};
        switch (type) {
            case "playerOnline":
                if (args.length < 1) {
                    break;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    response = new String[]{"true"};
                }
                break;
        }
        if (response.length > 0) {
            plugin.getServerManager().sendData(from, type + "R", response);
        }
    }
}
