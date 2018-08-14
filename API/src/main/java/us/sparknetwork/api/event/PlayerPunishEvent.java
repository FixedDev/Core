/**
 *
 */
package us.sparknetwork.api.event;


import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.sparknetwork.api.bans.Punishment;

import java.util.UUID;

public class PlayerPunishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final UUID player;
    @Getter
    private final Punishment punishment;

    public PlayerPunishEvent(UUID player, Punishment punish) {
        this.player = player;
        this.punishment = punish;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
