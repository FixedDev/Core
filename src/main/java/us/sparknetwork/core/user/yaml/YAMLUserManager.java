package us.sparknetwork.core.user.yaml;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.api.user.IParticipator;
import us.sparknetwork.core.user.User;
import us.sparknetwork.core.user.UserManager;

public class YAMLUserManager extends UserManager {

	private File userdir;

	public YAMLUserManager(CorePlugin plugin) {
		super(plugin);
		userdir = new File(plugin.getDataFolder(), "users");
		userdir.mkdir();
	}

	private File getUserFile(UUID user) {
		return new File(userdir, user.toString() + ".yml");
	}

	@Override
	public User loadUser(UUID player) {
		if(player == null) {
			return null;
		}
		File userfile = this.getUserFile(player);
		if (userfile.exists()) {
			YamlConfiguration config = new YamlConfiguration();
			try {
				config.load(userfile);
			} catch (IOException | InvalidConfigurationException e) {
				plugin.getLogger().severe("Failed to load user " + player.toString());
			}
			if (config.contains("user")) {
				this.loadedusers.put(player, (IParticipator) config.get("user"));
			}
		}
		return null;
	}

	@Override
	public void saveUser(IUser player) {
		if(player.getUniqueId() == null) {
			return;
		}
		File userfile = this.getUserFile(player.getUniqueId());
		YamlConfiguration config = new YamlConfiguration();
		config.set("user", player);
		try {
			config.save(userfile);
		} catch (IOException e) {
			plugin.getLogger().severe("Failed to save user " + player.getUniqueId());
		}

	}

}
