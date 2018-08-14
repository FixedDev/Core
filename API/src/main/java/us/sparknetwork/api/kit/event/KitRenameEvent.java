package us.sparknetwork.api.kit.event;

import lombok.Getter;
import us.sparknetwork.api.kit.IKit;

public class KitRenameEvent extends KitEvent {

    @Getter
    private final String newName;
    @Getter
    private final String oldName;
    public KitRenameEvent(IKit kit, String oldName, String newName) {
        super(kit);
        this.newName = newName;
        this.oldName = oldName;
    }

}
