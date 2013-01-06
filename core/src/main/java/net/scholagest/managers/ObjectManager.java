package net.scholagest.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.managers.ontology.RDFS;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.BaseObject;

public class ObjectManager {
    private OntologyManager ontologyManager;

    protected ObjectManager(OntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
    }

    protected BaseObject createObject(String requestId, ITransaction transaction, String objectKey, String type) throws Exception {
        if (objectKey == null) {
            objectKey = UUID.randomUUID().toString();
        }

        transaction.insert(objectKey, RDF.type, type, null);

        return new BaseObject(objectKey, type);
    }

    protected Map<String, Object> getObjectProperties(String requestId, ITransaction transaction, String objectKey, Set<String> propertiesName)
            throws Exception {
        Map<String, Object> properties = new HashMap<>();

        for (String name : propertiesName) {
            Object value = transaction.get(objectKey, name, null);
            String type = getPropertyType(requestId, transaction, name);
            if (type != null && ontologyManager.isSubtypeOf(requestId, transaction, type, CoreNamespace.tSet)) {
                if (value == null) {
                    value = DBSet.createDBSet(transaction, UUID.randomUUID().toString());
                } else {
                    value = new DBSet(transaction, (String) value);
                }
            }
            properties.put(name, value);
        }

        return properties;
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
        	if (type != null && ontologyManager.isSubtypeOf(requestId, transaction, type, CoreNamespace.tSet)) {
        		String setKey = (String) transaction.get(objectKey, propertyName, null);
        		DBSet dbSet = null;
        		if (setKey == null) {
        			dbSet = DBSet.createDBSet(transaction, UUID.randomUUID().toString());
        			transaction.insert(objectKey, propertyName, dbSet.getKey(), null);
                } else {
                	dbSet = new DBSet(transaction, (String) setKey);
                }
        		dbSet.clear();
        		
        		for (Object element : (Collection<Object>) value) {
					dbSet.add(element.toString());
				}
        	}
        	else {
        		transaction.insert(objectKey, propertyName, value, null);
        	}
        }
    }
}
