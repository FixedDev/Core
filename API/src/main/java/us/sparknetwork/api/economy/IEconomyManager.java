package us.sparknetwork.api.economy;

import java.util.UUID;


public interface IEconomyManager {

    /**
     * @deprecated if the user joined in the server has account obviously
     * @return true - always
     */
    boolean hasAccount(UUID playeruuid);
    /**
     * @deprecated the accounts are now the api user, can't create users manually
     * @return - a economy callback with no errors
     */
    EconomyCallback createAccount(UUID playeruuid);

    EconomyCallback setBalance(UUID playeruuid, double balance);

    EconomyCallback addBalance(UUID playeruuid, double balance);

    EconomyCallback withdrawBalance(UUID playeruuid, double balance);

    double getBalance(UUID playeruuid);

    void reloadEconomyData();

    void saveEconomyData();

    EconomyCallback resetAccount(UUID playeruuid);
}
