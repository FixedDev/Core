package us.sparknetwork.core.backend;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MongoCredentials {

    @Getter
    private String hostname;
    @Getter
    private int port;
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    private String database;
    
    public boolean isAuthenticable() {
    	return username != null && !username.contentEquals("") && password != null && !password.equals("");
    }

}
