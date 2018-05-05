package us.sparknetwork.core.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.sparknetwork.api.event.ReceiveDataEvent;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.user.UserManager;
import us.sparknetwork.util.SpigotUtils;

public class HSCServerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSync(ReceiveDataEvent e) {
        String server = e.getServer();
        if (e.getType().equalsIgnoreCase("StaffChat")) {
            if(e.getArgs().length < 2){
                return;
            }
            String playerName = e.getArgs()[0];
            String message = e.getArgs()[1];
            Bukkit.getOnlinePlayers().stream()
                    .map(p -> UserManager.getInstance().getUser(p.getUniqueId()))
                    .filter(u -> u.isStaffChatVisible() && u.getPlayer().hasPermission("core.staffchat.see"))
                    .forEach(u -> u.getPlayer().sendMessage(String.format(ChatColor.BLUE + "(Staff Chat) " + ChatColor.AQUA + "%1$s: " + ChatColor.GRAY + "%2$s", playerName, message)));
            return;
        }
        if (e.getType().equalsIgnoreCase("Helpop")) {
            if(e.getArgs().length < 2){
                return;
            }
            String playerName = e.getArgs()[0];
            String message = e.getArgs()[1];
            this.sendMessageToStaff(playerName, server, message);
            return;
        }
    }

    public void sendMessageToStaff(String player, String Server, String str) {
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission("core.command.helpop.receive")) {
                ComponentBuilder builder = new ComponentBuilder("[");
                BaseComponent[] components = builder.color(SpigotUtils.toBungee(CoreConstants.GOLD))
                        .append(Server).color(SpigotUtils.toBungee(CoreConstants.YELLOW))
                        .append("] ").color(SpigotUtils.toBungee(CoreConstants.GOLD))
                        .append(" ").append(player).color(SpigotUtils.toBungee(CoreConstants.GOLD)).bold(true)
                        .append(" ").append("is requesting help").color(SpigotUtils.toBungee(CoreConstants.YELLOW))
                        .append(": ").color(ChatColor.DARK_GRAY)
                        .append(str).color(ChatColor.LIGHT_PURPLE)
                        .create();
                for(int i = 0; i < components.length;i++){
                    if(i < 3){
                        components[i].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + Server));
                    }
                }
                staff.spigot().sendMessage(components);
            }
        }
    }
}
