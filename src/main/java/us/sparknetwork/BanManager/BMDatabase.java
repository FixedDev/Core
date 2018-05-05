package us.sparknetwork.BanManager;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.Datastore;
import us.sparknetwork.api.bans.BansAPI.Type;
import us.sparknetwork.api.bans.Database;
import us.sparknetwork.api.bans.Punishment;
import us.sparknetwork.core.CorePlugin;

import java.sql.PreparedStatement;
import java.util.UUID;

public class BMDatabase extends Database {

    private Datastore connection;

    public BMDatabase(Datastore connection) {
        this.connection = connection;
        instance = this;
    }

    @Override
    public boolean isPlayerBanned(UUID player, String ip) {
        return (ip != null && this.isPlayerIpBanned(ip)) || (player != null && this.isPlayerBanned(player));
    }

    private boolean isPlayerIpBanned(String ip) {
        if(StringUtils.isBlank(ip)){
            return false;
        }
        return connection.find(Punishment.class).disableValidation().field("type").equal(Type.BAN).field("ip").equalIgnoreCase(ip).field("active").equal(true).field("ippunishment").equal(true).get() != null;
    }

    private boolean isPlayerBanned(UUID player) {
        if(player == null){
            return false;
        }
        return connection.find(Punishment.class).disableValidation().field("type").equal(Type.BAN).field("_id").equal(player).field("active").equal(true).get() != null;
    }

    @Override
    public boolean isPlayerMuted(UUID player, String ip) {
        return (ip != null && this.isPlayerIpMuted(ip)) || (player != null && this.isPlayerMuted(player));

    }

    private boolean isPlayerMuted(UUID player) {
        if(player == null){
            return false;
        }
        return connection.find(Punishment.class).disableValidation().field("type").equal(Type.MUTE).field("_id").equal(player).field("active").equal(true).get() != null;
    }

    private boolean isPlayerIpMuted(String ip) {
        if(StringUtils.isBlank(ip)){
            return false;
        }
        return connection.find(Punishment.class).disableValidation().field("type").equal(Type.MUTE).field("ip").equalIgnoreCase(ip).field("active").equal(true).field("ippunishment").equal(true).get() != null;
    }

    @Override
    public PreparedStatement prepareStatement(String str) {
        return null;
    }

    @Override
    public Datastore getConnection() {
        CorePlugin.AsyncCatcher();
        return connection;
    }

    @Override
    public Datastore getUnsafeConnection() {

        return this.connection;
    }

    public Punishment getBan(UUID player, String ip) {
        if (ip != null) {
            Punishment punish = connection.find(Punishment.class).disableValidation().field("type").equal(Type.BAN).field("ip").equalIgnoreCase(ip).field("active").equal(true).field("ippunishment").equal(true).get();
            if (punish != null) {
                return punish;
            }
        }
        if (player != null) {
            Punishment punish = connection.find(Punishment.class).disableValidation().field("type").equal(Type.BAN).field("_id").equalIgnoreCase(player).field("active").equal(true).get();
            if (punish != null) {
                return punish;
            }
        }
        return null;
    }

    public Punishment getMute(UUID player, String ip) {
        if (ip != null) {
            Punishment punish = connection.find(Punishment.class).disableValidation().field("type").equal(Type.MUTE).field("ip").equalIgnoreCase(ip).field("active").equal(true).field("ippunishment").equal(true).get();
            if (punish != null) {
                return punish;
            }
        }
        if (player != null) {
            Punishment punish = connection.find(Punishment.class).disableValidation().field("type").equal(Type.MUTE).field("_id").equalIgnoreCase(player).field("active").equal(true).get();
            if (punish != null) {
                return punish;
            }
        }
        return null;
    }
}
