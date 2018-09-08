package us.sparknetwork.basics.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;
import redis.clients.jedis.JedisPool;

@AllArgsConstructor
@Getter
public class SyncEvent extends Event {
	private String server;
	private String type;
	private String[] args;
	private JedisPool dataSource;

}
