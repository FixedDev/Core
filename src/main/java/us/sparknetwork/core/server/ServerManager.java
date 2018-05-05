package us.sparknetwork.core.server;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import us.sparknetwork.api.server.IServerManager;
import us.sparknetwork.api.server.Server;
import us.sparknetwork.core.CorePlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ServerManager implements IServerManager {

    private BukkitTask task;
    private BukkitTask cachetask;
    private CorePlugin plugin;
    private Map<String, Server> serverCache;
    @Getter
    private boolean enabled;

    public ServerManager(CorePlugin plugin) {
        this.plugin = plugin;
        enabled = plugin.getRedisManager() != null && plugin.getRedisManager().getConnection() != null;
        if (this.enabled) {
            task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::update, 10L, 10L);
            serverCache = new ConcurrentHashMap<>();
            cachetask = plugin.getServer().getScheduler().runTaskTimer(plugin, this::cacheServers, 20,
                    20);
        }

    }

    public Server getServerData(String server) {
        return this.serverCache.get(server);
    }

    private Server retrieveServerData(String server) {
        try (Jedis jedis = plugin.getRedisManager().getConnection()) {
            Map<String, String> smap = jedis.hgetAll(server);
            if (smap.isEmpty()) {
                return new Server(server, 0, 0, false, false);
            }
            return new Server(smap.get("name"), Integer.parseInt(smap.get("playersOnline")),
                    Integer.parseInt(smap.get("maxPlayers")), Boolean.getBoolean(smap.get("whitelisted")),
                    Boolean.getBoolean(smap.get("online")));
        }
    }

    private void update() {
        new BukkitRunnable() {

            @Override
            public void run() {
                try (Jedis jedis = plugin.getRedisManager().getConnection()) {
                    HashMap<String, String> updatemap = new HashMap<>();
                    updatemap.put("name", ServerSettings.NAME);
                    updatemap.put("playersOnline", plugin.getServer().getOnlinePlayers().size() + "");
                    updatemap.put("maxPlayers", plugin.getServerHandler().getMaxPlayers() + "");
                    updatemap.put("whitelisted", Bukkit.getServer().hasWhitelist() + "");
                    updatemap.put("online", "true");
                    jedis.hmset("network.server." + ServerSettings.NAME, updatemap);
                }

            }

        }.runTaskAsynchronously(plugin);

    }


    public void sendData(String server, String type, String... args) {
        if (args.length <= 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length) {
                sb.append("|");
            }
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                try (Jedis jedis = plugin.getRedisManager().getConnection()) {
                    // from| to |type|args
                    jedis.publish("SparkNetwork", String.format("%1$s|%2$s|%3$s|%4$s", ServerSettings.NAME, server, type, args));
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public int getPlayersOnline(String server) {
        return serverCache.get(server).getPlayersOnline();
    }

    public int getMaxPlayers(String server) {
        return serverCache.get(server).getMaxPlayers();
    }

    public boolean isOnline(String server) {
        return serverCache.get(server).isOnline();
    }

    private void cacheServers() {
        try (Jedis jedis = plugin.getRedisManager().getConnection()) {
            Stream<String> servers = jedis.keys("network.server.*").stream().map(s -> s.split("\\.", 3)[2]);
            servers.forEach(server -> serverCache.put(server, this.retrieveServerData(server)));
        }
    }

    public void disableServerManager() {
        try {
            if (plugin.getRedisManager().getDataSource().getResource() != null) {
                try (Jedis jedis = plugin.getRedisManager().getConnection()) {
                    HashMap<String, String> updatemap = new HashMap<>();
                    updatemap.put("name", ServerSettings.NAME);
                    updatemap.put("playersOnline", "0");
                    updatemap.put("maxPlayers", plugin.getServerHandler().getMaxPlayers() + "");
                    updatemap.put("whitelisted", Bukkit.getServer().hasWhitelist() + "");
                    updatemap.put("online", "false");
                    jedis.hmset("network.server." + ServerSettings.NAME, updatemap);
                }
            }
        } catch (JedisException e) {
            // This is a controlled exception, if the connection isn't sucessfull will be throwed but we don't want this exception
        }
    }

}
