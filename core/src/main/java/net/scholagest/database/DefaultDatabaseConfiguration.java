package net.scholagest.database;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.factory.HFactory;

import com.google.inject.Inject;

public class DefaultDatabaseConfiguration implements IDatabaseConfiguration {

    @Inject
    public DefaultDatabaseConfiguration() {}

    @Override
    public String getClusterName() {
        return "scholagest-cluster";
    }

    @Override
    public CassandraHostConfigurator getHostConfigurator() {
        CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator();

        hostConfigurator.setHosts("localhost:9160");
        // hostConfigurator.setHosts("scholagest-demo:9160");

        return hostConfigurator;
    }

    @Override
    public ConsistencyLevelPolicy getConsistencyLevelPolicy() {
        return HFactory.createDefaultConsistencyLevelPolicy();
    }

    @Override
    public int getReplicationFactor() {
        return 1;
    }
}
