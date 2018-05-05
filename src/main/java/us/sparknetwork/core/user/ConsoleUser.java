package us.sparknetwork.core.user;

import java.util.UUID;

public class ConsoleUser extends Participator {

    private static UUID consoleuuid = UUID.fromString("02ab1ca0-3bd1-432f-99d1-18a9c1426692");

    public ConsoleUser() {
        super(consoleuuid, "CONSOLE");
    }

}
