package us.sparknetwork.core.commands.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.api.user.IParticipator;
import us.sparknetwork.core.user.UserManager;

public class CommandSocialSpy extends CommandModule {

    public CommandSocialSpy() {
        super("socialspy", 0, 0, "Usage /(command)");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        IParticipator user = UserManager.getInstance().getUser(((Player) sender).getUniqueId());
        user.setSocialSpy(!user.isSocialSpy());
        broadcastCommandMessage(sender,
                CoreConstants.YELLOW + String.format("Set SocialSpy mode of %1$s to %2$s.", sender.getName(), user.isSocialSpy()));
        return true;
    }

}
