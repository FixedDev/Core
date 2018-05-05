package us.sparknetwork.BanManager.commands;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import us.sparknetwork.BanManager.BanManager;
import us.sparknetwork.BanManager.BansConfig;
import us.sparknetwork.api.bans.BansAPI.Type;
import us.sparknetwork.api.bans.Database;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.util.PlayerUtils;
import us.sparknetwork.util.TimeParser;
import us.sparknetwork.util.TimeUtils;

public class BanCommand extends CommandModule
{

	BanManager plugin;

	public BanCommand(BanManager plugin) {
		super("ban", 1, -1, "Usage /(command) <player> [time] [reason] [-s]", "tempban");
		this.plugin = plugin;
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (!sender.hasPermission("banmanager.command.ban.perma")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to create permanent bans");
                return false;
            }
            OfflinePlayer target = PlayerUtils.getOfflinePlayer(args[0]);
            if (Database.get().isPlayerBanned(target.getUniqueId(), null)) {
                if (!sender.hasPermission("banmanager.command.ban.override")) {
                    sender.sendMessage(
                            ChatColor.RED + "You don't have permissions to override bans, contact an admin.");
                    return false;
                }
                plugin.unpunishPlayer(target.getUniqueId(), Type.BAN);
                sender.sendMessage(ChatColor.YELLOW
                        + String.format("The past ban of %1$s was overrided for this action", target.getName()));
            }
            if (BansConfig.UNPUNISHABLE_PLAYERS.contains(target.getName())) {
                sender.sendMessage(ChatColor.RED + "You can't punish that player.");
                return false;
            }
            plugin.insertPermanentBan(target.getUniqueId(), sender.getName(), null, false);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.DARK_RED + "WARNING: " + ChatColor.RED
                        + String.format("The player %1$s don't played before on this server.", args[0]));
            }
            if (target.isOnline()) {
                if (!BansConfig.IS_LOBBY) {
                    target.getPlayer()
                            .kickPlayer(ChatColor.RED + String.format(
                                    "Your account was banned from %1$s \n Reason: %2$s \n Banned by: %3$s",
                                    BansConfig.SERVER_NAME, "None", sender.getName()));
                }
            }
            return true;
        } else {
            OfflinePlayer target = PlayerUtils.getOfflinePlayer(args[0]);

            if (Database.get().isPlayerBanned(target.getUniqueId(), null)) {
                if (!sender.hasPermission("banmanager.command.ban.override")) {
                    sender.sendMessage(
                            ChatColor.RED + "You don't have permissions to override bans, contact an admin.");
                    return false;
                }
                plugin.unpunishPlayer(target.getUniqueId(), Type.BAN);
                sender.sendMessage(ChatColor.YELLOW
                        + String.format("The past ban of %1$s was overrided for this action", target.getName()));
            }
            String larg = args[args.length - 1];
            Long time = 0l;
            try {
                time = TimeParser.parse(args[1]);
            } catch (NumberFormatException e) {
                time = 0l;
            }
            if (time > 0) {
                if (!sender.hasPermission("banmanager.command.ban.temp")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to create temporal bans");
                    return false;
                }
                if (BansConfig.UNPUNISHABLE_PLAYERS.contains(target.getName())) {
                    sender.sendMessage(ChatColor.RED + "You can't punish that player.");
                    return false;
                }
                StringBuilder reason = new StringBuilder();
                if (args.length > 2) {
                    for (int i = 2; i < args.length; i++) {
                        if (args[i].equalsIgnoreCase("-s")) {
                            continue;
                        }
                        reason.append(args[i]);
                        if(i < args.length){
                            reason.append(" ");
                        }
                    }
                }
                String banreason = StringUtils.isBlank(reason.toString()) ? "None"
                        : reason.toString();

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(ChatColor.DARK_RED + "WARNING: " + ChatColor.RED
                            + String.format("The player %1$s don't played before on this server.", args[0]));
                }
                if (target.isOnline()) {
                    if (!BansConfig.IS_LOBBY) {

                        target.getPlayer().kickPlayer(ChatColor.RED + String.format(
                                "Your account was temporally banned from %1$s \n Reason: %2$s \n Banned by: %3$s \n Expiration: %4$s",
                                BansConfig.SERVER_NAME, banreason, sender.getName(),
                                TimeUtils.getMSG(TimeParser.parse(args[1]))));
                    }
                }
                boolean silent = larg.equalsIgnoreCase("-s");

                plugin.insertTemporalBan(target.getUniqueId(), TimeParser.parse(args[1]), sender.getName(),
                        banreason, silent);
                return true;
            }
            if (!sender.hasPermission("banmanager.command.ban.perma")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to create permanent bans");
                return false;
            }
            if (BansConfig.UNPUNISHABLE_PLAYERS.contains(target.getName())) {
                sender.sendMessage(ChatColor.RED + "You can't punish that player.");
                return false;
            }
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
            String banreason = StringUtils.isBlank(reason.toString()) ? "None"
                    : reason.toString();
            if (!target.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.DARK_RED + "WARNING:" + ChatColor.RED
                        + String.format("The player %1$s don't played before on this server.", args[0]));
            }
            if (target.isOnline()) {
                if (!BansConfig.IS_LOBBY) {
                    target.getPlayer()
                            .kickPlayer(ChatColor.RED + String.format(
                                    "Your account was banned from %1$s \n Reason: %2$s \n Banned by: %3$s",
                                    BansConfig.SERVER_NAME, banreason, sender.getName()));
                }

            }
            boolean silent = larg.equalsIgnoreCase("-s");

            plugin.insertPermanentBan(target.getUniqueId(), sender.getName(), banreason, silent);
            return true;
        }
	}

}
