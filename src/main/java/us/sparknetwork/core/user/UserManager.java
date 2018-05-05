package us.sparknetwork.core.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.sparknetwork.api.user.IParticipator;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.api.user.IUserManager;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.api.event.AsyncUserLoadEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class UserManager implements IUserManager {

    protected static UserManager instance;
    public CorePlugin plugin;
    protected Map<UUID, IParticipator> loadedusers;

    public UserManager(CorePlugin plugin) {
        this.plugin = plugin;
        instance = this;
        this.loadedusers = new ConcurrentHashMap<>();
        new ConsoleUser();
    }

    public static UserManager getInstance() {
        return instance;
    }

    public Map<UUID, IParticipator> getUserMap() {
        return this.loadedusers;
    }

    public IUser getUser(UUID user) {
        IParticipator participator;
        return (participator = this.getParticipator(user)) instanceof User ? (User) participator : null;
    }

    public void handleJoin(Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            User user = (User) loadUser(player.getUniqueId());
            if (user == null) {
                user = new User(player.getUniqueId(), player.getName());
                saveUser(user);
            }
            IUser old = (IUser) this.loadedusers.put(player.getUniqueId(), user);
            if (old != null) {
                old.merge(user);
            }
            Bukkit.getServer().getPluginManager().callEvent(new AsyncUserLoadEvent(user));
            user.setOnline(true);
        });
    }

    public void handleQuit(Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            IUser user = this.getUser(player.getUniqueId());
            user.setOnline(false);
            saveUser(user);
        });
    }

    public IParticipator getParticipator(UUID uuid) {
        IParticipator participator = loadedusers.get(uuid);
        if (participator == null) {
            participator = this.loadUser(uuid);
        }
        return participator;
    }

}
