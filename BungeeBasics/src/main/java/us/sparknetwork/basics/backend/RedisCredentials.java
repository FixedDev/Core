package us.sparknetwork.basics.backend;

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
