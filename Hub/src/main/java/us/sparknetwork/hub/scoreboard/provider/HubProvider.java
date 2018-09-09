package us.sparknetwork.hub.scoreboard.provider;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import us.sparknetwork.hub.HubPlugin;
import us.sparknetwork.hub.scoreboard.SidebarEntry;
import us.sparknetwork.hub.scoreboard.SidebarProvider;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;

public class HubProvider implements SidebarProvider {

	protected static String STRAIGHT_LINE = HubPlugin.STRAIGHT_LINE_DEFAULT.substring(0, 14);

	public String getTitle() {
		return CoreConstants.GOLD + ChatColor.BOLD.toString() + CoreConstants.NAME;
	}

	public SidebarEntry add(String s) {

		if (s.length() < 16) {
			return new SidebarEntry(s);
		}

		if (s.length() > 16 && s.length() < 32) {
			return new SidebarEntry(s.substring(0, 16), s.substring(16, s.length()), "");
		}

		if (s.length() > 32) {
			return new SidebarEntry(s.substring(0, 16), s.substring(16, 32), s.substring(32, s.length()));
		}

		return null;
	}

	public List<SidebarEntry> getLines(Player player) {
		List<SidebarEntry> lines = new ArrayList<>();
		lines.add(add(CoreConstants.GOLD + ChatColor.BOLD.toString() + " » " + CoreConstants.YELLOW + ChatColor.BOLD
				+ "Player" + ChatColor.DARK_GRAY + ":"));
		lines.add(add(ChatColor.WHITE + player.getName()));
		lines.add(add(CoreConstants.GOLD + ChatColor.BOLD.toString() + " » " + CoreConstants.YELLOW + ChatColor.BOLD
				+ "Rank" + ChatColor.DARK_GRAY + ":"));
		lines.add(add(ChatColor.WHITE + CorePlugin.getPerms().getPrimaryGroup(player)));
		lines.add(add(CoreConstants.GOLD + ChatColor.BOLD.toString() + " » " + CoreConstants.YELLOW + ChatColor.BOLD
				+ "Online Players" + ChatColor.DARK_GRAY + ":"));
		lines.add(add(ChatColor.WHITE + (HubPlugin.getPlugin().getPlayersOnline() + "")));
		lines.add(0, new SidebarEntry(ChatColor.GRAY, STRAIGHT_LINE, STRAIGHT_LINE));
		lines.add(add(CoreConstants.YELLOW + CoreConstants.SITE));
		lines.add(lines.size(),
				new SidebarEntry(ChatColor.GRAY, ChatColor.STRIKETHROUGH + STRAIGHT_LINE, STRAIGHT_LINE));
		return lines;
	}

}
