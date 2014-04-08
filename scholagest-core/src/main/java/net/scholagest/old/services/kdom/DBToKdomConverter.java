package net.scholagest.old.services.kdom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.objects.BaseObject;

public class DBToKdomConverter {
    public Set<BaseObject> convertDbSetToKdom(Set<? extends BaseObject> toConvert, Set<String> filteringProperties) throws DatabaseException {
        Set<BaseObject> converted = new HashSet<>();

        for (BaseObject baseObject : toConvert) {
            converted.add(convertDbToKdom(baseObject, filteringProperties));
        }

        return converted;
    }

    public BaseObject convertDbToKdom(BaseObject toConvert, Set<String> filteringProperties) throws DatabaseException {
        if (toConvert == null) {
            return null;
        }

        BaseObject clone = new BaseObject(toConvert.getKey(), toConvert.getType());

        Map<String, Object> convertedProperties = new HashMap<>();
        for (String propertyName : toConvert.getProperties().keySet()) {
            if (filteringProperties == null || filteringProperties.contains(propertyName)) {
                Object convertedValue = convertValue(toConvert, propertyName);
                convertedProperties.put(propertyName, convertedValue);
            }
        }
        clone.setProperties(convertedProperties);
        clone.flushAllProperties();

        return clone;
    }

    private Object convertValue(BaseObject toConvert, String propertyName) {
        Object convertedValue = null;
        Object dbValue = toConvert.getProperty(propertyName);
        if (dbValue instanceof DBSet) {
            DBSet dbSet = (DBSet) dbValue;
            convertedValue = new KSet(dbSet.getKey(), new HashSet<Object>(dbSet.values()));
        } else if (dbValue instanceof BaseObject) {
            convertedValue = convertDbToKdom((BaseObject) dbValue, null);
        } else {
            convertedValue = dbValue;
        }
        return convertedValue;
    }
}
