package us.sparknetwork.core.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.util.PlayerUtils;

public class CommandSudo extends CommandModule {

    public CommandSudo() {
        super("sudo", 2, -1, "Usage /(command) <player> <command args...>");
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player target = PlayerUtils.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
            return true;
        }
        StringBuilder command = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            command.append(args[i] + " ");
        }
        if (target.hasPermission("core.command.sudo.exempt")) {
            sender.sendMessage(CoreConstants.GOLD + String.format("The player %1$s can't be sudoed.", target.getName()));
            return true;
        }
        Bukkit.dispatchCommand(target, command.toString());
        sender.sendMessage(
                CoreConstants.YELLOW + String.format("Sucessfully forced to execute a command to %1$s.", target.getName()));
        return true;
    }

}
