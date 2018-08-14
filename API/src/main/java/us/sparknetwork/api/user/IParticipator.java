package us.sparknetwork.api.user;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.UUID;

public interface IParticipator extends ConfigurationSerializable {

    UUID getUniqueId();

    boolean isStaffChatVisible();

    void setStaffChatVisible(boolean staffChatVisible);

    boolean isGlobalChatVisible();

    void setGlobalChatVisible(boolean globalChatVisible);

    boolean isStaffChat();

    void setStaffChat(boolean staffChat);

    String getNick();

    void setNick(String nick);

    boolean isToggledMessages();

    void setToggledMessages(boolean toggledMessages);

    UUID getLastReplier();

    void setLastReplier(UUID lastReplier);

    boolean isSocialSpy();

    void setSocialSpy(boolean socialSpy);

    long getLastSpeakTime();

    void setLastSpeakTime(long lastSpeakTime);

    void merge(IParticipator participator);

    void update();
}
