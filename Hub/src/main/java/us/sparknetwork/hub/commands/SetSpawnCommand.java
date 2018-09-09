package us.sparknetwork.hub.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.PersistableLocation;

public class SetSpawnCommand extends CommandModule {

	public SetSpawnCommand() {
		super("setspawn", 0, 0, "Usage /(command)");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean run(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED+"Only players can execute this command.");
			return true;
		}
		CorePlugin.getInstance().getSpawnManager().setSpawn(new PersistableLocation(((Player) sender).getLocation()));
		broadcastCommandMessage(sender, CoreConstants.YELLOW+"Sucessfully set a new spawn location.");
		return true;
	}

}
