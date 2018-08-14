package us.sparknetwork.api;

import us.sparknetwork.api.bans.BansAPI;
import us.sparknetwork.api.command.CommandHandler;
import us.sparknetwork.api.economy.IEconomyManager;
import us.sparknetwork.api.kit.IKitManager;
import us.sparknetwork.api.server.IServerManager;
import us.sparknetwork.api.user.IUserManager;
import us.sparknetwork.util.ISignHandler;

public abstract class API {

    protected static API api;

    public static API get() {
        if (api == null)
            throw new UnsupportedOperationException("Failed to get API, maybe the core isn't enabled");
        return api;
    }

    public abstract IUserManager getUserManager();

    public abstract IEconomyManager getEconomyManager();

    public abstract IKitManager getKitManager();

    public abstract IServerManager getServerManager();

    public abstract CommandHandler getCommandHandler();

    public abstract ISignHandler getSignHandler();

    public abstract BansAPI getBansAPI();


}
