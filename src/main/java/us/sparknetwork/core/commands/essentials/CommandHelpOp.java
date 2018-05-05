package us.sparknetwork.core.commands.essentials;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.sparknetwork.api.command.CommandModule;
import us.sparknetwork.core.CoreConstants;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.util.ChatColorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandHelpOp extends CommandModule {

    public List<UUID> delayedplayers = new ArrayList<>();

    public CommandHelpOp() {
        super("helpop", 1, 4096, "Usage /(command) <message>", "request");
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED+"Only players can execute this command");
            return true;
        }
        Player player = (Player) sender;
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            message.append(args[i]);
            if (i < args.length) {
                message.append(" ");
            }
        }
        if (delayedplayers.contains(player.getUniqueId())) {
            sender.sendMessage(CoreConstants.GOLD + "You have a delay of 5 minutes between requests.");
            return true;
        }
        sendMessageToStaff((Player) sender, message.toString());
        CorePlugin.getPlugin().getServerManager().sendData("**","Helpop", sender.getName(), message.toString());
        sender.sendMessage(CoreConstants.YELLOW + "Sucessfully requested help to staff.");
        delayedplayers.add(((Player) sender).getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                delayedplayers.remove(((Player) sender).getUniqueId());
            }

        }.runTaskLater(CorePlugin.getPlugin(), 5 * 20 * 60);
        return true;
    }

    public void sendMessageToStaff(Player player, String str) {
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission("core.command.helpop.receive")) {
                TextComponent doubledots = new TextComponent(":");
                doubledots.setColor(ChatColor.DARK_GRAY);
                doubledots.setBold(false);
                TextComponent tc = new TextComponent(String.format("%1$s", player.getName()));
                TextComponent tc1 = new TextComponent(" is requesting help");
                TextComponent tc2 = new TextComponent("(Click here to teleport to him)");
                TextComponent message = new TextComponent(" " + str);
                message.setColor(ChatColor.LIGHT_PURPLE);
                message.setBold(false);
                tc2.setClickEvent(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tp %1$s", player.getName())));
                tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Teleport to the player").create()));
                tc.setColor(ChatColorUtils.toBungee(CoreConstants.GOLD));
                tc.setBold(true);
                tc1.setColor(ChatColorUtils.toBungee(CoreConstants.YELLOW));
                tc1.setBold(false);
                tc2.setColor(ChatColorUtils.toBungee(CoreConstants.GRAY));
                tc2.setBold(false);
                tc.addExtra(tc1);
                tc.addExtra(tc2);
                tc.addExtra(doubledots);
                tc.addExtra(message);
                staff.spigot().sendMessage(tc);
            }
        }
    }

}
