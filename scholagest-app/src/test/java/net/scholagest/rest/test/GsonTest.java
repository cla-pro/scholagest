package net.scholagest.rest.test;

import java.util.Map;
import java.util.UUID;

import net.scholagest.app.rest.old.object.RestObject;
import net.scholagest.app.rest.old.object.RestProperty;

import org.junit.Test;

import com.google.gson.Gson;

public class GsonTest {
	private String json = "{" +
			"key: \"" + UUID.randomUUID().toString() + "\", " +
			"type: \"tClass\", " +
			"properties: {" +
				"pClassStudents: {" +
					"value: [\"" + UUID.randomUUID().toString() + "\", \"" + UUID.randomUUID().toString() + "\"]," +
					"type: \"tSet\"," +
					"displayText: \"Elèves\"" +
				"}," +
				"pClassName: {" +
					"value: \"1P A\"," +
					"type: \"tString\"," +
					"displayText: \"Nom\"" +
				"}" +
			"}}";
	
	@Test
	public void testJson() {
		Gson gson = new Gson();
		RestObject testObject = gson.fromJson(json, RestObject.class);
		
		System.out.println("Key: " + testObject.getKey() + "\nType: " + testObject.getType());
		
		Map<String, RestProperty> properties = testObject.getProperties();
		System.out.println("Properties");
		for (String propertyName : properties.keySet()) {
			RestProperty prop = properties.get(propertyName);
			System.out.println(propertyName);
			System.out.println("    Type: " + prop.getType());
			System.out.println("    DisplayText: " + prop.getDisplayText());
			System.out.println("    Value: " + prop.getValue());
			System.out.println("    Value-class: " + prop.getValue().getClass().getName());
		}
		
		System.out.println(gson.toJson(testObject));
	}
}
