package net.scholagest.initializer;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;
import net.scholagest.old.database.IDatabaseConfiguration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBReset {
    private static Logger LOG = LogManager.getLogger(DBReset.class.getName());

    public static void resetKeyspace(IDatabaseConfiguration databaseConfiguration, String keyspace) {
        Cluster cluster = HFactory.getOrCreateCluster(databaseConfiguration.getClusterName(), databaseConfiguration.getHostConfigurator());

        if (cluster.describeKeyspace(keyspace) == null) {
            LOG.info("Keyspace cannot be dropped because it does not exist");
        } else {
            cluster.dropKeyspace(keyspace);
        }
    }
}
