package net.scholagest.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.RDF;

public class ObjectManager {
	protected String createObject(String requestId, ITransaction transaction,
			String objectKey, String type) throws Exception {
		if (objectKey == null)
			objectKey = UUID.randomUUID().toString();
		
		transaction.insert(objectKey, RDF.type, type, null);
		
		return objectKey;
	}
	
	protected Map<String, Object> getObjectProperties(String requestId,
			ITransaction transaction, String objectKey,
			Set<String> propertiesName) throws Exception {
		Map<String, Object> properties = new HashMap<>();
		
		for (String name : propertiesName) {
			Object value = transaction.get(objectKey, name, null);
			if (value != null)
				properties.put(name, value);
		}
		
		return properties;
	}

	protected void setObjectProperties(String requestId, ITransaction transaction,
			String objectKey, Map<String, Object> properties) throws Exception {
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			System.out.println("Set property: " + entry.getKey() + ", " + entry.getValue());
			transaction.insert(objectKey, entry.getKey(),
					entry.getValue(), null);
		}
	}
}
