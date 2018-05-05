package us.sparknetwork.core.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.net.InetAddresses;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;
import us.sparknetwork.api.economy.EconomyCallback;
import us.sparknetwork.api.economy.IEconomyManager;
import us.sparknetwork.api.event.*;
import us.sparknetwork.api.user.IParticipator;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.StaffPriority;
import us.sparknetwork.core.economy.EconomyManager;
import us.sparknetwork.core.handlers.FreezeManager;
import us.sparknetwork.core.handlers.StaffModeManager;
import us.sparknetwork.util.PersistableLocation;

import java.util.*;
import java.util.stream.Collectors;

@Entity("ServerUser")
public class User extends Participator implements IUser {

    @Getter
    @Transient
    private IEconomyManager em = EconomyManager.getInstance();
    @Getter
    private boolean vanished;
    @Getter
    private boolean freezed;
    @Getter
    @Setter
    private boolean online;
    @Getter
    private boolean staffMode;
    @Getter
    @Embedded
    private List<String> ipHistory;
    @Getter
    private double balance;
    @Getter
    @Setter
    @Transient
    private PersistableLocation backLocation;

    private User() {
        super();
    }

    public User(UUID uniqueId, String nick) {
        super(uniqueId, nick);
        this.balance = em.getBalance(uniqueId);
        this.vanished = false;
        this.freezed = false;
        this.staffMode = false;
        this.online = Bukkit.getOfflinePlayer(uniqueId).isOnline();
        this.ipHistory = new ArrayList<>();
        this.balance = 0D;
    }

    public User(User user) {
        super(user);
        this.balance = em.getBalance(uniqueId);
        this.vanished = user.isVanished();
        this.freezed = user.isFreezed();
        this.staffMode = user.staffChat;
        this.online = user.isOnline();
        this.ipHistory = this.getIpHistory();
        this.backLocation = this.getBackLocation();
        this.balance = user.getBalance();
    }

