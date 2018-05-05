package us.sparknetwork.core.commands.essentials;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;

public class CommandGamemode extends CommandModule {

    public CorePlugin plugin;

    public CommandGamemode(CorePlugin plugin) {
        super("gamemode", 1, 2, "Usage /(command) <gamemode> [player]", ImmutableList.of("gm"));
        this.plugin = plugin;
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && args.length < 2) {
            return false;
        }
        if (args.length == 1) {
            Player player = (Player) sender;
            GameMode mode = this.matchGameMode(args[0]);
            if (mode == null) {
                sender.sendMessage(CoreConstants.GOLD + "You must provide a valid gamemode name or id.");
                return true;
            }
            player.setGameMode(mode);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Set gamemode of %1$s to %2$s.", player.getName(),
                            WordUtils.capitalizeFully(mode.toString().toLowerCase())));
            return true;
        }
        if (args.length == 2) {
            if (!sender.hasPermission("core.command.gamemode.others")) {
                sender.sendMessage(this.getPermissionMessage());
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[1]));
                return true;
            }
            GameMode mode = this.matchGameMode(args[0]);
            if (mode == null) {
                sender.sendMessage(CoreConstants.GOLD + "You must provide a valid gamemode name or id.");
                return true;
            }
            target.setGameMode(mode);
            Command.broadcastCommandMessage(sender,
                    CoreConstants.YELLOW + String.format("Set gamemode of %1$s to %2$s.", target.getName(),
                            WordUtils.capitalizeFully(mode.toString().toLowerCase())));
            return true;
        }
        return false;
    }

    private GameMode matchGameMode(String modeString) {
        if (modeString.equalsIgnoreCase("gmc") || modeString.equalsIgnoreCase("egmc") || modeString.contains("creat")
                || modeString.equalsIgnoreCase("1") || modeString.equalsIgnoreCase("c")) {
            return GameMode.CREATIVE;
        } else if (modeString.equalsIgnoreCase("gms") || modeString.equalsIgnoreCase("egms")
                || modeString.contains("survi") || modeString.equalsIgnoreCase("0")
                || modeString.equalsIgnoreCase("s")) {
            return GameMode.SURVIVAL;
        } else if (modeString.equalsIgnoreCase("gma") || modeString.equalsIgnoreCase("egma")
                || modeString.contains("advent") || modeString.equalsIgnoreCase("2")
                || modeString.equalsIgnoreCase("a")) {
            return GameMode.ADVENTURE;
        } else {
            System.out.println("Insufficient arguments!");
        }
        return null;
    }
}
