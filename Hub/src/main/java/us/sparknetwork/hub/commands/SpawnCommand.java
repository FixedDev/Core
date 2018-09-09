package us.sparknetwork.hub.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.commands.CommandModule;

public class SpawnCommand extends CommandModule {

	public SpawnCommand() {
		super("spawn", 0, 0, "Usage /(command)");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		((Player) sender).teleport(CorePlugin.getInstance().getSpawnManager().getLoc());
		sender.sendMessage(CoreConstants.YELLOW+"Sucessfully teleported to the spawn.");
		return true;
	}

}
