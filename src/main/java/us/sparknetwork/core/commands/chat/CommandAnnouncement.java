package us.sparknetwork.core.commands.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.ServerHandler;

public class CommandAnnouncement extends CommandModule {

    public CommandAnnouncement() {
        super("announcement", 1, -1, "Usage /(command) <delay [seconds] | toggle | add <announce> | list | remove> ", "ann");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        ServerHandler sh = CorePlugin.getPlugin().getServerHandler();
        if (args[0].equalsIgnoreCase("toggle")) {
            if (args.length > 1) {
                return false;
            }
            sh.setAnnouncerEnabled(!sh.isAnnouncerEnabled());
            String mode = sh.isAnnouncerEnabled() ? "enabled" : "disabled";
            CorePlugin.getPlugin().registerSchedulers();
            broadcastCommandMessage(sender, String.format(CoreConstants.YELLOW + "The announcer is now %1$s.", mode));
            return true;
        }
        if (args[0].equalsIgnoreCase("delay")) {
            if (args.length == 1) {
                return false;
            }
            int number;
            try {
                number = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(CoreConstants.GOLD + "You must provide a valid quantity of seconds.");
                return true;
            }
            if (number == 0) {
                sender.sendMessage(CoreConstants.YELLOW + "You must provide a quantity of seconds greater than 0.");
                return true;
            }
            sh.setAnnouncerDelay(number);
            CorePlugin.getPlugin().registerSchedulers();
            broadcastCommandMessage(sender, String.format(CoreConstants.YELLOW + "Set the announcer delay to %1$s seconds.", number));
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 1) {
                return false;
            }
            StringBuilder message = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                message.append(args[i]);
                if (i < args.length) {
                    message.append(" ");
                }
            }
            String announcement = ChatColor.translateAlternateColorCodes('&', message.toString());
            sh.getAnnouncements().add(announcement);
            broadcastCommandMessage(sender, CoreConstants.YELLOW + "Sucessfully added a new announcement into the announcer.");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            if (args.length > 1) {
                return false;
            }
            int index = 0;

            for (String announcement : sh.getAnnouncements()) {
                sender.sendMessage(CoreConstants.YELLOW.toString() + index + ChatColor.DARK_GRAY + ": " + CoreConstants.GRAY + announcement);
                index++;
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2 || args.length > 2) {
                return false;
            }
            int number;
            try {
                number = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(CoreConstants.GOLD + "You must provide a valid integer for an announcement index.");
                return true;
            }
            if (sh.getAnnouncements().size() < number) {
                if (sh.getAnnouncements().size() <= 0) {
                    sender.sendMessage(CoreConstants.GOLD + "There are not announcements to remove, try adding one!");
                    return true;
                }
                sender.sendMessage(CoreConstants.GOLD + "Your announcement index is larger than the announcements available, try using /announcements list.");
                return true;
            }
            String removedAnnouncement = sh.getAnnouncements().remove(number);
            broadcastCommandMessage(sender, CoreConstants.YELLOW + String.format("Sucessfully removed the announcement '%1$s'.", removedAnnouncement));
            return true;
        }
        return false;

    }

}
