package us.sparknetwork.core.user.mongo;

import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.user.User;
import us.sparknetwork.core.user.UserManager;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.user.User;
import us.sparknetwork.core.user.UserManager;

import java.util.UUID;

public class MongoUserManager extends UserManager {

    private CorePlugin plugin;

    public MongoUserManager(CorePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void saveUser(IUser user) {
        if (user instanceof User) {
            User us = (User) user;
            plugin.getDatastore().save(us);
        }
    }

    @Override
    public IUser loadUser(UUID player) {
        return plugin.getDatastore().find(User.class).field("_id").equal(player).get();
    }

    public void fetch(UUID player) {
        if (this.loadedusers.containsKey(player)) {
            IUser newuser = this.loadUser(player);
            IUser old = (IUser) this.loadedusers.put(player, newuser);
            if(old != null){
                old.merge(newuser);
            }
        }
    }
}
