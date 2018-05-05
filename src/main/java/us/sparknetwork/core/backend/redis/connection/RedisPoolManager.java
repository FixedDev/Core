package us.sparknetwork.core.backend.redis.connection;

import lombok.Getter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import us.sparknetwork.api.event.ReceiveDataEvent;
import us.sparknetwork.core.backend.RedisCredentials;
import us.sparknetwork.core.server.ServerSettings;
import us.sparknetwork.util.ArrayUtils;

public class RedisPoolManager {

    @Getter
    private JedisPool dataSource;
    private RedisCredentials credentials;
    private int timeout = 3000;
    private JedisPubSub subscriber;
    private boolean subscribing;
    private JavaPlugin plugin;

    public RedisPoolManager(RedisCredentials cred, JavaPlugin plugin) {
        credentials = cred;
        this.plugin = plugin;
        setupPool();
    }

    private void setupPool() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setTestOnBorrow(true);
        if (credentials.getPassword() != null && !credentials.getPassword().equals("")) {
            dataSource = new JedisPool(config, credentials.getHostname(), credentials.getPort(), timeout,
                    credentials.getPassword());
        } else {
            dataSource = new JedisPool(config, credentials.getHostname(), credentials.getPort(), timeout);
        }
        subscriber = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                parseAndExecuteEvent(channel, message);
            }
        };

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                dataSource.getResource().subscribe(subscriber, "SparkNetwork");
            } catch (JedisException e) {
                plugin.getLogger().severe("Failed to enable Redis PUB/SUB, stopping server");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            }
        });
    }

    public Jedis getConnection() {
        if (dataSource == null || dataSource.isClosed()) {
            if (dataSource != null)
                dataSource.destroy();
            setupPool();
        }
        Jedis jedis = null;
        boolean sucess = false;
        int max_tries = 3;
        int tries = 0;
        while (!sucess && max_tries >= tries) {
            try {
                jedis = dataSource.getResource();
                sucess = true;
            } catch (JedisException e) {
                if ((max_tries - tries) > 0) {
                    this.plugin.getLogger().warning("Failed to connect to redis, retrying in another " + (max_tries - tries) + " times.");
                } else if ((max_tries - tries) <= 0) {
                    this.plugin.getLogger().severe("Failed to connect to redis, max attempts reached, stopping server");
                    Bukkit.getServer().getScheduler().cancelTasks(plugin);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
                }
            } finally {
                tries++;
            }
        }
        return jedis;
    }

    private void parseAndExecuteEvent(String channel, String message) {
        String[] args = message.split("\\|");
        if (args.length < 3 && args[0].equalsIgnoreCase(ServerSettings.NAME) && (!(args[1].equalsIgnoreCase(ServerSettings.NAME) || args[1].equalsIgnoreCase("**")))) {
            return;
        }
        String[] arguments = ArrayUtils.subArrayFromIndex(args, 3);

        ReceiveDataEvent e = new ReceiveDataEvent(args[0], args[2], arguments, dataSource);
        Bukkit.getServer().getPluginManager().callEvent(e);
    }

    public void destroy() {
        if (subscriber != null && subscriber.isSubscribed()) {
            subscriber.unsubscribe();
        }
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            dataSource.destroy();
        }
    }

}
