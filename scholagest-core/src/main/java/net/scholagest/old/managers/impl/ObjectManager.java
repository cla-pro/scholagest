package net.scholagest.old.managers.impl;

import java.util.Set;

import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.ontology.OntologyElement;
import net.scholagest.old.managers.ontology.RDF;
import net.scholagest.old.managers.ontology.RDFS;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.ObjectHelper;

public class ObjectManager {
    private IOntologyManager ontologyManager;

    protected ObjectManager(IOntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
    }

    protected IOntologyManager getOntologyManager() {
        return ontologyManager;
    }

    protected void persistObject(ITransaction transaction, BaseObject object) {
        startManagingObject(transaction, object);
        setDefaultValuesOrNullOnObjectsProperties(transaction, object);
        checkRequiredFields(transaction, object);
        object.flushAllProperties();
    }

    private BaseObject startManagingObject(ITransaction transaction, BaseObject object) {
        object.setTransaction(transaction, new ObjectHelper(ontologyManager));

        transaction.insert(object.getKey(), RDF.type, object.getType(), null);

        return object;
    }

    protected void deleteObject(ITransaction transaction, String objectKey) {
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

    // protected BaseObject createObject(ITransaction transaction, String
    // objectKey, String type) throws Exception {
    // if (objectKey == null) {
    // objectKey = UUID.randomUUID().toString();
    // }
    //
    // transaction.insert(objectKey, RDF.type, type, null);
    //
    // return new BaseObject(objectKey, type);
    // }

    // protected Map<String, Object> getAllObjectProperties(ITransaction
    // transaction, String objectKey) throws Exception {
    // return getObjectProperties(transaction, objectKey,
    // transaction.getColumns(objectKey));
    // }

    // protected Map<String, Object> getObjectProperties(ITransaction
    // transaction, String objectKey, Set<String> propertiesName) throws
    // Exception {
    // Map<String, Object> properties = new HashMap<>();
    //
    // for (String name : propertiesName) {
    // Object value = readAndConvertValue(transaction, objectKey, name);
    // properties.put(name, value);
    // }
    //
    // return properties;
    // }

    // private Object readAndConvertValue(ITransaction transaction, String
    // objectKey, String name) throws DatabaseException, Exception {
    // Object value = transaction.get(objectKey, name, null);
    // String type = getPropertyType(transaction, name);
    // if (type != null && ontologyManager.isSubtypeOf(type,
    // CoreNamespace.tSet)) {
    // if (value == null) {
    // value = DBSet.createDBSet(transaction, UUID.randomUUID().toString());
    // } else {
    // value = new DBSet(transaction, (String) value);
    // }
    // } else if (type != null && ontologyManager.isSubtypeOf(type,
    // CoreNamespace.tCalendar)) {
    // value = dateTimeFormatter.parseDateTime((String) value);
    // }
    // return value;
    // }

    private String getPropertyType(ITransaction transaction, String name) {
        OntologyElement element = ontologyManager.getElementWithName(name);
        if (element == null) {
            return null;
        }
        return element.getAttributeWithName(RDFS.range);
    }

    // @SuppressWarnings("unchecked")
    // protected void setObjectProperties(ITransaction transaction, String
    // objectKey, Map<String, Object> properties) throws Exception {
    // for (String propertyName : properties.keySet()) {
    // Object value = properties.get(propertyName);
    //
    // String type = getPropertyType(transaction, propertyName);
    // if (value instanceof DBSet) {
    // transaction.insert(objectKey, propertyName, ((DBSet) value).getKey(),
    // null);
    // } else if (type != null && ontologyManager.isSubtypeOf(type,
    // CoreNamespace.tSet)) {
    // String setKey = (String) transaction.get(objectKey, propertyName, null);
    // DBSet dbSet = null;
    // if (setKey == null) {
    // dbSet = DBSet.createDBSet(transaction, UUID.randomUUID().toString());
    // transaction.insert(objectKey, propertyName, dbSet.getKey(), null);
    // } else {
    // dbSet = new DBSet(transaction, setKey);
    // }
    // dbSet.clear();
    //
    // for (Object element : (Collection<Object>) value) {
    // dbSet.add(element.toString());
    // }
    // } else if (type != null && ontologyManager.isSubtypeOf(type,
    // CoreNamespace.tCalendar)) {
    // transaction.insert(objectKey, propertyName,
    // dateTimeFormatter.print((DateTime) value), null);
    // } else {
    // transaction.insert(objectKey, propertyName, value, null);
    // }
    // }
    // }

    private void setDefaultValuesOrNullOnObjectsProperties(ITransaction transaction, BaseObject object) {
        Set<String> ontologyProperties = ontologyManager.getPropertiesForType(object.getType());

        for (String ontologyProperty : ontologyProperties) {
            if (!RDF.type.equals(ontologyProperty) && object.getProperty(ontologyProperty) == null) {
                OntologyElement element = ontologyManager.getElementWithName(ontologyProperty);
                String value = element.getAttributeWithName(CoreNamespace.pOntologyPropertyDefaultValue);
                if (value != null) {
                    object.putProperty(ontologyProperty, value);
                }
            }
        }
    }

    private void checkRequiredFields(ITransaction transaction, BaseObject object) {
        Set<String> ontologyProperties = ontologyManager.getPropertiesForType(object.getType());

        for (String ontologyProperty : ontologyProperties) {
            OntologyElement element = ontologyManager.getElementWithName(ontologyProperty);
            boolean isRequired = element.getAttributeWithName(CoreNamespace.pOntologyPropertyRequired) != null;
            Object value = object.getProperty(ontologyProperty);
            if (isRequired && !isValueSet(value)) {
                throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.MISSING_REQUIRED_FIELD, "The field " + ontologyProperty
                        + " is required but not set");
            }
        }
    }

    private boolean isValueSet(Object value) {
        if (value == null || "".equals(value)) {
            return false;
        } else {
            return true;
        }
    }
}
