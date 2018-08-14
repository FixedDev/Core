package us.sparknetwork.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import redis.clients.jedis.JedisPool;

@AllArgsConstructor
@Getter
public class ReceiveDataEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String server;
    private String type;
    private String[] args;
    private JedisPool dataSource;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
	public HandlerList getHandlers() {
        return handlers;
    }

}
