package us.sparknetwork.basics.command;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import us.sparknetwork.basics.BasicsPlugin;
import us.sparknetwork.basics.ConfigurationService;
import us.sparknetwork.basics.schedulers.KickMaintenanceRunnable;

public class GMaintenanceCommand extends Command {

	public GMaintenanceCommand() {
		super("gmaintenance");
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		BasicsPlugin.getPlugin().getServersHandler()
				.setGlobalMaintenance(!BasicsPlugin.getPlugin().getServersHandler().isGlobalMaintenance());
		sender.sendMessage(ConfigurationService.COLOR_1
				+ String.format("Sucessfully set the maintenance mode of the network to %1$s.",
						BasicsPlugin.getPlugin().getServersHandler().isGlobalMaintenance()));
		BasicsPlugin.getPlugin().getConfig().set("network-maintenance",
				BasicsPlugin.getPlugin().getServersHandler().isGlobalMaintenance());
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(BasicsPlugin.getPlugin().getConfig(),
					new File(BasicsPlugin.getPlugin().getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		new KickMaintenanceRunnable().start();

	}

}
