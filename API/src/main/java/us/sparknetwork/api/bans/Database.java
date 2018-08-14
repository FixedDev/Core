package us.sparknetwork.api.bans;

import org.mongodb.morphia.Datastore;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.util.UUID;

public abstract class Database {

    protected static Database instance;

    public static Database get() {
        if (instance == null)
            throw new UnsupportedOperationException("BanManager is not in the server or isn't loaded report this!");
        return instance;
    }

    public abstract boolean isPlayerBanned(@Nullable UUID player, @Nullable String ip);

    public abstract boolean isPlayerMuted(@Nullable UUID player, @Nullable String ip);

    /**
     *
     * @deprecated TOTALLY DEPRECATED, DONT TRY TO USE, IT WILL RETURN NULL
     * AS THE PLUGIN DOESN'T USE MYSQL ANYMORE
     */
    public abstract PreparedStatement prepareStatement(String str);

    public abstract Datastore getConnection();

    public abstract Datastore getUnsafeConnection();

}
