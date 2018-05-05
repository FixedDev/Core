package us.sparknetwork.core.commands.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.JavaUtils;
import us.sparknetwork.util.TimeUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CommandSlowChat extends CommandModule {

    public CommandSlowChat() {
        super("slowchat", 0, 1, "Usage: /(command) <time>", Arrays.asList("slowc", "schat"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        long time = TimeUnit.MINUTES.toMillis(5);
        if (args.length == 1 && JavaUtils.parse(args[0]) >= 0) {
            time = JavaUtils.parse(args[0]);
        }
        CorePlugin.getPlugin().getServerHandler().setChatSlowed(System.currentTimeMillis()+time);
        CorePlugin.getPlugin().getServerHandler().saveServerData();
        Bukkit.broadcastMessage(CoreConstants.YELLOW + String.format("%1$s has slowed the chat for %2$s.", sender.getName(), TimeUtils.getMSG(time)));
        return true;
    }

}
