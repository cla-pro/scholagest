package net.scholagest.test.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.Database;
import net.scholagest.database.DatabaseException;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IObjectManager;
import net.scholagest.managers.ObjectManager;

public class ObjectManagerTest {
	public static void main(String[] args) {
		Database database = new Database(new DefaultDatabaseConfiguration());
		database.startup();
		
		IObjectManager objectManager = new ObjectManager();
		
		ITransaction transaction = null;
		try {
			transaction = database.getTransaction("ScholagestSecheron");
			
			Map<String, Object> properties = new HashMap<>();
			properties.put("prop1", "value1");
			properties.put("prop2", "valueasf2");
			properties.put("prop3", "valu3");
			properties.put("prop4", "value4.439085");
			
			Set<String> propsName = new HashSet<>();
			propsName.add("prop1");
			propsName.add("prop2");
			propsName.add("prop3");
			propsName.add("prop4");
			propsName.add("prop5");
			propsName.add("prop6");
			propsName.add("prop7");
			
			objectManager.setObjectProperties(UUID.randomUUID().toString(),
					transaction, "object1", properties);
			
			for (Map.Entry<String, Object> prop :
				objectManager.getObjectProperties(UUID.randomUUID().toString(),
						transaction, "object1", propsName).entrySet()) {
				System.out.println("\t" + prop.getKey() + " => "
						+ prop.getValue());
			}
			
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				transaction.rollback();
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
		}
		
		database.shutdown();
	}
}
