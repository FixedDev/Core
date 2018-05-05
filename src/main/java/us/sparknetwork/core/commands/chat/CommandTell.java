package us.sparknetwork.core.commands.chat;

import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.api.event.UserMessageEvent;
import us.sparknetwork.core.user.UserManager;

public class CommandTell extends CommandModule implements Listener{

    public CommandTell(CorePlugin plugin) {
        super("tell", 2, -1, "Usage: /<command> <target> <message...> ", ImmutableList.of("msg", "pm"));
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        IUser user = UserManager.getInstance().getUser(((Player) sender).getUniqueId());

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]);
            if (i < args.length) {
                message.append(" ");
            }
        }
        OfflinePlayer player = Bukkit.getPlayer(args[0]);
        if (!player.isOnline()) {
            sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
            return true;
        }
        IUser target = UserManager.getInstance().getUser(player.getUniqueId());
        Bukkit.getPluginManager().callEvent(new UserMessageEvent(user, target, message.toString()));
        return true;
    }

    @EventHandler
    public void onMessage(UserMessageEvent e) {
        IUser user = e.getFrom();
        IUser target = e.getTo();
        String message = e.getMessage();
        if (!target.isToggledMessages()) {
            user.getPlayer().sendMessage(ChatColor.RED + "The player has the private messages toggled.");
            return;
        }
        target.setLastReplier(user.getUniqueId());
        target.getPlayer().sendMessage(
                (CoreConstants.GRAY + "(From §d%sender%" + CoreConstants.GRAY + ") ")
                        .replace("%sender%", user.getPlayer().getName()) + message);
        user.getPlayer().sendMessage((CoreConstants.GRAY + "(To §d%target%" + CoreConstants.GRAY + ") ").replace("%target%",
                target.getPlayer().getName()) + message);
        Bukkit.getOnlinePlayers().stream().filter(u -> !u.equals(target.getPlayer()) || !u.equals(target.getPlayer())).map(p -> UserManager.getInstance().getUser(p.getUniqueId())).filter(IUser::isSocialSpy).forEach(u -> u.getPlayer().sendMessage(ChatColor.DARK_GRAY + "[" + CoreConstants.GOLD + "SocialSpy" + ChatColor.DARK_GRAY + "]" + String.format(CoreConstants.GRAY + "(%1$s to %2$s) %3$s", ChatColor.LIGHT_PURPLE + user.getPlayer().getName() + CoreConstants.GRAY, ChatColor.LIGHT_PURPLE + target.getPlayer().getName(), CoreConstants.GRAY + message)));
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + CoreConstants.GOLD + "SocialSpy" + ChatColor.DARK_GRAY + "]" + String.format(CoreConstants.GRAY + "(%1$s to %2$s) %3$s", ChatColor.LIGHT_PURPLE + user.getPlayer().getName() + CoreConstants.GRAY, ChatColor.LIGHT_PURPLE + target.getPlayer().getName(), CoreConstants.GRAY + message));
    }


}
