package us.sparknetwork.core.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {


    @EventHandler
    public void onColouredSignPlace(BlockPlaceEvent e) {
        Block bl = e.getBlock();
        Player pl = e.getPlayer();
        if (bl.getState() instanceof Sign) {
            Sign si = (Sign) bl.getState();
            String[] lines = si.getLines();
            if (pl.hasPermission("core.sign.color")) {
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i].replace("&", "§");
                    lines[i] = line;
                }
            }
        }
    }

    @EventHandler
    public void onColouredSignChange(SignChangeEvent e) {
        Player pl = e.getPlayer();
        String[] lines = e.getLines();
        if (pl.hasPermission("core.sign.color")) {
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].replace("&", "§");
                lines[i] = line;
            }
        }
    }

    @EventHandler
    public void onSignPlace(BlockPlaceEvent e) {
        Block bl = e.getBlock();
        Player pl = e.getPlayer();
        if (bl.getState() instanceof Sign) {
            Sign si = (Sign) bl.getState();
            String[] lines = si.getLines();
            if (lines[1].contains("Kit")) {
                if (!pl.hasPermission("core.sign.kit")) {
                    lines[1] = "No Permissions";
                }
            }

        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player pl = e.getPlayer();
        String[] lines = e.getLines();
        if (lines[1].contains("Kit")) {
            if (!pl.hasPermission("core.sign.kit")) {
                lines[1] = "No Permissions";
            }
        }
    }

}
