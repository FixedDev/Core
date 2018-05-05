package us.sparknetwork.core.commands.chat;

import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.api.event.UserMessageEvent;
import us.sparknetwork.core.user.UserManager;

public class CommandReply extends CommandModule {

    public CommandReply(CorePlugin plugin) {
        super("reply", 1, -1, "/(command) <message...>", ImmutableList.of("r"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        IUser user = UserManager.getInstance().getUser(player.getUniqueId());
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            message.append(args[i]);
            if (i < args.length) {
                message.append(" ");
            }
        }
        if (user.getLastReplier() == null) {
            sender.sendMessage(CoreConstants.YELLOW + "You don't have any player to reply");
            return true;
        }
        Player replier = Bukkit.getPlayer(user.getLastReplier());
        if (replier == null) {
            sender.sendMessage(CoreConstants.YELLOW + "You can't reply to offline players");
            return true;
        }
        IUser target = UserManager.getInstance().getUser(replier.getUniqueId());
        Bukkit.getPluginManager().callEvent(new UserMessageEvent(user, target, message.toString()));
        return true;
    }
}
