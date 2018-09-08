package us.sparknetwork.basics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.google.common.io.ByteStreams;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import us.sparknetwork.basics.backend.RedisCredentials;
import us.sparknetwork.basics.backend.redis.connection.RedisPoolManager;
import us.sparknetwork.basics.bungee.BungeeHandler;
import us.sparknetwork.basics.bungee.RedisBungeeHandler;
import us.sparknetwork.basics.command.AlertCommand;
import us.sparknetwork.basics.command.GMaintenanceCommand;
import us.sparknetwork.basics.command.GlistCommand;
import us.sparknetwork.basics.command.ManagerCommand;
import us.sparknetwork.basics.command.MotdCommand;
import us.sparknetwork.basics.command.ServerCommand;
import us.sparknetwork.basics.schedulers.AnnouncerRunnable;

public class BasicsPlugin extends Plugin {
	@Getter
	static BasicsPlugin plugin;
	@Getter
	Configuration config;
	@Getter
	PluginManager pm;
	ScheduledTask annrun;
	@Getter
	BungeeHandler serversHandler;
	@Getter
	RedisPoolManager redis;

	@Override
	public void onLoad() {
		plugin = this;
		pm = this.getProxy().getPluginManager();

		this.loadConfig();

		if (config.getSection("redis") != null) {
			RedisCredentials cred = new RedisCredentials(config.getString("redis.host"), config.getInt("redis.port"),
					config.getString("redis.password"));
			redis = new RedisPoolManager(cred, this);
			this.serversHandler = new RedisBungeeHandler(this);
		} else {
			ProxyServer.getInstance().stop("Failed to retreive redis data");
		}
		ConfigurationService.init();
		


	}

	@Override
	public void onEnable() {

		serversHandler.loadServers();
		serversHandler.registerServers();
		
		pm.registerCommand(this, new GlistCommand());
		pm.registerCommand(this, new AlertCommand());
		pm.registerCommand(this, new ServerCommand());
		pm.registerCommand(this, new MotdCommand());
		pm.registerCommand(this, new GMaintenanceCommand());
		pm.registerCommand(this, new ManagerCommand());

		pm.registerListener(this, new Listeners());
		
		this.getProxy().getScheduler().schedule(this, () -> serversHandler.saveServers(), 0, 5, TimeUnit.SECONDS);
	}
	
	@Override
	public void onDisable() {
		serversHandler.saveServers();
	}

	public void registerSchedulers() {
		if (annrun != null) {
			annrun.cancel();
		}
		annrun = this.getProxy().getScheduler().schedule(this, new AnnouncerRunnable(),
				ConfigurationService.ANNOUNCER_DELAY, ConfigurationService.ANNOUNCER_DELAY, TimeUnit.SECONDS);
	}

	public void loadConfig() {
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadResource("config.yml"));
		} catch (IOException e) {
			this.getLogger().log(Level.SEVERE, "Exception while reading config", e);
		}
	}

	public File loadResource(String resource) {
		File folder = this.getDataFolder();
		if (!folder.exists())
			folder.mkdir();
		File resourceFile = new File(folder, resource);
		try {
			if (!resourceFile.exists()) {
				resourceFile.createNewFile();
				try (InputStream in = this.getResourceAsStream(resource);
						OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
			}
		} catch (Exception e) {
			this.getLogger().severe("Exception while writing default config" + e);
		}
		return resourceFile;
	}

}
