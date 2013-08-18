package net.scholagest.managers.ontology.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.namespace.CoreNamespace;

public class DBMap {
    private ITransaction transaction;
    private String mapKey;
    private Map<String, String> cache;

    public DBMap(ITransaction transaction, String mapKey) {
        this.transaction = transaction;
        this.mapKey = mapKey;
        this.cache = new HashMap<>();
    }

    public static DBMap createDBMap(ITransaction transaction, String mapKey) throws DatabaseException {
        DBMap dbMap = new DBMap(transaction, mapKey);
        transaction.insert(mapKey, RDF.type, CoreNamespace.tMap, null);
        return dbMap;
    }

    public String get(String key) throws DatabaseException {
        if (cache.containsKey(key)) {
            return cache.get(key);
        } else {
            String value = (String) transaction.get(mapKey, key, null);
            cache.put(key, value);
            return value;
        }
    }

    public void put(String key, String value) throws DatabaseException {
        cache.put(key, value);
        transaction.insert(mapKey, key, value, null);
    }

    public void remove(String key) throws DatabaseException {
        cache.remove(key);
        transaction.delete(mapKey, key, null);
    }

    public boolean containsKey(String key) throws DatabaseException {
        if (cache.containsKey(key)) {
            return true;
        }
        String value = (String) transaction.get(mapKey, key, null);
        if (value != null) {
            cache.put(key, value);
            return true;
        }
        return false;
    }

    public Set<String> keySet() throws DatabaseException {
        Set<String> keySet = new HashSet<String>(transaction.getColumns(mapKey));
        keySet.remove(RDF.type);

        return keySet;
    }

    public void clear() throws DatabaseException {
        for (String key : keySet()) {
            remove(key);
        }
    }

    public void delete() throws DatabaseException {
        transaction.delete(mapKey, RDF.type, null);
        clear();
    }

    public String getKey() {
        return mapKey;
    }
}
