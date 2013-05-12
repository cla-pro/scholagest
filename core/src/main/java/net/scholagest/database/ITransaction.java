package net.scholagest.database;

import java.util.Map;
import java.util.Set;

public interface ITransaction {
    public void insert(String key, String column, Object value, String type) throws DatabaseException;

    public void delete(String key, String column, String type) throws DatabaseException;

    public Object get(String key, String column, String type) throws DatabaseException;

    public Set<String> getColumns(String key) throws DatabaseException;

    public Map<String, Object> getRow(String key) throws DatabaseException;

    public void commit() throws DatabaseException;

    public void rollback() throws DatabaseException;
}
