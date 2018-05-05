package us.sparknetwork.core.commands.chat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.api.user.IParticipator;
import us.sparknetwork.core.user.UserManager;

public class CommandStaffChat extends CommandModule {

    public CommandStaffChat() {
        super("staffchat", 0, 0, "Usage /staffchat", "sc");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command");
            return true;
        }
        IParticipator user = UserManager.getInstance().getUser(((Player) sender).getUniqueId());
        user.setStaffChat(!user.isStaffChat());
        if (user.isStaffChat()) {
            sender.sendMessage(CoreConstants.YELLOW + "You are now speaking in staffchat");
            return true;
        }
        sender.sendMessage(CoreConstants.YELLOW + "You are now speaking in globalchat");
        return true;
    }

}
