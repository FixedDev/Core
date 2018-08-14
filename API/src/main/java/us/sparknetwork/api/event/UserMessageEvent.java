package us.sparknetwork.api.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.sparknetwork.api.user.IUser;

public class UserMessageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final IUser from;
    @Getter
    private final IUser to;
    @Getter @Setter
    private String message;

    public UserMessageEvent(IUser from, IUser to, String message) {
        super(false);
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
