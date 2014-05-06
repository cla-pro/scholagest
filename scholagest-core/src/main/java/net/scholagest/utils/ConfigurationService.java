package net.scholagest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationService {
    private static ConfigurationService instance;

    static {
        instance = new ConfigurationService();
        instance.loadFile();
    }

    public static ConfigurationService getInstance() {
        return instance;
    }

    private Properties properties;

    private void loadFile() {
        properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("config.properties");
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStringProperty(ScholagestProperty property) {
        return properties.getProperty(property.getKey(), property.getDefaultValue());
    }

    public Integer getIntegerProperty(ScholagestProperty property) {
        String value = getStringProperty(property);
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            // TODO LOG
            return null;
        }
    }
}
