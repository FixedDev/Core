package us.sparknetwork.basics.backend.redis.connection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import us.sparknetwork.basics.ConfigurationService;
import us.sparknetwork.basics.backend.RedisCredentials;
import us.sparknetwork.basics.events.SyncEvent;

import java.util.Arrays;

public class RedisPoolManager {

	@Getter
	public JedisPool dataSource;
	public RedisCredentials credentials;
	public int timeout = 3000;
	public JedisPubSub subscriber;
	public boolean subscribing;
	public Plugin plugin;

	public RedisPoolManager(RedisCredentials cred, Plugin plugin) {
		credentials = cred;
		this.plugin = plugin;
		setupPool();
		setupSubscriber();
		subscribeSubscriber();
	}

	public void setupPool() {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setTestOnBorrow(true);
		if (credentials.getPassword() != null && !credentials.getPassword().equals("")) {
			dataSource = new JedisPool(config, credentials.getHostname(), credentials.getPort(), timeout,
					credentials.getPassword());
		} else {
			dataSource = new JedisPool(config, credentials.getHostname(), credentials.getPort(), timeout);
		}
	}

	private void subscribeSubscriber() {
		try {
			subscribing = true;
			plugin.getProxy().getScheduler().runAsync(plugin, () -> {
				try {
					if (subscriber != null && subscriber.isSubscribed())
						subscriber.unsubscribe();
					setupSubscriber();
					dataSource.getResource().subscribe(subscriber, "SparkNetwork");
				} catch (JedisConnectionException e) {
					subscribing = false;
				} finally {
					subscriber = null;
					if (dataSource != null)
						dataSource.close();
				}
			});

		} catch (JedisConnectionException e) {
			subscribing = false;
			subscriber = null;
			if (dataSource != null)
				dataSource.close();
		}
	}

	public void setupSubscriber() {
		subscriber = new JedisPubSub() {
			@Override
			public void onSubscribe(String channel, int subscribedChannels) {
				subscribing = false;
			}

			@Override
			public void onUnsubscribe(String channel, int subscribedChannels) {
				subscribing = false;
			}

			@Override
			public void onMessage(String channel, String message) {
				String[] args = message.split("\\|");
				String from = args[0];
				String to = args[1];
				String type = args[2];
				if (from.equalsIgnoreCase(ConfigurationService.BUNGEE_NAME)) {
					return;
				}
				if(!(to.equalsIgnoreCase(ConfigurationService.BUNGEE_NAME) || to.equalsIgnoreCase("**"))){
					return;
				}
				String[] arguments = new String[args.length - 3];
				for (int i = 3; i < args.length; i++) {
					arguments[i - 3] = args[i];
				}

				SyncEvent e = new SyncEvent(from, type, arguments, dataSource);
				plugin.getProxy().getPluginManager().callEvent(e);
			}
		};
	}

	public Jedis getConnection() {
		if (dataSource == null || dataSource.isClosed()) {
			if (dataSource != null)
				dataSource.destroy();
			setupPool();
		}
		Jedis jedis = null;
		jedis = dataSource.getResource();
		return jedis;
	}

	public void destroy() {
		if (subscriber != null)
			subscriber.unsubscribe();
		if (dataSource != null && !dataSource.isClosed())
			dataSource.close();
		dataSource.destroy();

	}

}
