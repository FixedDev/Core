package us.sparknetwork.core.commands.chat;

import org.bukkit.command.CommandSender;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.JavaUtils;
import us.sparknetwork.util.TimeUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CommandSetChatDelay extends CommandModule {

    public CommandSetChatDelay() {
        super("setchatdelay", 0, 1, "Usage: /(command) <seconds>", Arrays.asList("scd", "slowcdelay", "schatdelay", "scdelay", "slowchatd"));
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        long time = TimeUnit.SECONDS.toMillis(5);
        if (args.length == 1 && JavaUtils.parse(args[0]) >= 0) {
            time = JavaUtils.parse(args[0]);
        }
        CorePlugin.getPlugin().getServerHandler().setChatSlowedDelay((int) time / 1000);
        CorePlugin.getPlugin().getServerHandler().saveServerData();
        broadcastCommandMessage(sender, CoreConstants.YELLOW + String.format("%1$s setted chat delay to %2$s.", sender.getName(), TimeUtils.getMSG(time)));
        return true;
    }

}
