package us.sparknetwork.hub.scoreboard;

import java.util.List;

import org.bukkit.entity.Player;

public interface SidebarProvider {

    /**
     * Gets the title this provider will show for a {@link Player}.
     *
     * @return the title to be shown
     */
    public abstract String getTitle();

    /**
     * Gets the lines this provider will show for a {@link Player}.
     *
     * @param player
     *            the {@link Player} to get for
     * @return list of lines to show
     */
    public abstract List<SidebarEntry> getLines(Player player);

}