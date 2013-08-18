package net.scholagest.database;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.SliceQuery;

import com.google.inject.Inject;

public class Database implements IDatabase {
    private static final String COLUMN_FAMILY_NAME = "scholagestCF";
    private IDatabaseConfiguration databaseConfiguration = null;
    private Cluster cluster = null;

    @Inject
    public Database(IDatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
        startup();
    }

    @Override
    public void startup() {
        if (cluster != null) {
            return;
        }

        cluster = HFactory.getOrCreateCluster(databaseConfiguration.getClusterName(), databaseConfiguration.getHostConfigurator());
    }

    @Override
    public void shutdown() {
        HFactory.shutdownCluster(cluster);
    }

    @Override
    public ITransaction getTransaction(String keyspaceName) {
        Keyspace keyspace = getOrCreateKeyspace(keyspaceName);
        ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<>(keyspace, COLUMN_FAMILY_NAME,
                StringSerializer.get(), StringSerializer.get());

        return new Transaction(keyspace, columnFamilyTemplate);
    }

    @Override
    public void createIndex(String columnName) throws DatabaseException {
        // TODO Auto-generated method stub

    }

    private Keyspace getOrCreateKeyspace(String keyspaceName) {
        if (cluster.describeKeyspace(keyspaceName) == null) {
            ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(keyspaceName, COLUMN_FAMILY_NAME, ComparatorType.UTF8TYPE);

            KeyspaceDefinition newKeyspace = HFactory.createKeyspaceDefinition(keyspaceName, ThriftKsDef.DEF_STRATEGY_CLASS,
                    databaseConfiguration.getReplicationFactor(), Arrays.asList(cfDef));

            cluster.addKeyspace(newKeyspace, true);
        }

        return HFactory.createKeyspace(keyspaceName, cluster);
    }

    protected class Transaction implements ITransaction {
        private Keyspace keyspace;
        private ColumnFamilyTemplate<String, String> template;

        // private TransactionCommiter transactionCommiter;

        public Transaction(Keyspace keyspace, ColumnFamilyTemplate<String, String> template) {
            this.keyspace = keyspace;
            this.template = template;
            // this.transactionCommiter = new TransactionCommiter();
        }

        @Override
        public void insert(String key, String column, Object value, String type) throws DatabaseException {
            // String originalValue = (String) get(key, column, type);
            // transactionCommiter.add(new InsertDBAction(template, key, column,
            // (String) value, originalValue));

            ColumnFamilyUpdater<String, String> updater = template.createUpdater(key);

            updater.setString(column, (String) value);

            try {
                template.update(updater);
            } catch (HectorException e) {
                throw new DatabaseException(-1, -1, e.getMessage(), e);
            }
        }

        @Override
        public void delete(String key, String column, String type) throws DatabaseException {
            // String originalValue = (String) get(key, column, type);
            // transactionCommiter.add(new DeleteDBAction(template, key, column,
            // originalValue));

            try {
                template.deleteColumn(key, column);
            } catch (HectorException e) {
                throw new DatabaseException(-1, -1, e.getMessage(), e);
            }
        }

        @Override
        public Object get(String key, String column, String type) throws DatabaseException {
            Object result = null;
            try {
                ColumnFamilyResult<String, String> columnFamilyResult = template.queryColumns(key);
                result = columnFamilyResult.getString(column);
            } catch (HectorException e) {
                throw new DatabaseException(-1, -1, e.getMessage(), e);
            }

            return result;
        }

        @Override
        public Set<String> getColumns(String key) throws DatabaseException {
            SliceQuery<String, String, String> query = HFactory
                    .createSliceQuery(keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get()).setKey(key)
                    .setColumnFamily(COLUMN_FAMILY_NAME);

            ColumnSliceIterator<String, String, String> iterator = new ColumnSliceIterator<String, String, String>(query, null, "\uFFFF", false);

            Set<String> columns = new TreeSet<>();
            while (iterator.hasNext()) {
                HColumn<String, String> col = iterator.next();
                columns.add(col.getName());
            }

            return columns;
        }

        @Override
        public Map<String, Object> getRow(String key) throws DatabaseException {
            throw new DatabaseException(-1, -1, "Not yet implemented");
        }

        @Override
        public void commit() throws DatabaseException {
            // TODO Auto-generated method stub

        }

        @Override
        public void rollback() throws DatabaseException {
            // TODO Auto-generated method stub

        }
    }
}
