package us.sparknetwork.core.commands.essentials;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.util.JavaUtils;
import us.sparknetwork.util.PlayerUtils;

public class CommandSpeed extends CommandModule {

    @SuppressWarnings("deprecation")
    public CommandSpeed() {
        super("speed", 3, 3, "Usage /<command> <player> <fly|walk> <speed>");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player player = PlayerUtils.getPlayer(args[0]);
        String mode = args[1];
        float speed = JavaUtils.tryParseFloat(args[2]);
        if (speed > 10f)
            speed = 10f;
        if (speed < 0f)
            speed = 0f;
        if (player == null) {
            sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
            return true;
        }
        if (mode.equalsIgnoreCase("fly")) {
            player.setFlySpeed(speed / 10);
            broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Sucessfully set fly speed to %1$s.", speed));
            return true;
        }
        if (mode.equalsIgnoreCase("walk")) {
            player.setWalkSpeed(speed / 10);
            broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Sucessfully set walk speed to %1$s.", speed));
            return true;
        }
        return false;
    }

}
