package us.sparknetwork.core.commands.essentials;

import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.CoreConstants;

import java.util.Arrays;
import java.util.TimeZone;

public class CommandRam extends CommandModule {

    public CommandRam() {
        super("ram", 0, 0, "Usage: /<command>", Arrays.asList("gc", "lagg"));
    }


    private static final FastDateFormat FORMAT = FastDateFormat.getInstance("E MMM dd h:mm:ssa z yyyy", TimeZone.getTimeZone("America/Monterrey"));

    @Override
    public boolean run(CommandSender sender, String[] args) {
        sender.sendMessage(CoreConstants.GOLD + "TPS: " + CoreConstants.YELLOW + Bukkit.getServer().spigot().getTPS()[3]);
        sender.sendMessage(CoreConstants.GOLD + "Server Time: " + CoreConstants.YELLOW + FORMAT.format(System.currentTimeMillis()));
        sender.sendMessage(CoreConstants.GOLD + "Total Ram: " + CoreConstants.YELLOW + Runtime.getRuntime().totalMemory() / 1024 / 1024);
        sender.sendMessage(CoreConstants.GOLD + "Max Ram: " + CoreConstants.YELLOW + Runtime.getRuntime().maxMemory() / 1024 / 1024);
        sender.sendMessage(CoreConstants.GOLD + "Free Ram: " + CoreConstants.YELLOW + Runtime.getRuntime().freeMemory() / 1024 / 1024);
        sender.sendMessage(CoreConstants.GOLD + "Max Players: " + CoreConstants.YELLOW + CorePlugin.getPlugin().getServerHandler().getMaxPlayers());
        sender.sendMessage(CoreConstants.GOLD + "Entities:");
        Bukkit.getWorlds().forEach(world -> {
            sender.sendMessage(CoreConstants.GOLD + world.getName() + ": " + CoreConstants.YELLOW + world.getEntities().size());
        });
        return true;
    }
}

