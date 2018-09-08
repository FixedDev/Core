package us.sparknetwork.basics.command;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import us.sparknetwork.basics.BasicsPlugin;
import us.sparknetwork.basics.ConfigurationService;

public class ServerCommand extends Command {

	public ServerCommand() {
		super("server");
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command.");
			return;
		}
		if (args.length < 1) {
			sender.sendMessage(ConfigurationService.COLOR_1 + String.format("You're currently in the server %1$s.",
					((ProxiedPlayer) sender).getServer().getInfo().getName()));
			return;
		}
		ServerInfo server = BasicsPlugin.getPlugin().getProxy().getServerInfo(args[0]);
		if (server == null) {
			sender.sendMessage(ChatColor.GOLD + String.format("Server with name %1$s not found.", args[0]));
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		player.sendMessage(ConfigurationService.COLOR_1
				+ String.format("Sending to the server %1$s wait %2$s seconds.", args[0], 3));
		BasicsPlugin.getPlugin().getProxy().getScheduler().schedule(BasicsPlugin.getPlugin(), () -> {
			player.connect(server);
		}, 3, TimeUnit.SECONDS);
	}

}
