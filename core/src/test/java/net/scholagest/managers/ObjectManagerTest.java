package net.scholagest.managers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.DatabaseException;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Test;

public class ObjectManagerTest extends AbstractTestWithTransaction {
    private ObjectManager objectManager = new ObjectManager(new OntologyManager());

    @Test
    public void testCreateObject() throws Exception {
        String objectKey = "objectKey";
        objectManager.createObject(UUID.randomUUID().toString(), transaction, objectKey, CoreNamespace.tGroup);

        verify(transaction).insert(objectKey, RDF.type, CoreNamespace.tGroup, null);
    }

    @Test
    public void testSetObjectProperties() throws Exception {
        Map<String, Object> properties = createProperties();

        objectManager.setObjectProperties(UUID.randomUUID().toString(), transaction, "objectKey", properties);

        for (Map.Entry<String, Object> prop : properties.entrySet()) {
            verify(transaction).insert(anyString(), eq(prop.getKey()), eq(prop.getValue()), anyString());
        }
    }

    @Test
    public void testGetObjectProperties() throws Exception {
        String objectKey = "objectKey";

        insertObjectProperties(objectKey, createProperties());

        Set<String> properties = new HashSet<>();
        properties.add("prop1");
        properties.add("prop2");

        objectManager.getObjectProperties(UUID.randomUUID().toString(), transaction, objectKey, properties);

        for (String prop : properties) {
            verify(transaction).get(anyString(), eq(prop), anyString());
        }
    }

    private void insertObjectProperties(String objectKey, Map<String, Object> properties) throws DatabaseException {
        for (Map.Entry<String, Object> prop : properties.entrySet()) {
            transaction.insert(objectKey, prop.getKey(), prop.getValue(), null);
        }
    }

    @Test
    public void testGetSetObjectProperties() throws Exception {
        Map<String, Object> propertiesMap = createProperties();

        objectManager.setObjectProperties(UUID.randomUUID().toString(), transaction, "objectKey", propertiesMap);

        Map<String, Object> resultProperties = objectManager.getObjectProperties(UUID.randomUUID().toString(), transaction, "objectKey",
                propertiesMap.keySet());

        assertEquals("Returned properties should habe the same size as the saved one", propertiesMap.size(), resultProperties.size());
        for (String propertyName : propertiesMap.keySet()) {
            assertEquals("Wrong value for property \"" + propertyName + "\"", propertiesMap.get(propertyName), resultProperties.get(propertyName));
        }
    }

    private Map<String, Object> createProperties() {
        Map<String, Object> propertiesMap = new HashMap<>();

        propertiesMap.put("prop1", "value1");
        propertiesMap.put("prop2", "value2");

        return propertiesMap;
    }
}
