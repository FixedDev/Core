package us.sparknetwork.core.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import lombok.Data;

public @Data abstract class CommandArgument {

	protected String description;
	protected String permission;
	protected String[] aliases;
	private String name;

	public CommandArgument(String name, String description) {
		this(name, description, (String) null);
	}

	public CommandArgument(String name, String description, String permission) {
		this(name, description, permission, ArrayUtils.EMPTY_STRING_ARRAY);
	}

	public CommandArgument(String name, String description, String[] aliases) {
		this(name, description, null, aliases);
	}

	public CommandArgument(String name, String description, String permission, String[] aliases) {
		this.name = name;
		this.description = description;
		this.permission = permission;
		this.aliases = Arrays.copyOf(aliases, aliases.length);
	}

	public String[] getAliases() {
		if (this.aliases == null) {
			this.aliases = ArrayUtils.EMPTY_STRING_ARRAY;
		}

		return Arrays.copyOf(this.aliases, this.aliases.length);
	}

	public abstract String getUsage(String p0);

	public abstract boolean onCommand(CommandSender p0, Command p1, String p2, String[] p3);

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return Collections.emptyList();
	}

}