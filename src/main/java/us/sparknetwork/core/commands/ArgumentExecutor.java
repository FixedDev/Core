package us.sparknetwork.core.commands;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.util.BukkitUtils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ArgumentExecutor extends CommandModule implements TabCompleter {
    protected final List<CommandArgument> arguments;
    protected final String label;

    public ArgumentExecutor(final String label) {
        super(label, 0);
        this.arguments = new ArrayList<>();
        this.label = label;
    }

    public static void printUsage(final CommandSender sender, final String label,
                                  final Collection<CommandArgument> arguments2) {
        sender.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(CoreConstants.GOLD + net.md_5.bungee.api.ChatColor.BOLD.toString() + WordUtils.capitalizeFully(label) + " Help");
        sender.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        for (final CommandArgument argument : arguments2) {
            final String permission = argument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                sender.sendMessage(CoreConstants.GRAY + argument.getUsage(label) + " - " + argument.getDescription());
            }
        }
        sender.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    public static List<String> getCompletions(final String[] args, final List<String> input) {
        return getCompletions(args, input, 80);
    }

    public static List<String> getCompletions(final String[] args, final List<String> input, final int limit) {
        Preconditions.checkNotNull((Object) args);
        Preconditions.checkArgument(args.length != 0);
        final String argument = args[args.length - 1];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length()))
                .limit(limit).collect(Collectors.toList());
    }

    public boolean containsArgument(final CommandArgument argument) {
        return this.arguments.contains(argument);
    }

    public void addArgument(final CommandArgument argument) {
        this.arguments.add(argument);
    }

    public void removeArgument(final CommandArgument argument) {
        this.arguments.remove(argument);
    }

    public CommandArgument getArgument(final String id) {
        for (final CommandArgument argument : this.arguments) {
            final String name = argument.getName();
            if (name.equalsIgnoreCase(id) || Arrays.asList(argument.getAliases()).contains(id.toLowerCase())) {
                return argument;
            }
        }
        return null;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public List<CommandArgument> getArguments() {
        return ImmutableList.copyOf(this.arguments);
    }

    @Override
    public boolean run(final CommandSender sender, final String[] args) {
        if (args.length < 1) {
            Collection<CommandArgument> arguments = new HashSet<>();
            arguments.addAll(this.arguments);
            printUsage(sender, label, arguments);
            return true;
        }
        final CommandArgument argument2 = this.getArgument(args[0]);
        final String permission2 = argument2 == null ? null : argument2.getPermission();
        if (argument2 == null || permission2 != null && !sender.hasPermission(permission2)) {
            sender.sendMessage(
                    ChatColor.RED + WordUtils.capitalizeFully(this.label) + " sub-command " + args[0] + " not found.");
            return true;
        }
        argument2.onCommand(sender, this, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label,
                                      final String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length < 2) {
            for (final CommandArgument argument : this.arguments) {
                final String permission = argument.getPermission();
                if (permission == null || sender.hasPermission(permission)) {
                    results.add(argument.getName());
                }
            }
        } else {
            final CommandArgument argument2 = this.getArgument(args[0]);
            if (argument2 == null) {
                return results;
            }
            final String permission2 = argument2.getPermission();
            if (permission2 == null || sender.hasPermission(permission2)) {
                results = argument2.onTabComplete(sender, command, label, args);
                if (results == null) {
                    return null;
                }
            }
        }
        return getCompletions(args, results);
    }
}
