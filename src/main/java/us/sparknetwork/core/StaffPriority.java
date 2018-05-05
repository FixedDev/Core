package us.sparknetwork.core;

import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import lombok.Getter;

public enum StaffPriority {

    NONE(0, "core.staffpriority.none"), LOWEST(1, "core.staffpriority.lowest"), LOW(2,
            "core.staffpriority.low"), MEDIUM(3, "core.staffpriority.medium"), NORMAL(4,
            "core.staffpriority.normal"), HiGH(5,
            "core.staffpriority.high"), HIGHEST(6, "core.staffpriority.highest");

    private static final ImmutableMap<Integer, StaffPriority> BY_ID;

    static {
        Builder<Integer, StaffPriority> builder = new ImmutableMap.Builder<>();
        for (StaffPriority sp : values()) {
            builder.put(sp.priority, sp);
        }
        BY_ID = builder.build();
    }

    @Getter
    final int priority;
    @Getter
    final String permission;

    StaffPriority(int prior, String permission) {
        this.priority = prior;
        this.permission = permission;
    }

    public static StaffPriority getByLevel(int level) {
        return BY_ID.get(level);
    }

    public static StaffPriority getByPlayer(Player player) {
        for (StaffPriority sp : values()) {
            if (sp == NONE)
                continue;
            if (player.hasPermission(sp.getPermission())) {
                return sp;
            }
        }
        return NONE;

    }

    public boolean isMoreThan(StaffPriority sp) {
        return this.priority > sp.priority;
    }

}
