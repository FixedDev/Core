package us.sparknetwork.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import us.sparknetwork.core.CorePlugin;

public class MaxPlayersListener implements Listener {

    public String BYPASS_FULL_SERVER = "core.server.bypassfull";
    private CorePlugin plugin;

    public MaxPlayersListener(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {
        if(e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.KICK_FULL){
            e.allow();
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        int maxPlayers = CorePlugin.getPlugin().getServerHandler().getMaxPlayers();
        int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
        boolean isFull = onlinePlayers >= maxPlayers;
        if (isFull && !e.getPlayer().hasPermission(BYPASS_FULL_SERVER)) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL,
                    String.format(CorePlugin.getPlugin().getServerHandler().getServerFullMessage(),
                            Bukkit.getServer().getOnlinePlayers().size(),
                            CorePlugin.getPlugin().getServerHandler().getMaxPlayers()));
        } else {
            e.allow();
        }
    }

}
