package us.sparknetwork.BanManager.commands;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import us.sparknetwork.BanManager.BanManager;
import us.sparknetwork.BanManager.BansConfig;
import us.sparknetwork.api.bans.BansAPI.Type;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.util.PlayerUtils;

public class KickCommand extends CommandModule {
	
	BanManager plugin;

	public KickCommand(BanManager plugin) {
		super("kick", 1, -1, "Usage /(command) <player> [reason] [-s]");
		this.plugin = plugin;
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		if (args.length == 1) {
			Player target = PlayerUtils.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
				return false;
			}
			if (BansConfig.UNPUNISHABLE_PLAYERS.contains(target.getName())) {
				sender.sendMessage(ChatColor.RED + "You can't punish that player.");
				return false;
			}
			plugin.insertKick(target.getUniqueId(), sender.getName(), null, false);
			target.kickPlayer(String.format(
					ChatColor.RED + "Your account was kicked from %1$s \n Reason: %2$s \n Kicked by: %3$s",
					BansConfig.SERVER_NAME, "None", sender.getName()));

			return true;

		} else {
			String larg = args[args.length - 1];
			StringBuilder reason = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-s")) {
					continue;
				}
				reason.append(args[i]);
				if(i < args.length){
					reason.append(" ");
				}
			}
			String kickreason = StringUtils.isBlank(reason.toString()) ? "None"
					: reason.toString();
			Player target = PlayerUtils.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
				return false;
			}
			if (BansConfig.UNPUNISHABLE_PLAYERS.contains(target.getName())) {
				sender.sendMessage(ChatColor.RED + "You can't punish that player.");
				return false;
			}
			if (!BansConfig.IS_LOBBY) {
				target.kickPlayer(String.format(
						ChatColor.RED + "Your account was kicked from %1$s \n Reason: %2$s \n Kicked by: %3$s",
						BansConfig.SERVER_NAME, kickreason, sender.getName()));
			}
			boolean silent = larg.equalsIgnoreCase("-s");
			plugin.insertKick(target.getUniqueId(), sender.getName(), reason.toString(), silent);

			return true;
		}
	}

}
