package us.sparknetwork.core.commands.essentials;

import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.user.UserManager;

public class CommandStaffMode extends CommandModule {

    public CommandStaffMode(JavaPlugin plugin) {
        super("staffmode", 0, 0, "Usage: /(command)", ImmutableList.of("modmode", "mod", "staff"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        IUser us = UserManager.getInstance().getUser(((Player) sender).getUniqueId());
        us.toggleStaffMode();
        sender.sendMessage(CoreConstants.YELLOW + String.format("Set the staffmode of %1$s to %2$s.", us.getPlayer().getName(), us.isStaffMode()));
        return true;
    }

}
