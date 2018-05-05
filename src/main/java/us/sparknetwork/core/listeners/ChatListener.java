package us.sparknetwork.core.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.ServerHandler;
import us.sparknetwork.core.user.UserManager;
import us.sparknetwork.util.TimeUtils;

import java.util.*;

public class ChatListener implements Listener {

    private Map<UUID, String> messages;
    private CorePlugin plugin;

    public ChatListener(CorePlugin plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
    }

    public String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ChatFormat(AsyncPlayerChatEvent e) {
        Player target = e.getPlayer();
        IUser user = UserManager.getInstance().getUser(target.getUniqueId());
        Set<Player> viewers = new HashSet<>();
        for (Player player : e.getRecipients()) {
            IUser tu = UserManager.getInstance().getUser(player.getUniqueId());
            if (tu.isGlobalChatVisible()) {
                viewers.add(player);
            }
        }
        if (!user.isStaffChat()) {
            this.formatPublicChat(e);
            e.getRecipients().clear();
            e.getRecipients().addAll(viewers);
            return;
        }
        if (!user.isStaffChatVisible()) {
            target.sendMessage(ChatColor.RED
                    + "Your message was sent, but you cannot see the staff chat messages as you notifications are disabled: Use /togglesc.");
        }
        plugin.getServerManager().sendData("**", "StaffChat", e.getPlayer().getName(), e.getMessage());
        e.setCancelled(true);
        Bukkit.getOnlinePlayers().stream()
                .map(p -> UserManager.getInstance().getUser(p.getUniqueId()))
                .filter(u -> u.isStaffChatVisible() && u.getPlayer().hasPermission("core.staffchat.see"))
                .forEach(u -> u.getPlayer().sendMessage(String.format(ChatColor.BLUE + "(Staff Chat) " + ChatColor.AQUA + "%1$s: " + net.md_5.bungee.api.ChatColor.GRAY + "%2$s", e.getPlayer().getName(), e.getMessage())));

    }

    private void formatPublicChat(AsyncPlayerChatEvent e) {
        IUser user = UserManager.getInstance().getUser(e.getPlayer().getUniqueId());
        ServerHandler handler = plugin.getServerHandler();
        if (((handler.getChatDisabled() - System.currentTimeMillis()) > 0)
                && !e.getPlayer().hasPermission("core.chat.disabled.bypass")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You can't send messages while the chat is disabled.");
            return;
        }
        if (messages.containsKey(e.getPlayer().getUniqueId()) && messages.get(e.getPlayer().getUniqueId()).equalsIgnoreCase(e.getMessage())
                && !e.getPlayer().hasPermission("core.chat.doublepost.bypass")) {
            e.getPlayer().sendMessage(ChatColor.RED + "Double posting is prohibited");
            e.setCancelled(true);
            return;
        }
        if ((user.getLastSpeakTime() + handler.getChatSlowedDelay() * 1000) > System.currentTimeMillis() &&
                !e.getPlayer().hasPermission("core.chat.slow.bypass") &&
                handler.getChatSlowed() >= System.currentTimeMillis()) {
            e.getPlayer().sendMessage(ChatColor.RED
                    + "You have a delay of %time% between messages because the slow mode is enabled.".replace("%time%",
                    TimeUtils.getMSG((user.getLastSpeakTime() + handler.getChatSlowedDelay() * 1000) - System.currentTimeMillis())));
            e.setCancelled(true);
            return;
        }
        messages.put(e.getPlayer().getUniqueId(), e.getMessage());
        e.getPlayer().setDisplayName(e.getPlayer().isOp() ? ChatColor.RED + e.getPlayer().getDisplayName() : e.getPlayer().getDisplayName());
        String RawChatFormat = handler.getChatFormat();
        RawChatFormat = color(RawChatFormat
                .replace("{prefix}", getPrefix(e.getPlayer()))
                .replace("{suffix}", getSuffix(e.getPlayer()))
                .replace("{user}", "%1$s")
                .replace("{chat}", "%2$s"));
        e.setMessage(e.getPlayer().hasPermission("core.chat.color") ? ChatColor.translateAlternateColorCodes('&', e.getMessage()) : e.getMessage());
        e.setFormat(RawChatFormat);
        user.setLastSpeakTime(System.currentTimeMillis());
    }

    public static String getPrefix(Player player) {
        String[] groups = CorePlugin.getChat().getPlayerGroups(player);
        StringBuilder prefix = new StringBuilder();
        if (groups.length > 0) {
            for (String group : groups) {
                String gprefix = CorePlugin.getChat().getGroupPrefix((String) null, group);
                    if (!StringUtils.isBlank(gprefix)) {
                    prefix.append(ChatColor.translateAlternateColorCodes('&', gprefix));
                }
            }
        }
        return prefix.length() <= 0 ? "" : prefix.toString();
    }

    public static String getSuffix(Player player) {
        String[] groups = CorePlugin.getChat().getPlayerGroups(player);
        StringBuilder suffix = new StringBuilder();
        if (groups.length > 0) {
            for (String group : groups) {
                String gsuffix = CorePlugin.getChat().getGroupSuffix((String) null, group);
                if (!StringUtils.isBlank(gsuffix)) {
                    suffix.append(ChatColor.translateAlternateColorCodes('&', gsuffix));
                }
            }
        }
        return suffix.length() <= 0 ? "" : suffix.toString();
    }

}
