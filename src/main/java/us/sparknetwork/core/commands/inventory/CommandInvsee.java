package us.sparknetwork.core.commands.inventory;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.util.PlayerUtils;

public class CommandInvsee extends CommandModule {


    public CommandInvsee(JavaPlugin plugin) {
        super("invsee", 1, 1, "Usage: /(command) <player>", ImmutableList.of("seeinv", "seeinventory"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED+"Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        Player target = PlayerUtils.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(String.format(CoreConstants.PLAYER_WITH_NAME_OR_UUID, args[0]));
            return true;
        }
        Inventory inv = target.getInventory();
        player.openInventory(inv);
        Command.broadcastCommandMessage(sender,
                CoreConstants.YELLOW + String.format("Seeing the inventory of %1$s.", target.getName()));
        return true;
    }

}
