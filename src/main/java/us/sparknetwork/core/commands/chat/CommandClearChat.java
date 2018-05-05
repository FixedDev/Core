package us.sparknetwork.core.commands.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CoreConstants;

public class CommandClearChat extends CommandModule {

    public CommandClearChat() {
        super("clearchat", 1, 999, "Usage: /(command) <reason>", "cc");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        StringBuilder reason = new StringBuilder();
        for (String arg : args) {
            reason.append(arg + " ");
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (int i = 1; i < 150; i++) {
                player.sendMessage("");
            }
        }
        Bukkit.broadcastMessage(CoreConstants.YELLOW + "%player% has cleared the chat for %reason%."
                .replace("%player%", sender.getName()).replace("%reason%", reason.toString()));
        return true;
    }

}
