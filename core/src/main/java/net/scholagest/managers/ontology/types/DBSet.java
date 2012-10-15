package net.scholagest.managers.ontology.types;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.ontology.RDF;

public class DBSet {
    private ITransaction transaction;
    private String setKey;
    private Set<String> cache;

    public DBSet(ITransaction transaction, String setKey) {
        this.transaction = transaction;
        this.setKey = setKey;
        this.cache = new HashSet<>();
    }

    public static DBSet createDBSet(ITransaction transaction, String setKey) throws DatabaseException {
        DBSet dbSet = new DBSet(transaction, setKey);
        transaction.insert(setKey, RDF.type, CoreNamespace.tSet, null);
        return dbSet;
    }

    public void add(String element) throws DatabaseException {
        cache.add(element);
        transaction.insert(setKey, element, element, null);
    }

    public void remove(String element) throws DatabaseException {
        cache.remove(element);
        transaction.delete(setKey, element, null);
    }

    public boolean contains(String element) throws DatabaseException {
        if (cache.contains(element)) {
            return true;
        }
        String value = (String) transaction.get(setKey, element, null);
        if (value != null) {
            cache.add(value);
            return true;
        }
        return false;
    }

    public int size() throws DatabaseException {
        // -1 for the RDF.type
        return transaction.getColumns(setKey).size() - 1;
    }

    public Set<String> values() throws DatabaseException {
        Set<String> values = new HashSet<String>(transaction.getColumns(setKey));
        values.remove(RDF.type);

        return values;
    }

    public String getKey() {
        return setKey;
    }
}
