package us.sparknetwork.core.commands.essentials;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.PlayerUtils;

public class CommandFly extends CommandModule {

    public CorePlugin plugin;

    public CommandFly(CorePlugin plugin) {
        super("fly", 0, 1, "Usage /(command) [player]", ImmutableList.of("flight"));
        this.plugin = plugin;
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && args.length < 1) {
            return false;
        }
        if (args.length == 0) {
            Player p = (Player) sender;
            p.setAllowFlight(!p.getAllowFlight());
            Command.broadcastCommandMessage(sender, CoreConstants.YELLOW
                    + String.format("Set fly mode of %1$s to %2$s", p.getName(), p.getAllowFlight()));
            return true;
        }
        if (args.length == 1) {
            Player p = PlayerUtils.getPlayer(args[0]);
            if (p == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
                return true;
            }
            if (!sender.hasPermission("core.command.fly.others")) {
                sender.sendMessage(this.getPermissionMessage());
                return true;
            }
            p.setAllowFlight(!p.getAllowFlight());
            Command.broadcastCommandMessage(sender, CoreConstants.YELLOW
                    + String.format("Set fly mode of %1$s to %2$s", p.getName(), p.getAllowFlight()));
            return true;
        }
        return false;

    }

}
