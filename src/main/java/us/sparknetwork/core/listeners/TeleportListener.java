package us.sparknetwork.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.user.UserManager;
import us.sparknetwork.util.PersistableLocation;

public class TeleportListener implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        if (user == null) return;
        if (!user.isOnline()) return;
        user.setBackLocation(new PersistableLocation(e.getFrom()));
    }

}
