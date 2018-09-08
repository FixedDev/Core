package us.sparknetwork.basics.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import us.sparknetwork.basics.BasicsPlugin;
import us.sparknetwork.basics.ConfigurationService;

public class AlertCommand extends Command {

	public AlertCommand() {
		super("alert");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("basics.command.alert")) {
			sender.sendMessage(ChatColor.RED + "Insufficient permissions to execute this command.");
			return;
		}
		if (args.length >= 1) {
			if (sender.hasPermission("basics.command.alert.staff")) {
				if(args.length > 2) {
					if(args[0].equalsIgnoreCase("-p")) {
						StringBuilder builder = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
							builder.append(args[i]);
							builder.append(" ");
						}
						BasicsPlugin.getPlugin().getProxy()
						.broadcast(ConfigurationService.COLOR_1 + "<" + ChatColor.DARK_GRAY + "["
								+ ConfigurationService.COLOR_2 + ConfigurationService.NETWORK_NAME + ChatColor.DARK_GRAY
								+ "] " + ConfigurationService.COLOR_2 + sender.getName() + ConfigurationService.COLOR_1
								+ ">" + ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', builder.toString()));
						return;
					}
				}
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < args.length; i++) {
					builder.append(args[i]);
					builder.append(" ");
				}
				BasicsPlugin.getPlugin().getProxy()
						.broadcast(ChatColor.DARK_GRAY + "[" + ConfigurationService.COLOR_2
								+ ConfigurationService.NETWORK_NAME + ChatColor.DARK_GRAY + "] "
								+ ChatColor.translateAlternateColorCodes('&', builder.toString()));
				return;
			}
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				builder.append(args[i]);
				builder.append(" ");
			}
			BasicsPlugin.getPlugin().getProxy()
					.broadcast(ConfigurationService.COLOR_1 + "<" + ChatColor.DARK_GRAY + "["
							+ ConfigurationService.COLOR_2 + ConfigurationService.NETWORK_NAME + ChatColor.DARK_GRAY
							+ "] " + ConfigurationService.COLOR_2 + sender.getName() + ConfigurationService.COLOR_1
							+ ">" + ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', builder.toString()));
			return;

		}
		sender.sendMessage(ChatColor.RED + "Usage /alert [-p] <message>");
	}

}
