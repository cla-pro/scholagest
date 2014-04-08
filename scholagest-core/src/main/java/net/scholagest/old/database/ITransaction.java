package net.scholagest.old.database;

import java.util.Iterator;
import java.util.Set;

public interface ITransaction {
    public void insert(String key, String column, Object value, String type) throws DatabaseException;

    public void delete(String key, String column, String type) throws DatabaseException;

    public Object get(String key, String column, String type) throws DatabaseException;

    public Set<String> getColumns(String key) throws DatabaseException;

    public Iterator<DbRow> getRowsFromKey(String startKey, int size);

    public Iterator<DbRow> getAllRows() throws DatabaseException;

    public void commit() throws DatabaseException;

    public void rollback() throws DatabaseException;
}
