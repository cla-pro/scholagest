package net.scholagest.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.RDF;

public class BaseObject implements ScholagestObject {
    private ITransaction transaction;

    private String key;
    private String type;
    private Map<String, Object> properties;

    private Set<String> readProperties = new HashSet<>();

    private ObjectHelper objectHelper;

    public BaseObject(String key, String type) {
        this.key = key;
        this.type = type;
        this.properties = new HashMap<>();
    }

    public BaseObject(ITransaction transaction, ObjectHelper objectHelper, String key) {
        this.transaction = transaction;
        this.objectHelper = objectHelper;
        this.key = key;
        this.properties = new HashMap<>();
        this.type = (String) transaction.get(key, RDF.type, null);
    }

    public void setTransaction(ITransaction transaction, ObjectHelper objectHelper) {
        this.transaction = transaction;
        this.objectHelper = objectHelper;
    }

    public void flushAllProperties() {
        for (String propertyName : properties.keySet()) {
            if (objectHelper != null) {
                objectHelper.setObjectProperty(transaction, key, propertyName, properties.get(propertyName));
            }
            readProperties.add(propertyName);
        }
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Map<String, Object> getProperties() {
        if (objectHelper != null) {
            for (String column : transaction.getColumns(key)) {
                if (!column.equals(RDF.type)) {
                    properties.put(column, objectHelper.getObjectProperty(transaction, key, column));
                    readProperties.add(column);
                }
            }
        }

        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        if (properties == null) {
            this.properties = new HashMap<>();
        } else {
            this.properties = properties;
            flushAllProperties();
        }
    }

    @Override
    public Object getProperty(String propertyName) {
        Object value = null;
        if (readProperties.contains(propertyName)) {
            value = properties.get(propertyName);
        } else if (objectHelper != null) {
            value = objectHelper.getObjectProperty(transaction, key, propertyName);
            readProperties.add(propertyName);
            if (value != null) {
                properties.put(propertyName, value);
            }
            return value;
        }

        return value;
    }

    public void putProperty(String propertyName, Object value) {
        properties.put(propertyName, value);
        readProperties.add(propertyName);
        if (objectHelper != null) {
            objectHelper.setObjectProperty(transaction, key, propertyName, value);
        }
    }

    public void putAllProperties(Map<String, Object> toPut) {
        for (String propertyName : toPut.keySet()) {
            putProperty(propertyName, toPut.get(propertyName));
        }
    }

    public void removeProperty(String propertyName) {
        properties.remove(propertyName);
        readProperties.add(propertyName);
        if (transaction != null) {
            transaction.delete(key, propertyName, null);
            // TODO delete the next level if it is a set.
        }
    }
}
