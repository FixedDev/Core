package us.sparknetwork.core.spawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import us.sparknetwork.util.Config;
import us.sparknetwork.util.PersistableLocation;

public class SpawnManager {

    @Getter
    static SpawnManager instance;
    public JavaPlugin plugin;
    public Config spawndata;
    @Getter
    @Setter
    PersistableLocation spawn;

    public SpawnManager(JavaPlugin pl) {
        instance = this;
        this.plugin = pl;
        reload();
    }

    public Location getLoc() {
        return spawn.toLocation();
    }

    public void teleportToSpawn(Player pl) {
        pl.teleport(spawn.toLocation(), TeleportCause.PLUGIN);
    }

    public void reload() {
        spawndata = new Config(plugin, "spawn");
        spawn = (PersistableLocation) spawndata.get("Spawn");
    }

    public void saveData() {
        spawndata.set("Spawn", spawn);
        spawndata.save();
    }
}
