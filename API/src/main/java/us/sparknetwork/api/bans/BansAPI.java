package us.sparknetwork.api.bans;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

public interface BansAPI {

    public List<Punishment> getLastBans(Long lasttime);

    public Long getActualTime();

    public void unpunishPlayer(UUID player, Type type);

    public void insertPermanentBan(UUID punished, String punisher, String reason, boolean silent);

    public void insertPermanentMute(UUID punished, String punisher, String reason, boolean silent);

    public void insertTemporalMute(UUID punished, long expire, String punisher, String reaso, boolean silentn);

    public void insertIpMute(UUID punished, long expire, String punisher, String reason, boolean silent);

    public void insertKick(UUID punished, String punisher, String reason, boolean silent);

    public void insertIpban(UUID punished, String ip, String punisher, String reason, boolean silent);

    public void insertTemporalBan(UUID punished, long expire, String punisher, String reason, boolean silent);

    public Database getDatabase();

    public enum Type {

        BAN(null,"banned"), MUTE(null,"muted"), KICK(null,"kicked"), IPBAN(BAN,"ip-banned"), IPMUTE(MUTE,"ip-muted");
        @Getter
        Type base;
        @Getter
        String displayName;

        Type(Type reference, String displayName) {
            base = reference;
            this.displayName = displayName;
        }

        public boolean hasBase() {
            return base != null;
        }
    }
}