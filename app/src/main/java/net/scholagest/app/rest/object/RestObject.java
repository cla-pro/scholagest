package net.scholagest.app.rest.object;

import java.util.Map;

public class RestObject {
	private String key;
	private String type;
	private Map<String, RestProperty> properties;
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Map<String, RestProperty> getProperties() {
		return properties;
	}
	
	public void setProperties(Map<String, RestProperty> properties) {
		this.properties = properties;
	}
	
	@Override
	public String toString() {
		return String.format("ScObject [key: %s, type: %s, properties: %s]", key, type, properties.toString());
	}
}
