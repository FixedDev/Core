package us.sparknetwork.core.backend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class RedisCredentials {

    private String hostname;
    private int port;
    private String password;

}