    @SuppressWarnings("unchecked")
    public User(Map<String, Object> map) {
        super(map);
        if (map.containsKey("vanished") && map.containsKey("freezed") && map.containsKey("staff-mode") && map.containsKey("back-location") && map.containsKey("ip-history")) {
            this.vanished = (boolean) map.get("vanished");
            this.freezed = (boolean) map.get("freezed");
            this.staffMode = (boolean) map.get("staff-mode");
            this.balance = Double.parseDouble((String) map.get("balance"));
            this.ipHistory = (List<String>) map.get("ip-history");
            this.backLocation = (PersistableLocation) map.get("back-location");
        }
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.getUniqueId());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("vanished", this.vanished);
        map.put("freezed", this.freezed);
        map.put("staff-mode", staffMode);
        map.put("ip-history", this.ipHistory);
        map.put("balance", ((Double) this.balance).toString());
        map.put("back-location", this.backLocation);
        return map;
    }

    @Override
    public void setVanished(boolean vanish) {
        if (!isOnline()) {
            this.vanished = vanish;
            return;
        }
        Player player = CorePlugin.getPlugin().getServer().getPlayer(uniqueId);
        UserVanishEvent event = new UserVanishEvent(player, vanish);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        this.updateVanishState(player, Bukkit.getOnlinePlayers(), vanish);
        this.vanished = vanish;
        player.setCanPickupItems(!vanish);
    }

    @Override
    public void setVanished() {
        setVanished(!isVanished());
    }

    @Override
    public void updateVanishState(Player player, Collection<? extends Player> collection, boolean vanished) {
        player.spigot().setCollidesWithEntities(!vanished);
        for (Player viewer : collection) {
            if (player.equals(viewer))
                continue;
            if (vanished) {
                if (StaffPriority.getByPlayer(player).getPriority() > StaffPriority.getByPlayer(viewer).getPriority()) {
                    viewer.hidePlayer(player);
                }
                continue;
            }
            viewer.showPlayer(player);
        }

    }

    public void setFreezed(boolean freezed){
        this.freezed = freezed;
        if(!freezed) return;
        List<String> message = CorePlugin.getPlugin().getServerHandler().getFreezeMessage().stream().map(str -> {
            str = ChatColor.translateAlternateColorCodes('&', str);
            String finalMessage = String.format(str, CoreConstants.TEAMSPEAK).replace("%YELLOW%", CoreConstants.YELLOW.toString()).replace("%GRAY%", CoreConstants.GRAY.toString()).replace("%GOLD%", CoreConstants.GOLD.toString());
            return finalMessage;
        }).collect(Collectors.toList());
        getPlayer().sendMessage(message.toArray(new String[message.size()]));
    }

    @Override
    public void toggleFreeze() {
        this.setFreezed(!isFreezed());
    }

    @Override
    public EconomyCallback setBalance(double balance) {

        if (balance > 99999999999L) {
            return EconomyCallback.getIBCallback(balance);
        }
        UserSetBalanceEvent ev;
        Bukkit.getPluginManager().callEvent((ev = new UserSetBalanceEvent(this, this.getBalance(), balance)));
        if (ev.isCancelled()) {
            return EconomyCallback.getECCallback();
        }
        EconomyCallback e = new EconomyCallback(EconomyCallback.EconTransaction.SUCCESSFULLY, this.getBalance(), balance, EconomyCallback.ErrorReason.NOERRORS);
        this.balance = balance;
        return e;
    }

    @Override
    public EconomyCallback addBalance(double balance) {
        if (balance < 0) {
            return EconomyCallback.getIBCallback(balance);
        }
        if (this.getBalance() + balance > 99999999999L) {
            return EconomyCallback.getIBCallback(balance);
        }
        UserAddBalanceEvent ev;
        Bukkit.getPluginManager().callEvent((ev = new UserAddBalanceEvent(this, this.getBalance(), balance)));
        if (ev.isCancelled()) {
            return EconomyCallback.getECCallback();
        }
        EconomyCallback e = new EconomyCallback(EconomyCallback.EconTransaction.SUCCESSFULLY, this.getBalance(), this.balance, EconomyCallback.ErrorReason.NOERRORS);
        this.balance += balance;
        return e;
    }

    @Override
    public EconomyCallback withdrawBalance(double balance) {
        if (balance < 0) {
            return EconomyCallback.getIBCallback(balance);
        }
        if (this.getBalance() + balance > 99999999999L) {
            return EconomyCallback.getIBCallback(balance);
        }
        UserBalanceWithdrawEvent ev;
        Bukkit.getPluginManager().callEvent((ev = new UserBalanceWithdrawEvent(this, this.getBalance(), balance)));
        if (ev.isCancelled()) {
            return EconomyCallback.getECCallback();
        }
        EconomyCallback e = new EconomyCallback(EconomyCallback.EconTransaction.SUCCESSFULLY, this.getBalance(), this.balance, EconomyCallback.ErrorReason.NOERRORS);
        this.balance -= balance;
        return e;
    }

    @Override
    public EconomyCallback resetBalance() {
        UserResetBalanceEvent ev;
        Bukkit.getPluginManager().callEvent((ev = new UserResetBalanceEvent(this)));
        if (ev.isCancelled()) {
            return EconomyCallback.getECCallback();
        }
        EconomyCallback e = new EconomyCallback(EconomyCallback.EconTransaction.SUCCESSFULLY, 0, 0, EconomyCallback.ErrorReason.NOERRORS);
        this.balance = 0D;
        return e;
    }

    /*
     * @Override public void setNick(String nick) { }
     */

    @Override
    public String getLastKnownIP() {
        return Iterables.getLast(this.getIpHistory());
    }

    @Override
    public boolean tryLoggingIp(String ip) {
        Preconditions.checkNotNull(ip, "Cannot log null ipaddress");
        if(this.getIpHistory() == null){
            this.ipHistory = new ArrayList<>();
        }
        if (!this.getIpHistory().contains(ip)) {
            Preconditions.checkArgument(InetAddresses.isInetAddress(ip), "Cannot log a invalid address");
            ipHistory.add(ip);
            return true;
        }
        return false;
    }

    @Override
    public void toggleStaffMode() {
        this.setStaffMode(!this.isStaffMode());
    }

    @Override
    public void setStaffMode(boolean staffMode) {
        if (!this.isOnline()) {
            this.staffMode = false;
        }
        if (staffMode) {
            StaffModeManager.getInstance().setStaffMode(Bukkit.getPlayer(this.getUniqueId()), true);
            this.staffMode = true;
            return;
        }
        StaffModeManager.getInstance().setStaffMode(Bukkit.getPlayer(this.getUniqueId()), false);
        this.staffMode = false;
    }


    public void merge(IParticipator participator) {
        if (participator instanceof IUser) {
            IUser user = (IUser) participator;
            this.vanished = user.isVanished();
            this.freezed = user.isFreezed();
            this.staffMode = user.isStaffMode();
            this.ipHistory = user.getIpHistory();
        }
    }

    public void update() {
        CorePlugin plugin = CorePlugin.getPlugin();
        if (plugin != null && plugin.getDatastore() != null) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                Datastore datastore = plugin.getDatastore();
                datastore.save(this);
            });
        }

    }
}
