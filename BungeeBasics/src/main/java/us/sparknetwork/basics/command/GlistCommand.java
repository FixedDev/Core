package us.sparknetwork.basics.command;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;
import us.sparknetwork.basics.BasicsPlugin;
import us.sparknetwork.basics.ConfigurationService;

public class GlistCommand extends Command {

	public GlistCommand() {
		super("glist");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			BaseComponent bc1 = new TextComponent(String.format(
					"There are %1$s players in the network, use /glist showall to see players in all the servers.",
					BasicsPlugin.getPlugin().getProxy().getPlayers().size()));
			bc1.setColor(ConfigurationService.COLOR_1);
			sender.sendMessage(bc1);
			return;
		}
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("showall")) {
				this.sendGlistAll(sender);
				return;
			} else {
				BaseComponent bc1 = new TextComponent(String.format(
						"There are %1$s players in the network, use /glist showall to see players in all the servers.",
						BasicsPlugin.getPlugin().getProxy().getPlayers().size()));
				bc1.setColor(ConfigurationService.COLOR_1);
				sender.sendMessage(bc1);
				return;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void sendGlistAll(CommandSender sender) {
		for (Entry<String, ServerInfo> ent : BasicsPlugin.getPlugin().getProxy().getServers().entrySet()) {
			Set<String> playerlist = new HashSet<>();
			ent.getValue().getPlayers().forEach(p -> {
				playerlist.add(p.getName());
			});
			sender.sendMessage(ConfigurationService.COLOR_1 + ent.getValue().getName() + ChatColor.GOLD + "["
					+ ConfigurationService.COLOR_1 + playerlist.size() + ChatColor.GOLD + "]" + ChatColor.DARK_GRAY
					+ ": " + ConfigurationService.COLOR_1 + StringUtils.join(playerlist, ","));

		}
	}
}
