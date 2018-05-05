package us.sparknetwork.core.backend.mongo.connection;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.DefaultCreator;
import us.sparknetwork.core.CorePlugin;
import us.sparknetwork.core.backend.MongoCredentials;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MongoConnectionManager {


    private Map<String, Datastore> datastores;
    @Getter
    private Morphia morphia;
    private MongoClient dataSource;
    private MongoCredentials credentials;

    public MongoConnectionManager( MongoCredentials credentials) {
        this.credentials = credentials;
        this.init();
        this.morphia.getMapper().getOptions().setObjectFactory(new DefaultCreator() {
            @Override
            protected ClassLoader getClassLoaderForClass() {
                return CorePlugin.getPlugin().getBukkitClassLoader();
            }
        });
    }

    private void init() {
        MongoCredential credential = MongoCredential.createCredential(this.credentials.getUsername(), credentials.getDatabase(), credentials.getPassword().toCharArray());
        ServerAddress address = new ServerAddress(credentials.getHostname(), credentials.getPort());
        if (credentials.isAuthenticable()) {
            dataSource = new MongoClient(address, Collections.singletonList(credential));
        } else {
            dataSource = new MongoClient(address);
        }
        morphia = new Morphia();
        datastores = new HashMap<>();
    }

    public Datastore registerDatastore(String name) {
        synchronized(datastores) {
            Datastore ds = this.morphia.createDatastore(this.dataSource, name);
            datastores.put(name, ds);
            return ds;
        }
    }

    public Optional<Datastore> getDatastore(String name) {
        synchronized(datastores) {
            Datastore ds = this.datastores.get(name);
            return Optional.ofNullable(ds);
        }
    }

    public boolean deleteDatastore(String datastore) {
        synchronized(datastores){
            return datastores.remove(datastore) != null;
        }
    }

    public MongoClient getConnection() {
        return this.dataSource;
    }

    public synchronized void closeMongo() {
        this.datastores.clear();
        this.dataSource.close();
    }


}
