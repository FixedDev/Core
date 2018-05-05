package us.sparknetwork.core.commands.chat;

import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.user.IParticipator;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.user.UserManager;

public class CommandToggleStaffChat extends CommandModule {

    public CommandToggleStaffChat() {
        super("togglestaffchat", 0, 0, "/(command)", ImmutableList.of("tsc", "togglesc"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        IParticipator user = UserManager.getInstance().getUser(((Player) sender).getUniqueId());
        user.setStaffChatVisible(!user.isStaffChatVisible());
        String mode = user.isStaffChatVisible() ? "on" : "off";
        sender.sendMessage(CoreConstants.YELLOW + String.format("Set your staff chat visibility to %1$s.", mode));
        return true;
    }

}
