package us.sparknetwork.core.commands.essentials;

import com.google.common.net.InetAddresses;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.user.IParticipator;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.user.User;
import us.sparknetwork.core.user.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandIpHistory extends CommandModule {

    public CommandIpHistory() {
        super("iphistory", 1, 2, "Usage /(command) <address|player>");
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("address") || args[0].equalsIgnoreCase("ip")
                    || args[0].equalsIgnoreCase("ipaddress")) {
                sender.sendMessage(ChatColor.RED + "/" + this.getLabel() + " " + args[0] + " <ipAddress>");
                return true;
            }
            if (args[0].equalsIgnoreCase("player")) {
                sender.sendMessage(ChatColor.RED + "/" + this.getLabel() + " " + args[0] + " <player>");
                return true;
            }
            return false;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("address") || args[0].equalsIgnoreCase("ip")
                    || args[0].equalsIgnoreCase("ipaddress")) {
                String address = args[1];
                if (!InetAddresses.isInetAddress(address)) {
                    sender.sendMessage(
                            CoreConstants.GOLD + String.format("Invalid ip %1$s, you must provide a valid one.", ChatColor.GREEN + address + CoreConstants.GOLD));
                    return true;
                }
                List<String> playerlist = new ArrayList<>();
                for (IParticipator pa : UserManager.getInstance().getUserMap().values()) {
                    User us = (User) pa;
                    if (us.getIpHistory().contains(address)) {
                        playerlist.add(us.getNick());
                    }
                }
                if (playerlist.isEmpty()) {
                    sender.sendMessage(CoreConstants.GOLD + String.format("Players with address %1$s not found.",
                            ChatColor.GREEN + address + CoreConstants.GOLD));
                    return true;
                }
                String[] players = new String[playerlist.size()];
                players = playerlist.toArray(players);
                sender.sendMessage(CoreConstants.GOLD
                        + String.format("IP Address: %1$s is shared by:\n" + StringUtils.join(players, ",\n"),
                        CoreConstants.YELLOW + address + CoreConstants.GOLD));
                return true;
            }
            if (args[0].equalsIgnoreCase("player")) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                IUser us = UserManager.getInstance().getUser(player.getUniqueId());
                if (us == null) {
                    sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[1]));
                    return true;
                }
                sender.sendMessage(
                        CoreConstants.GOLD + String.format("IP Addresses utilized by %1$s:", player.getName()));
                for (String address : us.getIpHistory()) {
                    sender.sendMessage(this.getPlayers(address));
                }
                return true;
            }
            return false;
        }
        return false;

    }

    public String getPlayers(String address) {
        if (CorePlugin.getPlugin().isMongo()) {
            List<User> users = CorePlugin.getPlugin().getDatastore().find(User.class).field("ipHistory").hasThisOne(address).asList();
            List<String> playerList = users.stream().map(User::getUniqueId).map(user -> Bukkit.getOfflinePlayer(user).getName()).collect(Collectors.toList());
            String[] players = playerList.toArray(new String[playerList.size()]);
            return String.format((CoreConstants.GRAY + "%1$s:" + CoreConstants.YELLOW + " [%2$s]"), address,
                    StringUtils.join(players, ","));
        } else {
            List<String> playerlist = new ArrayList<>();
            UserManager.getInstance().getUserMap().values().stream().map(u -> (IUser) u).filter(us -> us.getIpHistory().contains(address)).forEach(us -> playerlist.add(us.getNick()));
            String[] players = playerlist.toArray(new String[playerlist.size()]);
            return String.format((CoreConstants.GRAY + "%1$s:" + CoreConstants.YELLOW + " [%2$s]"), address,
                    StringUtils.join(players, ","));
        }
    }

}
