package us.sparknetwork.core.schedulers;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import us.sparknetwork.core.CorePlugin;

public class AnnouncerRunnable extends BukkitRunnable {

    @Getter
    private CorePlugin plugin;

    public AnnouncerRunnable(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!plugin.getServerHandler().isAnnouncerEnabled()) {
            return;
        }
        if (plugin.getServerHandler().getAnnouncements().isEmpty()) {
            return;
        }
        String next = plugin.getServerHandler().getAnnouncements().get(0);

        Bukkit.broadcastMessage(next);

        Collections.rotate(plugin.getServerHandler().getAnnouncements(), -1);
    }

}
