package us.sparknetwork.api.kit.event;

import org.bukkit.event.Cancellable;

import us.sparknetwork.api.kit.IKit;

public class KitRemoveEvent extends KitEvent implements Cancellable {

    public KitRemoveEvent(IKit kit) {
        super(kit);
    }

}
