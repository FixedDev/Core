package us.sparknetwork.core.schedulers;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CorePlugin;

public class AutoRestartHandler extends BukkitRunnable {

    @Getter
    private CorePlugin plugin;

    public AutoRestartHandler(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

    }

}
