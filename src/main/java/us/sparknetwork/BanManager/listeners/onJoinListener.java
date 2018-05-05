package us.sparknetwork.BanManager.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import us.sparknetwork.BanManager.BMDatabase;
import us.sparknetwork.BanManager.BanManager;
import us.sparknetwork.BanManager.BansConfig;
import us.sparknetwork.api.bans.BansAPI.Type;
import us.sparknetwork.api.bans.Database;
import us.sparknetwork.api.bans.Punishment;
import us.sparknetwork.util.TimeUtils;

public class onJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(PlayerLoginEvent e) {
        if (BansConfig.IS_LOBBY) {
            return;
        }
        Player player = e.getPlayer();
        if (Database.get().isPlayerBanned(player.getUniqueId(),
                e.getAddress().getHostAddress())) {
            Punishment punish = ((BMDatabase) Database.get())
                    .getBan(player.getUniqueId(), e.getAddress().getHostAddress());
            if (punish.getType() == Type.BAN) {
                if (punish.isIppunishment()) {
                    if (punish.isPermanent()) {
                        e.disallow(Result.KICK_BANNED,
                                ChatColor.RED + String.format(
                                        "Your account was ip-banned from %1$s \n Reason: %2$s \n Banned by: %3$s",
                                        BansConfig.SERVER_NAME, punish.getReason(), punish.getPunisher()));
                    } else {
                        if (punish.getExpire() - BanManager.getInstance().getActualTime() <= 0) {
                            BanManager.getInstance().unpunishPlayer(player.getUniqueId(), Type.BAN);
                            e.allow();
                            return;
                        }
                        e.disallow(Result.KICK_BANNED, ChatColor.RED + String.format(
                                "Your account was temporally ip-banned from %1$s \n Reason: %2$s \n Banned by: %3$s \n Expiration: %4$s",
                                BansConfig.SERVER_NAME, punish.getReason(), punish.getPunisher(),
                                TimeUtils.getMSG(punish.getExpire() - BanManager.getInstance().getActualTime())));
                    }
                    return;
                }
                if (punish.isPermanent()) {
                    e.disallow(Result.KICK_BANNED,
                            ChatColor.RED + String.format(
                                    "Your account was banned from %1$s \n Reason: %2$s \n Banned by: %3$s",
                                    BansConfig.SERVER_NAME, punish.getReason(), punish.getPunisher()));
                } else {
                    if (punish.getExpire() - BanManager.getInstance().getActualTime() <= 0) {
                        BanManager.getInstance().unpunishPlayer(player.getUniqueId(), Type.BAN);
                        e.allow();
                        return;
                    }
                    e.disallow(Result.KICK_BANNED, ChatColor.RED + String.format(
                            "Your account was temporally banned from %1$s \n Reason: %2$s \n Banned by: %3$s \n Expiration: %4$s",
                            BansConfig.SERVER_NAME, punish.getReason(), punish.getPunisher(),
                            TimeUtils.getMSG(punish.getExpire() - BanManager.getInstance().getActualTime())));
                }
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void SolveIpConflicts(PlayerLoginEvent e) {
        String ip = e.getAddress().getHostAddress();
        Player player = e.getPlayer();
        if (Database.get().isPlayerBanned(player.getUniqueId(), ip)) {
            Punishment punish = ((BMDatabase) Database.get())
                    .getBan(player.getUniqueId(), e.getAddress().getHostAddress());
            if (punish.getIp() == null) {
                punish.setIp(ip);
                BanManager.getInstance().overridePunishment(punish, punish.getPlayer(), Type.BAN);
                if (punish.getPlayer() != player.getUniqueId()) {
                    BanManager.getInstance().insertIpban(player.getUniqueId(), ip, punish.getPunisher(),
                            punish.getReason(), true);
                }
                return;
            }
            if (punish.getIp() != ip) {
                punish.setIp(ip);
                BanManager.getInstance().overridePunishment(punish, punish.getPlayer(), Type.BAN);
                if (punish.getPlayer() != player.getUniqueId()) {
                    BanManager.getInstance().insertIpban(player.getUniqueId(), ip, punish.getPunisher(),
                            punish.getReason(), true);
                }
            }
        }
    }
}
