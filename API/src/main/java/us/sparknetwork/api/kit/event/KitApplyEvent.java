package us.sparknetwork.api.kit.event;

import org.bukkit.entity.Player;

import us.sparknetwork.api.kit.IKit;


public class KitApplyEvent extends KitPlayerEvent {

    public boolean force = false;

    public KitApplyEvent(Player player, IKit kit, boolean force) {
        super(player, kit);
        this.force = force;
    }


}
