package us.sparknetwork.api.kit.event;

import org.bukkit.event.Cancellable;

import us.sparknetwork.api.kit.IKit;

public class KitCreateEvent extends KitEvent implements Cancellable {

    public KitCreateEvent(IKit kit) {
        super(kit);
    }

}
