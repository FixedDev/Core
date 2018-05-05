package us.sparknetwork.core.commands.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.user.IParticipator;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.user.UserManager;

import java.util.Arrays;

public class CommandTogglePm extends CommandModule {

    public CommandTogglePm(CorePlugin plugin) {
        super("toggleprivatemessages", 0, 0, "Usage /<command>", Arrays.asList("togglepm", "togglemsg", "tpm", "tmsg"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Player pl = (Player) sender;
        IParticipator us = UserManager.getInstance().getUser(pl.getUniqueId());
        us.setToggledMessages(!us.isToggledMessages());
        String mode = us.isToggledMessages() ? "on" : "off";
        sender.sendMessage(CoreConstants.YELLOW + String.format("Set your global chat visibility to %1$s.", mode));
        return true;

    }

}
