package us.sparknetwork.BanManager.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import us.sparknetwork.BanManager.BMDatabase;
import us.sparknetwork.BanManager.BanManager;
import us.sparknetwork.api.bans.BansAPI.Type;
import us.sparknetwork.api.bans.Database;
import us.sparknetwork.api.bans.Punishment;
import us.sparknetwork.util.TimeUtils;

public class onChatListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {

		Player player = e.getPlayer();
		if (BMDatabase.get().isPlayerMuted(player.getUniqueId(), player.getAddress().getAddress().getHostAddress())) {
			Punishment mute = ((BMDatabase) Database.get()).getMute(player.getUniqueId(),
					player.getAddress().getAddress().getHostAddress());
			if (mute.getType() != Type.MUTE | mute.getType() != Type.IPMUTE) {
				return;
			}
			if (mute.isPermanent()) {
				e.setCancelled(true);
				player.sendMessage(ChatColor.GREEN + String.format("You are permanently muted for %1$s by %2$s.",
						mute.getReason(), ChatColor.AQUA + mute.getPunisher() + ChatColor.GREEN));
				return;
			} else {
				e.setCancelled(true);
				if(!mute.isExpired(BanManager.getInstance().getActualTime())) {
					player.sendMessage(ChatColor.GREEN + String.format(
							"You are temp-muted for %1$s by %2$s for the time of %3$s.", mute.getReason() + ChatColor.GREEN,
							ChatColor.AQUA + mute.getPunisher() + ChatColor.GREEN,
							TimeUtils.getMSG(mute.getExpire() - mute.getPunishtime())));
					return;
				}
				BanManager.getInstance().unpunishPlayer(e.getPlayer().getUniqueId(), Type.MUTE);
				return;
			}
		}

	}

}
