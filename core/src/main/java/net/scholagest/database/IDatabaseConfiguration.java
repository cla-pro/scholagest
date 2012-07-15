package net.scholagest.database;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;

public interface IDatabaseConfiguration {
	public String getClusterName();
	
	public CassandraHostConfigurator getHostConfigurator();
	
	public ConsistencyLevelPolicy getConsistencyLevelPolicy();
	
	public int getReplicationFactor();
}
