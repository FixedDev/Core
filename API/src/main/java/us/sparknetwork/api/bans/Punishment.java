package us.sparknetwork.api.bans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import us.sparknetwork.api.bans.BansAPI.Type;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Entity
public class Punishment {

    @Getter
    @Id
    private UUID player;

    @Setter
    @Getter
    private String punisher = "Console";

    @Setter
    @Getter
    private String reason = "None";

    @Setter
    @Getter
    private long expire = 0l;
    @Getter
    private long punishtime = System.currentTimeMillis();

    @Setter
    @Getter
    private String ip = "##offline##";
    @Getter
    private Type type;

    @Setter
    @Getter
    private boolean active = true;

    @Setter
    @Getter
    private boolean ippunishment = false;

    @Getter
    private boolean silent = false;

    @Getter
    private String server = "";

    public Punishment(){

    }

    public Punishment(UUID player, Type type) {
        this.player = player;
        this.type = type;
    }

    public boolean isPermanent() {
        return expire == 0l;
    }

    public boolean isExpired(long currentTime) {
        return isPermanent() ? false : getExpire() <= currentTime;
    }

}
