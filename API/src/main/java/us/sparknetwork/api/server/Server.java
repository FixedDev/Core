package us.sparknetwork.api.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Server {

    String name;
    int playersOnline;
    int maxPlayers;
    boolean whitelisted;
    boolean online;

}
