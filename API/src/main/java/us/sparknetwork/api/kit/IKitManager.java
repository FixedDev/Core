package us.sparknetwork.api.kit;

import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IKitManager extends Listener {

    void reloadKitData();

    void saveKitData();

    void removeKit(IKit kit);

    void createKit(IKit kit);

    boolean containsKit(IKit kit);

    Optional<IKit> getKit(UUID kitid);

    Optional<IKit> getKit(String name);

    List<IKit> getKits();

}
