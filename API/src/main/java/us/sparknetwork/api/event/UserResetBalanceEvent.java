package us.sparknetwork.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import us.sparknetwork.api.user.IUser;

public class UserResetBalanceEvent extends UserEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    public UserResetBalanceEvent(IUser user) {
        super(user);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
