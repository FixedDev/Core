package us.sparknetwork.BanManager.commands;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.sparknetwork.BanManager.BanManager;
import us.sparknetwork.BanManager.BansConfig;
import us.sparknetwork.api.bans.BansAPI.Type;
import us.sparknetwork.api.bans.Database;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.StaffPriority;
import us.sparknetwork.util.PlayerUtils;
import us.sparknetwork.util.TimeParser;

import java.util.Arrays;

@SuppressWarnings("deprecation")
public class MuteCommand extends CommandModule {

    private BanManager plugin;

    public MuteCommand(BanManager banManager) {
        super("mute", 1, -1, "Usage /<command> <playerName> [time] [reason]", Arrays.asList("tempmute"));
        this.plugin = banManager;
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (!sender.hasPermission("banmanager.command.mute.perma")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to create permanent bans");
                return false;
            }
            OfflinePlayer target = PlayerUtils.getOfflinePlayer(args[0]);
            if (BansConfig.UNPUNISHABLE_PLAYERS.contains(target.getName())) {
                sender.sendMessage(ChatColor.RED + "You can't punish that player.");
                return true;
            }
            if (sender instanceof Player) {
                if (target.isOnline()) {
                    StaffPriority tsp = StaffPriority.getByPlayer(target.getPlayer());
                    StaffPriority ssp = StaffPriority.getByPlayer((Player) sender);
                    if (tsp.isMoreThan(ssp)) {
                        sender.sendMessage(ChatColor.RED + "You can't mute staff with mayor priority than you.");
                        return true;
                    }
                }
            }
            if (Database.get().isPlayerMuted(target.getUniqueId(), null)) {
                if (!sender.hasPermission("banmanager.command.mute.override")) {
                    sender.sendMessage(
                            ChatColor.RED + "You don't have permissions to override mutes, contact an admin.");
                    return true;
                }
                plugin.unpunishPlayer(target.getUniqueId(), Type.MUTE);
                sender.sendMessage(ChatColor.YELLOW
                        + String.format("The past mute of %1$s was overrided for this action", target.getName()));
            }

            plugin.insertPermanentMute(target.getUniqueId(), sender.getName(), null, false);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.DARK_RED + "WARNING: " + ChatColor.RED
                        + String.format("The player %1$s don't played before on this server.", args[0]));
            }
            return true;
        } else {
            OfflinePlayer target = PlayerUtils.getOfflinePlayer(args[0]);

            if (BansConfig.UNPUNISHABLE_PLAYERS.contains(target.getName())) {
                sender.sendMessage(ChatColor.RED + "You can't punish that player.");
                return false;
            }
            if (sender instanceof Player) {
                if (target.isOnline()) {
                    StaffPriority tsp = StaffPriority.getByPlayer(target.getPlayer());
                    StaffPriority ssp = StaffPriority.getByPlayer((Player) sender);
                    if (tsp.isMoreThan(ssp)) {
                        sender.sendMessage(ChatColor.RED + "You can't mute staff with mayor priority than you.");
                        return true;
                    }
                }
            }
            if (Database.get().isPlayerMuted(target.getUniqueId(), null)) {
                if (!sender.hasPermission("banmanager.command.mute.override")) {
                    sender.sendMessage(
                            ChatColor.RED + "You don't have permissions to override mutes, contact an admin.");
                    return true;
                }
                plugin.unpunishPlayer(target.getUniqueId(), Type.MUTE);
                sender.sendMessage(ChatColor.YELLOW
                        + String.format("The past mute of %1$s was overrided for this action", target.getName()));
            }
            Long time = 0L;
            try {
                time = TimeParser.parse(args[1]);
            } catch (NumberFormatException e) {
                time = 0l;
            }
            if (time > 0) {
                if (!sender.hasPermission("banmanager.command.mute.temp")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to create temporal mutes.");
                    return false;
                }

                StringBuilder reason = new StringBuilder();
                if (args.length > 2) {
                    for (int i = 2; i < args.length; i++) {
                        reason.append(args[i]);
                        if (i < args.length) {
                            reason.append(" ");
                        }
                    }
                }
                String banreason = StringUtils.isBlank(reason.toString()) ? "None" : reason.toString();
                plugin.insertTemporalMute(target.getUniqueId(), TimeParser.parse(args[1]), sender.getName(),
                        banreason, false);
                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(ChatColor.DARK_RED + "WARNING: " + ChatColor.RED
                            + String.format("The player %1$s don't played before on this server.", args[0]));
                }
                return true;
            }
            if (!sender.hasPermission("banmanager.command.mute.perma")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to create permanent mutes.");
                return false;
            }
            if (BansConfig.UNPUNISHABLE_PLAYERS.contains(target.getName())) {
                sender.sendMessage(ChatColor.RED + "You can't punish that player.");
                return false;
            }
            StringBuilder reason = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]);
                if (i < args.length) {
                    reason.append(" ");
                }
            }
            String banreason = StringUtils.isBlank(reason.toString()) ? "None" : reason.toString();
            plugin.insertPermanentMute(target.getUniqueId(), sender.getName(), banreason, false);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.DARK_RED + "WARNING:" + ChatColor.RED
                        + String.format("The player %1$s don't played before on this server.", args[0]));
            }
            return true;
        }

    }
}
