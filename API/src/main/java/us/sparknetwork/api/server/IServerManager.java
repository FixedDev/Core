package us.sparknetwork.api.server;

public interface IServerManager {

    /**
     * Gets data from redis of the requested server
     * @param server - Requested server name
     * @return A Server information instance
     */
    Server getServerData(String server);

    /**
     * Sends data to a server from redis pubsub
     * @param server - Server to send the information(can be ** for all servers)
     * @param type - Information type(A string because can be extended)
     * @param args - Information arguments
     */
    void sendData(String server, String type, String... args);

    /**
     * Gets the players online of the requested server
     * @param server - Requested server name
     * @return A integer of the online players
     */
    int getPlayersOnline(String server);

    void disableServerManager();

    boolean isEnabled();

}