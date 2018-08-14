package us.sparknetwork.api.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.sparknetwork.api.user.IUser;

public class AsyncUserLoadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final IUser user;

    public AsyncUserLoadEvent(IUser user){
        super(true);
        this.user = user;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


}
