package us.sparknetwork.core;

import lombok.Data;
import us.sparknetwork.util.Config;

import java.util.List;

@Data
public class ServerHandler {

    private final Config config;
    private final CorePlugin plugin;

    private List<String> announcements;
    private boolean announcerEnabled;
    private int announcerDelay;

    private int clearLagDelay;

    private String chatFormat;
    private long chatDisabled;
    private long chatSlowed;
    private int chatSlowedDelay;

    private String serverFullMessage;
    private int maxPlayers;

    private List<String> allowedops;

    private List<String> freezeMessage;

    public ServerHandler(CorePlugin plugin) {
        this.plugin = plugin;
        this.config = new Config(plugin, "config");
        if (config.getConfigurationSection("server") != null) {
            new CoreConstants().getConstants(config.getConfigurationSection("server"));
        }
        this.reloadServerData();
    }

    public void reloadServerData() {
        this.announcements = config.getStringList("announcer.announcements");
        this.announcerEnabled = config.getBoolean("announcer.enabled", false);
        this.announcerDelay = config.getInt("announcer.delay", 1000);

        this.clearLagDelay = config.getInt("clearlag.delay", 300);

        this.chatFormat = config.getString("chat.format", "+prefix +user&8»&7 +chat");
        this.chatDisabled = config.getLong("chat.disabled.millis", 0L);
        this.chatSlowed = config.getLong("chat.slowed.millis", 0L);
        this.chatSlowedDelay = config.getInt("chat.slowed-delay", 3);

        this.serverFullMessage = config.getString("kick-server-full");
        this.maxPlayers = config.getInt("max-players");

        this.allowedops = config.getStringList("allowed-ops");

        this.freezeMessage = config.getStringList("freeze-message");
    }

    public void saveServerData() {
        config.set("announcer.announcements", this.getAnnouncements());
        config.set("announcer.enabled", this.isAnnouncerEnabled());
        config.set("announcer.delay", this.getAnnouncerDelay());

        config.set("clearlag.delay", this.getClearLagDelay());

        config.set("chat.format", this.getChatFormat());
        config.set("chat.disabled.millis", this.getChatDisabled());
        config.set("chat.slowed.millis", this.getChatSlowed());
        config.set("chat.slowed-delay", this.getChatSlowedDelay());

        config.set("kick-server-full", this.getServerFullMessage());
        config.set("max-players", this.getMaxPlayers());

        config.set("allowed-ops", this.getAllowedops());
        config.set("freeze-message",this.getFreezeMessage());
        config.save();
    }

}
