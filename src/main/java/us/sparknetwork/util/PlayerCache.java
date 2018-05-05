package us.sparknetwork.util;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import lombok.Getter;

public @Getter class PlayerCache {
	public final UUID playerUUID;
	public final Location location;
	public final GameMode gameMode;
	public final boolean allowFlight;
	public final boolean flying;
	public final ItemStack[] inventory;
	public final ItemStack[] armor;
	public final double health;
	public final int food;
	public final int level;
	public final float xp;
	public final int fireTicks;
	public final Collection<PotionEffect> potions;

	public PlayerCache(UUID playerUUID, Location location, GameMode gameMode, boolean allowFlight, boolean flying,
			ItemStack[] inventory, ItemStack[] armor, double health, int food, int level, float xp, int fireTicks,
			Collection<PotionEffect> potions) {
		this.playerUUID = playerUUID;
		this.location = location;
		this.gameMode = gameMode;
		this.allowFlight = allowFlight;
		this.flying = flying;
		this.inventory = inventory;
		this.armor = armor;
		this.health = health;
		this.food = food;
		this.level = level;
		this.xp = xp;
		this.fireTicks = fireTicks;
		this.potions = potions;
	}

	public PlayerCache(Player player) {
		this.playerUUID = player.getUniqueId();
		this.location = player.getLocation();
		this.gameMode = player.getGameMode();
		this.allowFlight = player.getAllowFlight();
		this.flying = player.isFlying();
		this.inventory = player.getInventory().getContents();
		this.armor = player.getInventory().getArmorContents();
		this.health = player.getHealth();
		this.food = player.getFoodLevel();
		this.level = player.getLevel();
		this.xp = player.getExp();
		this.fireTicks = player.getFireTicks();
		this.potions = player.getActivePotionEffects();
	}

	public void apply(Player player, boolean teleportToLocation) {
		PlayerUtils.wipePlayer(player);
		player.setGameMode(this.gameMode);
		player.setAllowFlight(this.allowFlight);
		player.setFlying(this.flying);
		player.getInventory().setContents(this.inventory);
		player.getInventory().setArmorContents(this.armor);
		player.setHealth(this.health);
		player.setFoodLevel(this.food);
		player.setLevel(this.level);
		player.setExp(this.xp);
		player.setFireTicks(this.fireTicks);
		player.addPotionEffects(this.potions);
		if (teleportToLocation) {
			player.teleport(location);
		}
	}

}
