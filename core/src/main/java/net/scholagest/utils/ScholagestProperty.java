package net.scholagest.utils;

public class ScholagestProperty {
    public static ScholagestProperty KEYSPACE = new ScholagestProperty("net.scholagest.keyspace", "DefaultScholagestKeyspace");
    public static ScholagestProperty BASE_URL = new ScholagestProperty("net.scholagest.baseUrl", "http://localhost:8080/scholagest-app/");

    private String key;
    private String defaultValue;

    private ScholagestProperty(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
