package us.sparknetwork.util;

import java.util.*;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public final class PlayerUtils {

    private static Map<UUID, PlayerCache> cache = new HashMap<>();

    public static Player getPlayer(String playeruuidorname) {
        UUID u = null;
        try {
            u = UUID.fromString(playeruuidorname);
        } catch (IllegalArgumentException e) {

        }
        if (u != null) {
            return Bukkit.getPlayer(u);
        }
        return Bukkit.getPlayer(playeruuidorname);
    }

    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOfflinePlayer(String playeruuidorname) {
        UUID u = null;
        try {
            u = UUID.fromString(playeruuidorname);
        } catch (IllegalArgumentException e) {

        }
        if (u != null) {
            return Bukkit.getOfflinePlayer(u);
        }
        return Bukkit.getOfflinePlayer(playeruuidorname);
    }

    public static void wipePlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setExp(0.0F);
        player.setLevel(0);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setRemainingAir(player.getMaximumAir());
        player.setFireTicks(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setFlying(false);
        Collection<PotionEffect> potions = player.getActivePotionEffects();
        for (PotionEffect potion : potions) {
            player.removePotionEffect(potion.getType());
        }
    }

    public static void cachePlayer(Player player) {
        if (player != null) {
            cache.put(player.getUniqueId(), new PlayerCache(player));
        }
    }

    public static void restorePlayer(Player player) {
        PlayerCache pcache = cache.get(player.getUniqueId());
        if (pcache != null) {
            pcache.apply(player, false);
        }
    }

    public static PlayerCache getCache(Player player) {
        PlayerCache pcache = cache.get(player.getUniqueId());
        if (pcache != null) {
            return pcache;
        }
        return null;
    }

    public static UUID getOfflinePlayerUUID(String playerNick){
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerNick.toLowerCase(Locale.ROOT)).getBytes(Charsets.UTF_8));
    }

}
