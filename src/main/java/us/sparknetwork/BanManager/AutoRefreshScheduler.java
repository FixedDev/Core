package us.sparknetwork.BanManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.sparknetwork.api.bans.BansAPI;
import us.sparknetwork.api.bans.Punishment;
import us.sparknetwork.core.server.ServerSettings;

import java.util.List;

public class AutoRefreshScheduler extends BukkitRunnable {

    long lastupdate = BanManager.getInstance().getActualTime();

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().isEmpty())
            return;
        List<Punishment> bans = BanManager.getInstance().getLastBans(lastupdate);
        bans.forEach(punish -> {
            if(punish.getServer().equalsIgnoreCase(ServerSettings.NAME)){
                return;
            }
            Player pl = Bukkit.getPlayer(punish.getPlayer());
            if (pl != null) {
                if (punish.getType() == BansAPI.Type.KICK) {
                    pl.kickPlayer(ChatColor.RED + String.format("You we're kicked from the network by %1$s, reason %2$s", punish.getPunisher(), punish.getReason()));
                } else if (punish.getType() == BansAPI.Type.BAN) {
                    pl.kickPlayer(ChatColor.RED + String.format("You we're banned from the network by %1$s, rejoin for more details", punish.getPunisher()));
                }

            }
            BanManager.getInstance().displayPunishmentMessage(punish);
        });
        lastupdate = System.currentTimeMillis();
    }

}
