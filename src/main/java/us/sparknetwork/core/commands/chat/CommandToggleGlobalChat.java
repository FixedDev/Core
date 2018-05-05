package us.sparknetwork.core.commands.chat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.user.IParticipator;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.user.UserManager;

public class CommandToggleGlobalChat extends CommandModule {

    public CommandToggleGlobalChat() {
        super("toggleglobalchat", 0, 0, "/(command)", "tgc");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can be used only by players");
            return true;
        }
        IParticipator user = UserManager.getInstance().getUser(((Player) sender).getUniqueId());
        user.setGlobalChatVisible(!user.isGlobalChatVisible());
        String mode = user.isGlobalChatVisible() ? "on" : "off";
        sender.sendMessage(CoreConstants.YELLOW + String.format("Set your global chat visibility to %1$s.", mode));
        return true;
    }

}
