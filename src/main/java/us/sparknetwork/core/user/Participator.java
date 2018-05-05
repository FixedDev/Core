package us.sparknetwork.core.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import us.sparknetwork.api.user.IParticipator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Entity("ServerUser")
public class Participator implements IParticipator {

    @Id
    @Getter
    protected UUID uniqueId;
    @Getter
    @Setter
    protected boolean staffChatVisible;
    @Getter
    @Setter
    protected boolean globalChatVisible;
    @Getter
    @Setter
    protected boolean staffChat;
    @Getter
    @Setter
    protected String nick;
    @Getter
    @Setter
    protected boolean toggledMessages;
    @Getter
    @Setter
    protected UUID lastReplier;
    @Getter
    @Setter
    protected boolean socialSpy;
    @Getter
    @Setter
    protected long lastSpeakTime;

    protected Participator() {

    }

    public Participator(UUID uniqueId, String nick) {
        this.nick = nick;
        this.uniqueId = uniqueId;
        this.globalChatVisible = true;
        this.toggledMessages = true;
    }

    public Participator(IParticipator user) {
        this.uniqueId = user.getUniqueId();
        this.staffChatVisible = user.isStaffChatVisible();
        this.globalChatVisible = user.isGlobalChatVisible();
        this.staffChat = user.isStaffChat();
        this.nick = user.getNick();
        this.toggledMessages = user.isToggledMessages();
        this.lastReplier = user.getLastReplier();
        this.socialSpy = user.isSocialSpy();
        this.lastSpeakTime = user.getLastSpeakTime();
    }

    public Participator(Map<String, Object> map) {
        if (map.containsKey("uniqueId") && map.containsKey("nick") && map.containsKey("last-replier")
                && map.containsKey("toggled-messages") && map.containsKey("staff-chat-visible")
                && map.containsKey("staff-chat") && map.containsKey("global-chat-visible") &&
                map.containsKey("social-spy") && map.containsKey("last-speak-time")) {
            this.uniqueId = UUID.fromString((String) map.get("uniqueId"));
            this.nick = (String) map.get("nick");
            this.lastReplier = map.get("last-replier") == null ? null : UUID.fromString((String) map.get("last-replier"));
            this.toggledMessages = (boolean) map.get("toggled-messages");
            this.staffChatVisible = (boolean) map.get("staff-chat-visible");
            this.globalChatVisible = (boolean) map.get("global-chat-visible");
            this.staffChat = (boolean) map.get("staff-chat");
            this.socialSpy = (boolean) map.get("social-spy");
            this.lastSpeakTime = Long.parseLong((String) map.get("last-speak-time"));
        }
    }


    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uniqueId", this.uniqueId.toString());
        map.put("nick", this.nick);
        map.put("last-replier", lastReplier == null ? null : lastReplier.toString());
        map.put("toggled-messages", toggledMessages);
        map.put("staff-chat-visible", this.staffChatVisible);
        map.put("global-chat-visible", this.globalChatVisible);
        map.put("staff-chat", this.staffChat);
        map.put("social-spy", this.socialSpy);
        map.put("last-speak-time", ((Long) lastSpeakTime).toString());
        return map;
    }

    public void merge(IParticipator participator) {
        this.staffChatVisible = participator.isStaffChatVisible();
        this.globalChatVisible = participator.isGlobalChatVisible();
        this.staffChat = participator.isStaffChat();
        this.nick = participator.getNick();
        this.toggledMessages = participator.isToggledMessages();
        this.lastReplier = participator.getLastReplier();
        this.socialSpy = participator.isSocialSpy();
        this.lastSpeakTime = participator.getLastSpeakTime();
    }

    public void update() {
    }

}
