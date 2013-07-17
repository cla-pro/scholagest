package net.scholagest.managers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.managers.ontology.RDFS;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ObjectManager {
    private static final String DATE_TIME_FORMAT_PATTERN = "YYYY-MM-dd HH:mm:ss:SSS";

    private IOntologyManager ontologyManager;

    private DateTimeFormatter dateTimeFormatter;

    protected ObjectManager(IOntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
        dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT_PATTERN);
    }

    protected void persistObject(ITransaction transaction, BaseObject object) throws Exception {
        createObject(transaction, object.getKey(), object.getType());
        setObjectProperties(transaction, object.getKey(), object.getProperties());
    }

    protected void deleteObject(ITransaction transaction, String objectKey) throws Exception {
        for (String property : transaction.getColumns(objectKey)) {
            String propertyType = getPropertyType(transaction, property);

            if (propertyType != null && ontologyManager.isSubtypeOf(propertyType, CoreNamespace.tSet)) {
                Object value = transaction.get(objectKey, property, null);
                DBSet set = new DBSet(transaction, (String) value);
                set.delete();
            }

            transaction.delete(objectKey, property, null);
        }
    }

    protected BaseObject createObject(ITransaction transaction, String objectKey, String type) throws Exception {
        if (objectKey == null) {
            objectKey = UUID.randomUUID().toString();
        }

        transaction.insert(objectKey, RDF.type, type, null);

        return new BaseObject(objectKey, type);
    }

    protected Map<String, Object> getAllObjectProperties(ITransaction transaction, String objectKey) throws Exception {
        return getObjectProperties(transaction, objectKey, transaction.getColumns(objectKey));
    }

    protected Map<String, Object> getObjectProperties(ITransaction transaction, String objectKey, Set<String> propertiesName) throws Exception {
        Map<String, Object> properties = new HashMap<>();

        for (String name : propertiesName) {
            Object value = readAndConvertValue(transaction, objectKey, name);
            properties.put(name, value);
        }

        return properties;
    }

    private Object readAndConvertValue(ITransaction transaction, String objectKey, String name) throws DatabaseException, Exception {
        Object value = transaction.get(objectKey, name, null);
        String type = getPropertyType(transaction, name);
        if (type != null && ontologyManager.isSubtypeOf(type, CoreNamespace.tSet)) {
            if (value == null) {
                value = DBSet.createDBSet(transaction, UUID.randomUUID().toString());
            } else {
                value = new DBSet(transaction, (String) value);
            }
        } else if (type != null && ontologyManager.isSubtypeOf(type, CoreNamespace.tCalendar)) {
            value = dateTimeFormatter.parseDateTime((String) value);
        }
        return value;
    }

    private String getPropertyType(ITransaction transaction, String name) throws Exception {
        OntologyElement element = ontologyManager.getElementWithName(name);
        if (element == null) {
            return null;
        }
        return element.getAttributeWithName(RDFS.range);
    }

    @SuppressWarnings("unchecked")
    protected void setObjectProperties(ITransaction transaction, String objectKey, Map<String, Object> properties) throws Exception {
        for (String propertyName : properties.keySet()) {
            Object value = properties.get(propertyName);

            String type = getPropertyType(transaction, propertyName);
            if (value instanceof DBSet) {
                transaction.insert(objectKey, propertyName, ((DBSet) value).getKey(), null);
            } else if (type != null && ontologyManager.isSubtypeOf(type, CoreNamespace.tSet)) {
                String setKey = (String) transaction.get(objectKey, propertyName, null);
                DBSet dbSet = null;
                if (setKey == null) {
                    dbSet = DBSet.createDBSet(transaction, UUID.randomUUID().toString());
                    transaction.insert(objectKey, propertyName, dbSet.getKey(), null);
                } else {
                    dbSet = new DBSet(transaction, setKey);
                }
                dbSet.clear();

                for (Object element : (Collection<Object>) value) {
                    dbSet.add(element.toString());
                }
            } else if (type != null && ontologyManager.isSubtypeOf(type, CoreNamespace.tCalendar)) {
                transaction.insert(objectKey, propertyName, dateTimeFormatter.print((DateTime) value), null);
            } else {
                transaction.insert(objectKey, propertyName, value, null);
            }
        }
    }
}
