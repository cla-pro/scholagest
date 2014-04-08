package net.scholagest.utils.old;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.database.DbRow;
import net.scholagest.old.database.IDatabase;
import net.scholagest.old.database.ITransaction;

public class InMemoryDatabase implements IDatabase {
    public InMemoryDatabase() {

    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public InMemoryTransaction getTransaction(String keyspaceName) {
        return new InMemoryTransaction(keyspaceName);
    }

    @Override
    public void createIndex(String columnName) throws DatabaseException {

    }

    public class InMemoryTransaction implements ITransaction {
        private String keyspace;
        private Map<String, Map<String, Object>> values = new HashMap<>();

        public InMemoryTransaction(String keyspace) {
            this.keyspace = keyspace;
        }

        @Override
        public void insert(String key, String column, Object value, String type) throws DatabaseException {
            Map<String, Object> columns = values.get(key);
            if (columns == null) {
                columns = new HashMap<>();
                values.put(key, columns);
            }

            columns.put(column, value);
        }

        @Override
        public void delete(String key, String column, String type) throws DatabaseException {
            Map<String, Object> columns = values.get(key);
            if (columns != null) {
                columns.remove(column);
            }
        }

        @Override
        public Object get(String key, String column, String type) throws DatabaseException {
            Map<String, Object> columns = values.get(key);
            if (columns == null) {
                return null;
            }

            return columns.get(column);
        }

        @Override
        public Set<String> getColumns(String key) throws DatabaseException {
            Map<String, Object> columns = values.get(key);
            if (columns == null) {
                return new HashSet<>();
            }
            return columns.keySet();
        }

        @Override
        public Iterator<DbRow> getRowsFromKey(String startKey, int size) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Iterator<DbRow> getAllRows() throws DatabaseException {
            // TODO Auto-generated method stub
            return null;
        }

        public String getKeyspace() {
            return keyspace;
        }

        @Override
        public void commit() throws DatabaseException {

        }

        @Override
        public void rollback() throws DatabaseException {

        }

        public Map<String, Map<String, Object>> getValues() {
            return values;
        }

        public void addValues(Map<String, Map<String, Object>> toAdd) {
            values.putAll(toAdd);
        }
    }
}
