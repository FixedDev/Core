package us.sparknetwork.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.sparknetwork.api.event.AsyncUserLoadEvent;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.handlers.StaffModeManager;
import us.sparknetwork.core.user.User;
import us.sparknetwork.core.user.UserManager;
import us.sparknetwork.util.MessageUtils;

import java.util.*;
import java.util.stream.Collectors;

public class UserManagerListener implements Listener {


    public UserManagerListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoinLow(PlayerJoinEvent e) {
        UserManager.getInstance().handleJoin(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onUserLoad(AsyncUserLoadEvent e) {
        List<User> playerlist = this.getPlayersWithSharedIpOf(e.getUser().getPlayer().getAddress().getAddress().getHostAddress());
        IUser user = e.getUser();
        if (playerlist.size() > 1) {
            Bukkit.broadcast(CoreConstants.YELLOW + ChatColor.BOLD.toString() + e.getUser().getPlayer().getName()
                    + CoreConstants.GRAY + " maybe have more accounts.", "network.staff");
        }
        user.setNick(e.getUser().getPlayer().getName());
        if (user.tryLoggingIp(e.getUser().getPlayer().getAddress().getAddress().getHostAddress())) {
            MessageUtils.sendMessageToStaff(ChatColor.RED + ChatColor.BOLD.toString() + e.getUser().getPlayer().getName()
                    + CoreConstants.GRAY + " joined with a new ip.");
        } else {
            MessageUtils.sendMessageToStaff(ChatColor.GREEN + ChatColor.BOLD.toString() + e.getUser().getPlayer().getName()
                    + CoreConstants.GRAY + " joined with a existent ip.");
        }
        if (user.isStaffMode()) {
            StaffModeManager.getInstance().setStaffMode(user.getPlayer(), user.isStaffMode());
        }
        user.updateVanishState(user.getPlayer(), Bukkit.getOnlinePlayers(), user.isVanished());
        List<IUser> vanishedUsers = new ArrayList<>();
        UserManager.getInstance().getUserMap().values().stream().filter(u -> u instanceof IUser).map(u -> (IUser) u).filter(IUser::isVanished).forEach(vanishedUsers::add);
        Collection<Player> player = Collections.singleton(user.getPlayer());
        vanishedUsers.forEach(u -> Bukkit.getScheduler().runTask(CorePlugin.getPlugin(), () -> u.updateVanishState(u.getPlayer(), player, true)));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLeave(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        IUser user = UserManager.getInstance().getUser(uuid);
        UserManager.getInstance().handleQuit(e.getPlayer());
        if (user.isStaffMode()) {
            StaffModeManager.getInstance().setStaffMode(e.getPlayer(), false);
        }
    }

    public List<User> getPlayersWithSharedIpOf(String address) {
        if (CorePlugin.getPlugin().isMongo()) {
            List<User> users = CorePlugin.getPlugin().getDatastore().find(User.class).field("ipHistory").hasThisOne(address).asList();
            return users;
        } else {
            List<User> users = UserManager.getInstance().getUserMap().values().stream().filter(u -> u instanceof User).map(u -> (User) u).filter(u -> u.getIpHistory().isEmpty()).filter(u -> u.getIpHistory().contains(address)).collect(Collectors.toList());
            return users;
        }
    }


}
