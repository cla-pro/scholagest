package net.scholagest.services.kdom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.DatabaseException;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.BaseObject;

public class DBToKdomConverter {
    public Set<BaseObject> convertDbSetToKdom(Set<? extends BaseObject> toConvert) throws DatabaseException {
        Set<BaseObject> converted = new HashSet<>();

        for (BaseObject baseObject : toConvert) {
            converted.add(convertDbToKdom(baseObject));
        }

        return converted;
    }

    public BaseObject convertDbToKdom(BaseObject toConvert) throws DatabaseException {
        if (toConvert == null) {
            return null;
        }

        BaseObject clone = new BaseObject(toConvert.getKey(), toConvert.getType());

        Map<String, Object> convertedProperties = new HashMap<>();
        for (String propertyName : toConvert.getProperties().keySet()) {
            Object convertedValue = null;
            Object dbValue = toConvert.getProperty(propertyName);
            if (dbValue instanceof DBSet) {
                DBSet dbSet = (DBSet) dbValue;
                convertedValue = new KSet(dbSet.getKey(), new HashSet<Object>(dbSet.values()));
            } else if (dbValue instanceof BaseObject) {
                convertedValue = convertDbToKdom((BaseObject) dbValue);
            } else {
                convertedValue = dbValue;
            }
            convertedProperties.put(propertyName, convertedValue);
        }
        clone.setProperties(convertedProperties);

        return clone;
    }
}
