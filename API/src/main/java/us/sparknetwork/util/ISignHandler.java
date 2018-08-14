package us.sparknetwork.util;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collection;

public interface ISignHandler {
    boolean showLines(Player player, Sign sign, String[] newLines, long ticks, boolean forceChange);

    Collection<SignChange> getSignChanges(Player player);

    void cancelTasks(@Nullable Sign sign);

    void cancelTasks(Player player, @Nullable Sign sign, boolean revertLines);
}
