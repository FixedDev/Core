package us.sparknetwork.core.commands.teleport;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.api.user.IUser;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.user.UserManager;

@SuppressWarnings("deprecation")
public class CommandBack extends CommandModule {

    public CommandBack() {
        super("back", 0, 0, "Usage /<command>");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        IUser user = UserManager.getInstance().getUser(player.getUniqueId());
        if (user.getBackLocation() == null) {
            sender.sendMessage(CoreConstants.YELLOW + "You don't have a back location stored.");
            return true;
        }
        ((Player) sender).teleport(user.getBackLocation().toLocation(), TeleportCause.PLUGIN);
        sender.sendMessage(CoreConstants.YELLOW + "Sucessfully teleported to your latest teleport location.");
        return true;
    }

}
