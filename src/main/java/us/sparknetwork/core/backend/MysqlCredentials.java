package us.sparknetwork.core.backend;

import lombok.Getter;

public class MysqlCredentials {

    @Getter
    private String hostname;
    @Getter
    private int port;
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    private String databaseName;

    public MysqlCredentials(String hostname, int port, String username, String password, String databaseName) {
        super();
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
    }

}
