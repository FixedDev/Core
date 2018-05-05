package us.sparknetwork.BanManager.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import us.sparknetwork.BanManager.BanManager;
import us.sparknetwork.api.bans.BansAPI.Type;
import us.sparknetwork.api.bans.Database;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.util.PlayerUtils;

public class UnbanCommand extends CommandModule {

	BanManager plugin;

	public UnbanCommand(BanManager plugin) {
		super("unban", 1, 1, "Usage /(command) <player>", "pardon");
		this.plugin = plugin;
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		OfflinePlayer target = PlayerUtils.getOfflinePlayer(args[0]);
		if (Database.get().isPlayerBanned(target.getUniqueId(), null)) {
			BanManager.getInstance().unpunishPlayer(target.getUniqueId(), Type.BAN);
			sender.sendMessage(ChatColor.YELLOW + String.format("The player %1$s was unbanned.", target.getName()));
			return true;
		}
		BanManager.getInstance().unpunishPlayer(target.getUniqueId(), Type.BAN);
		sender.sendMessage(ChatColor.YELLOW + String
				.format("The player %1$s isn't banned if he try to enter it will be unbanned.", target.getName()));
		return true;
	}

}
