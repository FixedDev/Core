package us.sparknetwork.api.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IKit {

    void renameKit(String name);

    boolean applyTo(Player player, boolean force, boolean inform);

    String getKitPermission();

    void setItems(java.util.List<org.bukkit.inventory.ItemStack> items);

    void setArmour(java.util.List<org.bukkit.inventory.ItemStack> armour);

    void setEffects(java.util.Collection<org.bukkit.potion.PotionEffect> effects);

    void setImage(org.bukkit.inventory.ItemStack image);

    void setEnabled(boolean enabled);

    void setDelayMillis(long delayMillis);

    void setMinPlaytimeMillis(long minPlaytimeMillis);

    void setMaximumUses(int maximumUses);

    UUID getUniqueID();

    String getName();

    List<ItemStack> getItems();

    List<ItemStack> getArmour();

    Collection<PotionEffect> getEffects();

    ItemStack getImage();

    boolean isEnabled();

    long getDelayMillis();

    long getMinPlaytimeMillis();

    int getMaximumUses();
}

