package us.sparknetwork.BanManager.commands;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableList;

import us.sparknetwork.BanManager.BanManager;
import us.sparknetwork.BanManager.BansConfig;
import us.sparknetwork.api.bans.BansAPI.Type;
import us.sparknetwork.api.bans.Database;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.util.PlayerUtils;

public class BlacklistCommand extends CommandModule {

	BanManager plugin;

	public BlacklistCommand(BanManager plugin) {
		super("blacklist", 1, -1, "Usage /(command) <player> [reason] [-s]",
				ImmutableList.of("banip", "ipban", "ip-ban", "ban-ip"));
		this.plugin = plugin;
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		if (args.length == 1) {
			OfflinePlayer target = PlayerUtils.getOfflinePlayer(args[0]);
			if (!target.hasPlayedBefore()) {
				sender.sendMessage(ChatColor.DARK_RED + "WARNING:" + ChatColor.RED
						+ String.format("The player %1$s don't played before on this server.", args[0]));
			}
			if (BansConfig.UNPUNISHABLE_PLAYERS.contains(target.getName())) {
				sender.sendMessage(ChatColor.RED + "You can't punish that player.");
				return false;
			}
			if (Database.get().isPlayerBanned(target.getUniqueId(), null)) {
				if (!sender.hasPermission("banmanager.command.ban.override")) {
					sender.sendMessage(
							ChatColor.RED + "You don't have permissions to override bans, contact an admin.");
					return false;
				}
				plugin.unpunishPlayer(target.getUniqueId(), Type.BAN);
				sender.sendMessage(ChatColor.YELLOW
						+ String.format("The past bans of %1$s was overrided for this action", target.getName()));
			}
			plugin.insertIpban(target.getUniqueId(),
					target.isOnline() ? target.getPlayer().getAddress().getHostString() : "##offline##",
					sender.getName(), null, false);
			if (target.isOnline()) {
				if (!BansConfig.IS_LOBBY) {
					target.getPlayer()
							.kickPlayer(String.format(
									ChatColor.RED + "Your account was ip-banned from %1$s \n Reason: %2$s",
									BansConfig.SERVER_NAME, "None"));
				}
			}
			return true;
		} else {
			OfflinePlayer target = PlayerUtils.getOfflinePlayer(args[0]);
			if (!target.hasPlayedBefore()) {
				sender.sendMessage(ChatColor.DARK_RED + "WARNING:" + ChatColor.RED
						+ String.format("The player %1$s don't played before on this server.", args[0]));
			}
			if (BansConfig.UNPUNISHABLE_PLAYERS.contains(target.getName())) {
				sender.sendMessage(ChatColor.RED + "You can't punish that player.");
				return false;
			}
			if (Database.get().isPlayerBanned(target.getUniqueId(), null)) {
				if (!sender.hasPermission("banmanager.command.ban.override")) {
					sender.sendMessage(
							ChatColor.RED + "You don't have permissions to override bans, contact an admin.");
					return false;
				}
				plugin.unpunishPlayer(target.getUniqueId(), Type.BAN);
				sender.sendMessage(ChatColor.YELLOW
						+ String.format("The past bans of %1$s was overrided for this action", target.getName()));
			}
			String larg = args[args.length - 1];
			StringBuilder reason = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-s")) {
					continue;
				}
				reason.append(args[i]);
				if(i < args.length-1){
					reason.append(" ");
				}
			}
			String blreason = StringUtils.isBlank(reason.toString()) ? "None" : reason.toString();
			if (target.isOnline()) {
				if (!BansConfig.IS_LOBBY) {

					target.getPlayer()
							.kickPlayer(String.format(
									ChatColor.RED + "Your account was ip-banned from %1$s \n Reason: %2$s",
									BansConfig.SERVER_NAME, blreason));
				}
			}
			boolean silent = larg.equalsIgnoreCase("-s");
			plugin.insertIpban(target.getUniqueId(),
					target.isOnline() ? target.getPlayer().getAddress().getHostString() : "##offline##",
					sender.getName(), blreason, silent);
			return true;
		}

	}

}
