package net.scholagest.managers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.managers.ontology.RDFS;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.BaseObject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ObjectManager {
    private static final String DATE_TIME_FORMAT_PATTERN = "YYYY-MM-dd HH:mm:ss:SSS";

    private OntologyManager ontologyManager;

    private DateTimeFormatter dateTimeFormatter;

    protected ObjectManager(OntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
        dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT_PATTERN);
    }

    protected void persistObject(String requestId, ITransaction transaction, BaseObject object) throws Exception {
        createObject(requestId, transaction, object.getKey(), object.getType());
        setObjectProperties(requestId, transaction, object.getKey(), object.getProperties());
    }

    protected void deleteObject(String requestId, ITransaction transaction, String objectKey) throws Exception {
        for (String property : transaction.getColumns(objectKey)) {
            String propertyType = getPropertyType(requestId, transaction, property);

            if (propertyType != null && ontologyManager.isSubtypeOf(requestId, transaction, propertyType, CoreNamespace.tSet)) {
                Object value = transaction.get(objectKey, property, null);
                DBSet set = new DBSet(transaction, (String) value);
                set.delete();
            }

            transaction.delete(objectKey, property, null);
        }
    }

    protected BaseObject createObject(String requestId, ITransaction transaction, String objectKey, String type) throws Exception {
        if (objectKey == null) {
            objectKey = UUID.randomUUID().toString();
        }

        transaction.insert(objectKey, RDF.type, type, null);

        return new BaseObject(objectKey, type);
    }

    protected Map<String, Object> getAllObjectProperties(String requestId, ITransaction transaction, String objectKey) throws Exception {
        return getObjectProperties(requestId, transaction, objectKey, transaction.getColumns(objectKey));
    }

    protected Map<String, Object> getObjectProperties(String requestId, ITransaction transaction, String objectKey, Set<String> propertiesName)
            throws Exception {
        Map<String, Object> properties = new HashMap<>();

        for (String name : propertiesName) {
            Object value = readAndConvertValue(requestId, transaction, objectKey, name);
            properties.put(name, value);
        }

        return properties;
    }

    private Object readAndConvertValue(String requestId, ITransaction transaction, String objectKey, String name) throws DatabaseException, Exception {
        Object value = transaction.get(objectKey, name, null);
        String type = getPropertyType(requestId, transaction, name);
        if (type != null && ontologyManager.isSubtypeOf(requestId, transaction, type, CoreNamespace.tSet)) {
            if (value == null) {
                value = DBSet.createDBSet(transaction, UUID.randomUUID().toString());
            } else {
                value = new DBSet(transaction, (String) value);
            }
        } else if (type != null && ontologyManager.isSubtypeOf(requestId, transaction, type, CoreNamespace.tCalendar)) {
            value = dateTimeFormatter.parseDateTime((String) value);
        }
        return value;
    }

    private String getPropertyType(String requestId, ITransaction transaction, String name) throws Exception {
        OntologyElement element = ontologyManager.getElementWithName(requestId, transaction, name);
        if (element == null) {
            return null;
        }
        return element.getAttributeWithName(RDFS.range);
    }

    @SuppressWarnings("unchecked")
    protected void setObjectProperties(String requestId, ITransaction transaction, String objectKey, Map<String, Object> properties) throws Exception {
        for (String propertyName : properties.keySet()) {
            Object value = properties.get(propertyName);

            String type = getPropertyType(requestId, transaction, propertyName);
            if (value instanceof DBSet) {
                transaction.insert(objectKey, propertyName, ((DBSet) value).getKey(), null);
            } else if (type != null && ontologyManager.isSubtypeOf(requestId, transaction, type, CoreNamespace.tSet)) {
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
            } else if (type != null && ontologyManager.isSubtypeOf(requestId, transaction, type, CoreNamespace.tCalendar)) {
                transaction.insert(objectKey, propertyName, dateTimeFormatter.print((DateTime) value), null);
            } else {
                transaction.insert(objectKey, propertyName, value, null);
            }
        }
    }
}
