package us.sparknetwork.core.economy;

import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import us.sparknetwork.api.economy.EconomyCallback;
import us.sparknetwork.api.economy.EconomyCallback.EconTransaction;
import us.sparknetwork.api.economy.IEconomyManager;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.user.UserManager;
import us.sparknetwork.core.user.UserManager;

public class EconomyManager implements IEconomyManager {

    public static String ECONOMY_SYMBOL = "$";
    private static EconomyManager instance;
    public final JavaPlugin plugin;

    public EconomyManager(JavaPlugin pl) {
        instance = this;
        this.plugin = pl;
    }

    public static EconomyManager getInstance() {
        return instance;
    }

    @Override
	public boolean hasAccount(UUID playeruuid) {
        return true;
    }

    @Override
    public EconomyCallback createAccount(UUID playeruuid) {
        return new EconomyCallback(EconTransaction.SUCCESSFULLY, 0D, 0D, EconomyCallback.ErrorReason.NOERRORS);
    }

    @Override
    public EconomyCallback setBalance(UUID playeruuid, double balance) {
        IUser user = UserManager.getInstance().getUser(playeruuid);
        if(user == null){
            return EconomyCallback.getUDECallback();
        }
        return user.setBalance(balance);
    }

    @Override
    public EconomyCallback addBalance(UUID playeruuid, double balance) {
        IUser user = UserManager.getInstance().getUser(playeruuid);
        if(user == null){
            return EconomyCallback.getUDECallback();
        }
        return user.addBalance(balance);
    }

    @Override
    public EconomyCallback withdrawBalance(UUID playeruuid, double balance) {
        IUser user = UserManager.getInstance().getUser(playeruuid);
        if(user == null){
            return EconomyCallback.getUDECallback();
        }
        return user.withdrawBalance(balance);
    }

    @Override
    public double getBalance(UUID playeruuid) {
        IUser user = UserManager.getInstance().getUser(playeruuid);
        if(user == null){
            return 0;
        }
        return user.getBalance();
    }

    @Override
    public EconomyCallback resetAccount(UUID playeruuid) {
        IUser user = UserManager.getInstance().getUser(playeruuid);
        if(user == null){
            return EconomyCallback.getUDECallback();
        }
        return user.resetBalance();
    }

    @Override
    public void reloadEconomyData() {
        return;
    }

    @Override
    public void saveEconomyData() {
        return;
    }

}
