package us.sparknetwork.core.server;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import us.sparknetwork.api.event.ReceiveDataEvent;

/**
 * This class provides a global messager for the network
 * this class can send messages to a player,
 * broadcast messages to a server,
 * execute commands in another servers(included the bungeecord)
 * and send messages to the whole network
 */
public class GlobalMessager {

    public enum MessagerType {
        PLAYERMESSAGE, GLOBALMESSAGE, COMMAND
    }

    private ServerManager manager;

    public GlobalMessager(ServerManager manager) {
        this.manager = manager;
    }

    /**
     * This send a message to the whole network
     *
     * @param message - The message to be sended
     */
    public void broadcastMessageToNetwork(String message) {
        this.broadcastMessageToServer("**", message);
    }

    /**
     * This send a message to a server or to the whole network
     *
     * @param toServer - The destinatary server name definited in his core config
     * @param message  - The message to be sended
     *                 <p>
     *                 The argument toServer can be "**" that means all servers
     */
    public void broadcastMessageToServer(String toServer, String message) {
        Validate.isTrue(StringUtils.isBlank(toServer), "The destinatary server can't be null or empty");
        Validate.isTrue(StringUtils.isBlank(message), "The message can't be null or empty");
        manager.sendData(toServer, "messager", MessagerType.GLOBALMESSAGE.toString(), message);
    }

    /**
     * This send a message to a player or all players
     *
     * @param playerName - The destinatary player
     * @param message    - The message to be sended
     *                   <p>
     *                   The arguments playerName can be "**" that means all players
     */
    public void sendMessageToPlayer(String playerName, String message) {
        Validate.isTrue(StringUtils.isBlank(playerName), "The destinatary player can't be null or empty");
        Validate.isTrue(StringUtils.isBlank(message), "The message can't be null or empty");
        manager.sendData("**", "messager", MessagerType.PLAYERMESSAGE.toString(), playerName, message);
    }

    /**
     * This executes a command in the specified server
     *
     * @param toServer - The destinatary server name definited in his core config
     * @param command  - The command to be executed in the specified server
     *                 <p>
     *                 The argument toServer can be "**" that means all servers
     */
    public void sendCommandToServer(String toServer, String command) {
        Validate.isTrue(StringUtils.isBlank(toServer), "The destinatary server can't be null or empty");
        Validate.isTrue(StringUtils.isBlank(command), "The command to be executed can't be null or empty");
        manager.sendData(toServer, "messager", MessagerType.COMMAND.toString(), command);
    }

    /**
     * This executes a command in the specified server
     *
     * @param command - The command to be executed in the specified server
     */
    public void sendCommandToNetwork(String command) {
        this.sendCommandToServer("**", command);
    }

    public class MessagerListener implements Listener {

        public void messageListener(ReceiveDataEvent e) {
            String[] args = e.getArgs();
            String type = e.getType();
            if (!type.equalsIgnoreCase("messager")) {
                return;
            }
            MessagerType messagerType = MessagerType.valueOf(args[0]);
            switch (messagerType) {
                case PLAYERMESSAGE:
                    String playerName = args[1];
                    String message = args[2];
                    Player target = Bukkit.getPlayer(playerName);
                    if (target == null) {
                        return;
                    }
                    target.sendMessage(message);
                    break;
                case GLOBALMESSAGE:
                    String bcmessage = args[1];
                    Bukkit.broadcastMessage(bcmessage);
                    break;
                case COMMAND:
                    String command = args[1];
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    break;
            }
        }
    }
}
