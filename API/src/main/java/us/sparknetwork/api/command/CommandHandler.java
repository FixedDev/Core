package us.sparknetwork.api.command;

import java.util.List;


public interface CommandHandler {

    boolean register(CommandModule command);

    boolean registerAll(List<CommandModule> commandlist);

    boolean setPermission(CommandModule CommandModule, String permission);

    boolean setPermissionMessageToAll(String message);

    boolean setPermissionMessageToAll(List<CommandModule> commandlist, String message);

    boolean unregisterPluginCommands();
}
