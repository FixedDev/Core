package us.sparknetwork.api.command;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class CommandModule extends Command {

    @Getter
    public final int minArgs;
    @Getter
    public final int maxArgs;
    protected String nextCommandLabel;

    public CommandModule(String label, int minArgs, int maxArgs, String usage, String alias) {
        super(label);
        this.setAliases(ImmutableList.of(alias));
        this.setUsage(usage);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    public CommandModule(String label, int minArgs, int maxArgs, String usage, List<String> alliases) {
        super(label);
        this.setAliases(alliases);
        this.setUsage(usage);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    public CommandModule(String label, int minArgs, int maxArgs, String usage) {
        super(label);
        this.setUsage(usage);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    public CommandModule(String label, int minArgs, int maxArgs, List<String> alliases) {
        super(label);
        this.setAliases(alliases);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    public CommandModule(String label, int minArgs, int maxArgs) {
        super(label);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    public CommandModule(String label, int minArgs, String alias) {
        super(label);
        this.setAliases(ImmutableList.of(alias));
        this.minArgs = minArgs;
        this.maxArgs = -1;
    }

    public CommandModule(String label, int minArgs, List<String> aliases) {
        super(label);
        this.setAliases(aliases);
        this.minArgs = minArgs;
        this.maxArgs = -1;
    }

    public CommandModule(String label, int minArgs) {
        super(label);
        this.minArgs = minArgs;
        this.maxArgs = -1;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        boolean sucess = false;
        this.nextCommandLabel = label;
        if (!testPermission(sender))
            return true;
        try {
            if (maxArgs == -1) {
                if (args.length >= minArgs) {
                    sucess = run(sender, args);
                }
            }
            if (args.length >= minArgs && args.length <= maxArgs) {
                sucess = run(sender, args);
            }
        } catch (Throwable e) {
            throw new CommandException("Unhandled exception executing command " + label, e);
        }

        if (sucess == false && this.getUsage().length() > 0) {
            for (String line : this.getUsage().replace("(command)", label).replace("<command>", label).split("\n")) {
                sender.sendMessage(line);
            }
        }
        return sucess;
    }

    public abstract boolean run(CommandSender sender, String[] args);

}
