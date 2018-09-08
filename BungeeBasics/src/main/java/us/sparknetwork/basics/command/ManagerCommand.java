package us.sparknetwork.basics.command;

import java.util.Map;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import us.sparknetwork.basics.BasicsPlugin;
import us.sparknetwork.basics.ConfigurationService;
import us.sparknetwork.basics.bungee.BungeeServerInformation;

public class ManagerCommand extends Command {

	public ManagerCommand() {
		super("servermanager");
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("basics.command.servermanager")) {
			sender.sendMessage(ChatColor.RED + "You don't have permissions to execute this command.");
			return;
		}
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("add")) {
				if (args.length < 3) {
					sender.sendMessage(ChatColor.RED + "Usage: /servermanager add <name> <address:port>");
					return;
				}
				String name = args[1];
				BasicsPlugin.getPlugin().getServersHandler().addServer(name, args[2]);
				sender.sendMessage(ConfigurationService.COLOR_1 + "Sucessfully added a server to the bungeecord.");
				return;
			}
			if (args[0].equalsIgnoreCase("remove")) {
				if (args.length < 3) {
					sender.sendMessage(ChatColor.RED + "Usage: /servermanager remove <name>");
					return;
				}
				String name = args[1];
				Map<String, BungeeServerInformation> map = BasicsPlugin.getPlugin().getServersHandler().servers;
				if (!map.containsKey(name)) {
					sender.sendMessage(ChatColor.RED + String.format("Server with name %1$s not found.", name));
					return;
				}
				BasicsPlugin.getPlugin().getServersHandler().removeServer(name);
				sender.sendMessage(ConfigurationService.COLOR_1 + "Sucessfully removed a server from the bungeecord.");
				return;
			}
		}
		sender.sendMessage(ChatColor.RED + "Usage: /servermanager <add|remove>");
		return;
	}

}
