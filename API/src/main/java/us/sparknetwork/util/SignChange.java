package us.sparknetwork.util;

import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

public class SignChange {
    public final Sign sign;
    public final String[] newLines;
    public BukkitRunnable runnable;

    public SignChange(final Sign sign, final String[] newLines) {
        this.sign = sign;
        this.newLines = newLines;
    }
}
