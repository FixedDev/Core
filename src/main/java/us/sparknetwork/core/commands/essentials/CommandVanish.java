package us.sparknetwork.core.commands.essentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.user.UserManager;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.user.UserManager;

public class CommandVanish extends CommandModule {

    public CommandVanish(CorePlugin plugin) {
        super("vanish", 0, 0, "Usage /vanish", "v");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        IUser us = UserManager.getInstance().getUser(player.getUniqueId());
        us.setVanished(!us.isVanished());
        Command.broadcastCommandMessage(sender, CoreConstants.YELLOW + String.format("Set the vanish mode of %1$s to %2$s.", sender.getName(), us.isVanished() + ""));
        return true;

    }

}
