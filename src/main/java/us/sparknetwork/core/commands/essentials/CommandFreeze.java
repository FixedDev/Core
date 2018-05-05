package us.sparknetwork.core.commands.essentials;

import com.google.common.collect.ImmutableList;
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
import us.sparknetwork.util.PlayerUtils;

public class CommandFreeze extends CommandModule {

    public CommandFreeze(CorePlugin plugin) {
        super("freeze", 1, 1, "Usage /(command) [player]", ImmutableList.of("ss", "screenshare"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        Player player = PlayerUtils.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
            return true;
        }
        IUser us = UserManager.getInstance().getUser(player.getUniqueId());
        us.setFreezed(!us.isFreezed());
        String mode = us.isFreezed() ? "Freezed" : "Unfreezed";
        Command.broadcastCommandMessage(sender,
                CoreConstants.YELLOW + String.format("%1$s the player %2$s", mode, player.getName()));
        return true;
    }

}
