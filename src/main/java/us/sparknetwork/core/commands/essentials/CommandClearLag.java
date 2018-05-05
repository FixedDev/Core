package us.sparknetwork.core.commands.essentials;

import org.bukkit.command.CommandSender;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;

public class CommandClearLag extends CommandModule {

    public CommandClearLag() {
        super("clearlag", 1, 1, "Usage /(command) <seconds>");
        this.setDescription("Changes the time of the clearlag");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        int number;
        try {
            number = Integer.parseInt(args[0]);

        } catch (NumberFormatException e) {
            sender.sendMessage(CoreConstants.GOLD + "You must provide a valid quantity of seconds.");
            return true;
        }
        if (number == 0) {
            sender.sendMessage(CoreConstants.YELLOW + "You must provide a quantity of seconds greater than 0.");
            return true;
        }
        CorePlugin.getPlugin().getServerHandler().setClearLagDelay(number);
        CorePlugin.getPlugin().registerSchedulers();
        broadcastCommandMessage(sender,
                CoreConstants.YELLOW + String.format("Sucessfully set the clearlag delay to %1$s seconds.", number));
        return true;
    }

}
