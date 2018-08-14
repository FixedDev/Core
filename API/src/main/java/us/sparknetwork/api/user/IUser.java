package us.sparknetwork.api.user;

import org.bukkit.entity.Player;
import us.sparknetwork.api.economy.EconomyCallback;
import us.sparknetwork.api.kit.IKit;
import us.sparknetwork.util.PersistableLocation;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IUser extends IParticipator {

    Player getPlayer();

    void setVanished();

    void updateVanishState(Player player, Collection<? extends Player> collection, boolean vanished);

    void toggleFreeze();

    double getBalance();

    EconomyCallback setBalance(double balance);

    EconomyCallback addBalance(double balance);

    EconomyCallback withdrawBalance(double balance);

    EconomyCallback resetBalance();

    String getLastKnownIP();

    boolean tryLoggingIp(String ip);

    void toggleStaffMode();

    void setStaffMode(boolean staffMode);

    boolean isVanished();

    void setVanished(boolean vanish);

    boolean isFreezed();

    void setFreezed(boolean freeze);

    boolean isOnline();

    void setOnline(boolean online);

    boolean isStaffMode();

    List<String> getIpHistory();

    PersistableLocation getBackLocation();

    void setBackLocation(us.sparknetwork.util.PersistableLocation backLocation);

}
