package us.sparknetwork.core.commands;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import us.sparknetwork.api.command.CommandHandler;
import us.sparknetwork.api.command.CommandModule;

public class ReflectionCommandHandler implements CommandHandler {

    CommandMap commandmap;
    JavaPlugin plugin;
    Map<String, CommandModule> cmdlist;

    public ReflectionCommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        cmdlist = new HashMap<>();
        try {
            final Field bukkitCommandMap = plugin.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandmap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
        	e.printStackTrace();
            plugin.getLogger().severe(
                    "Failed to get CommandMap disabling ReflectionCommandHandler trying to enable alternate command handlers");
        }
    }

    @Override
	public boolean register(CommandModule command) {
        if (commandmap == null) {
            return false;
        }
        if (command == null) {
            return false;
        }
        cmdlist.put(command.getName(), command);
        commandmap.register(plugin.getName(), command);
        return true;
    }

    public boolean unregisterAll() {
        if (commandmap == null) {
            return false;
        }
        commandmap.clearCommands();
        return true;
    }

    @Override
	public boolean unregisterPluginCommands() {
        if (commandmap == null) {
            return false;
        }
        for (Entry<String, CommandModule> ent : cmdlist.entrySet()) {
            ent.getValue().unregister(commandmap);
        }
        cmdlist.clear();
        return true;
    }

    @Override
	public boolean registerAll(List<CommandModule> commandlist) {
        if (commandmap == null) {
            return false;
        }
        if (commandlist == null || commandlist.isEmpty()) {
            return false;
        }
        for (CommandModule cmd : commandlist) {
            register(cmd);
        }
        return true;
    }

    public boolean unregister(String cmd) {
        if (commandmap == null) {
            return false;
        }
        cmdlist.get(cmd).unregister(commandmap);
        cmdlist.remove(cmd);
        return true;
    }

    @Override
	public boolean setPermission(CommandModule cmd, String permission) {
        if (commandmap == null) {
            return false;
        }
        if (cmd == null) {
            return false;
        }
        if (permission == null) {
            return false;
        }
        cmd.setPermission(permission);
        return true;
    }

    @Override
	public boolean setPermissionMessageToAll(String message) {
        if (commandmap == null) {
            return false;
        }
        if (message == null) {
            return false;
        }
        SimpleCommandMap map = (SimpleCommandMap) commandmap;
        for (Command cmd : map.getCommands()) {
            cmd.setPermissionMessage(message);
        }
        return true;
    }

    @Override
	public boolean setPermissionMessageToAll(List<CommandModule> commandlist, String message) {
        if (commandmap == null) {
            return false;
        }
        if (message == null) {
            return false;
        }
        for (Command cmd : commandlist) {
            cmd.setPermissionMessage(message);
        }
        return true;
    }
}
