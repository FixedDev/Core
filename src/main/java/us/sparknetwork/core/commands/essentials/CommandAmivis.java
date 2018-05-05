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
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.user.UserManager;

public class CommandAmivis extends CommandModule {

    public CommandAmivis(JavaPlugin plugin) {
        super("amivis", 0, 0, "Usage: /(command)", ImmutableList.of("iaminvisible", "iaminvis"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        IUser user = UserManager.getInstance().getUser(player.getUniqueId());
        String mode = user.isVanished() ? "enabled" : "disabled";
        sender.sendMessage(CoreConstants.YELLOW + String.format("You vanish mode is now %1$s.", mode));
        return true;
    }

}
