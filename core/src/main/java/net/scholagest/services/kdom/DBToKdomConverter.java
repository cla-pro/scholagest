package net.scholagest.services.kdom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.scholagest.database.DatabaseException;
import net.scholagest.managers.ontology.types.DBSet;

public class DBToKdomConverter {
    public Map<String, Object> convertDBToKdom(Map<String, Object> toConvert) throws DatabaseException {
        Map<String, Object> convertedMap = new HashMap<String, Object>();

        for (String propertyName : toConvert.keySet()) {
            Object value = toConvert.get(propertyName);
            if (value instanceof DBSet) {
                DBSet dbSet = (DBSet) value;
                KSet kSet = new KSet(dbSet.getKey(), new HashSet<Object>(dbSet.values()));
                convertedMap.put(propertyName, kSet);
            } else {
                convertedMap.put(propertyName, value);
            }
        }

        return convertedMap;
    }
}
