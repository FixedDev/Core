package us.sparknetwork.core.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;

public class CommandWorld extends CommandModule {

    public CommandWorld() {
        super("world", 1, 1, "Usage /(command) <world>");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        World world = Bukkit.getServer().getWorld(args[0]);
        if (world == null) {
            sender.sendMessage(CoreConstants.GOLD + String.format("World with name %1$s not found.", args[0]));
            return true;
        }
        player.teleport(world.getSpawnLocation());
        broadcastCommandMessage(sender,
                String.format(
                        (CoreConstants.YELLOW + "Sucessfully teleported to the world " + ChatColor.AQUA + "%1$s(%2$s)"
                                + CoreConstants.YELLOW + "."),
                        world.getName(), WordUtils.capitalizeFully(world.getEnvironment().toString())));
        return true;
    }

}
