package us.sparknetwork.core.commands;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.util.BukkitUtils;

import java.io.Serializable;
import java.util.*;

public class CommandWrapper implements CommandExecutor, TabCompleter {
    private final Collection<CommandArgument> arguments;

    public CommandWrapper(final Collection<CommandArgument> arguments) {
        this.arguments = arguments;
    }

    public static void printUsage(final CommandSender sender, final String label, final Collection<CommandArgument> arguments2) {
        sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(CoreConstants.GOLD + ChatColor.BOLD.toString() + WordUtils.capitalizeFully(label) + " Help");
        sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        for (final CommandArgument argument : arguments2) {
            final String permission = argument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                sender.sendMessage(CoreConstants.GRAY + argument.getUsage(label) + " - " + argument.getDescription());
            }
        }
        sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    public static CommandArgument matchArgument(final String id, final CommandSender sender, final Collection<CommandArgument> arguments) {
        for (final CommandArgument argument : arguments) {
            final String permission = argument.getPermission();
            if ((permission == null || sender.hasPermission(permission)) && (argument.getName().equalsIgnoreCase(id) || Arrays.asList(argument.getAliases()).contains(id))) {
                return argument;
            }
        }
        return null;
    }

    public static List<String> getAccessibleArgumentNames(final CommandSender sender, final Collection<CommandArgument> arguments) {
        final List<String> results = new ArrayList<String>();
        for (final CommandArgument argument : arguments) {
            final String permission = argument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                results.add(argument.getName());
            }
        }
        return results;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1) {
            printUsage(sender, label, this.arguments);
            return true;
        }
        final CommandArgument argument = matchArgument(args[0], sender, this.arguments);
        if (argument == null) {
            printUsage(sender, label, this.arguments);
            return true;
        }
        return argument.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        List<String> results;
        if (args.length == 1) {
            results = getAccessibleArgumentNames(sender, this.arguments);
        } else {
            final CommandArgument argument = matchArgument(args[0], sender, this.arguments);
            if (argument == null) {
                return Collections.emptyList();
            }
            results = argument.onTabComplete(sender, command, label, args);
            if (results == null) {
                return null;
            }
        }
        return BukkitUtils.getCompletions(args, results);
    }

    public static class ArgumentComparator implements Comparator<CommandArgument>, Serializable {

        private static final long serialVersionUID = 158627711205120192L;

        @Override
        public int compare(final CommandArgument primaryArgument, final CommandArgument secondaryArgument) {
            return secondaryArgument.getName().compareTo(primaryArgument.getName());
        }
    }
}
