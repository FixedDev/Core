package us.sparknetwork.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CorePlugin;

public class SecurityListener implements Listener {

    private CorePlugin plugin;

    public SecurityListener(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!plugin.getServerHandler().getAllowedops().contains(event.getPlayer().getName())) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission("*")) {
                Player p = event.getPlayer();
                p.setOp(false);
                p.kickPlayer("BAD");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                        "ban %player% &cBAD".replaceAll("%player%", event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void OnPlayerJoinOPED(PlayerLoginEvent event) {
        if (!plugin.getServerHandler().getAllowedops().contains(event.getPlayer().getName())) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission("*")) {
                Player p = event.getPlayer();
                p.setOp(false);
                event.disallow(null, "BAD");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                        "ban %player% &cBAD".replaceAll("%player%", event.getPlayer().getName()));
            }
        }
    }

    public CorePlugin getPlugin() {
        return plugin;
    }

}
