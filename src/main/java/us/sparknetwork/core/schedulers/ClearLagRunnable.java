package us.sparknetwork.core.schedulers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Tameable;
import org.bukkit.scheduler.BukkitRunnable;

import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;

public class ClearLagRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if ((entity.getType() == EntityType.DROPPED_ITEM || entity.getType() == EntityType.SKELETON
                        || entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.ARROW
                        || entity.getType() == EntityType.FALLING_BLOCK)
                        && (!(entity instanceof Tameable) || !((Tameable) entity).isTamed())) {
                    entity.remove();

                }
            }
        }
        Bukkit.broadcastMessage(CoreConstants.YELLOW + "Sucessfully cleared the items in the floor.");

    }

}
