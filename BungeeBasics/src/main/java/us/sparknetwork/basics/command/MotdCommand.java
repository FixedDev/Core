package us.sparknetwork.basics.command;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import us.sparknetwork.basics.BasicsPlugin;
import us.sparknetwork.basics.ConfigurationService;

public class MotdCommand extends Command {

	public MotdCommand() {
		super("motd");
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("basics.command.motd")) {
			sender.sendMessage(ChatColor.RED + "Insufficient permissions to execute this command.");
			return;
		}
		if (args.length >= 1) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				builder.append(args[i]);
				builder.append(" ");
			}
			ConfigurationService.MOTD = ChatColor.translateAlternateColorCodes('&', builder.toString());
			BasicsPlugin.getPlugin().getConfig().set("motd", ConfigurationService.MOTD);
			try {
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(BasicsPlugin.getPlugin().getConfig(),
						new File(BasicsPlugin.getPlugin().getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			sender.sendMessage(ConfigurationService.COLOR_1 + "Sucessfully changed the motd.");
			return;

		}
		sender.sendMessage(ChatColor.RED + "Specify a valid motd!");
	}

}
