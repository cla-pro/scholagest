package net.scholagest.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

public class ConfigurationServiceImplTest {
    @Test
    public void testGetStringProperty() {
        assertEquals("value1", ConfigurationServiceImpl.getInstance().getStringProperty(createProperty("prop1", null)));
        assertNull(ConfigurationServiceImpl.getInstance().getStringProperty(createProperty("prop2", null)));
        assertEquals("default", ConfigurationServiceImpl.getInstance().getStringProperty(createProperty("prop2", "default")));
    }

    @Test
    public void testGetIntegerProperty() {
        assertEquals(3, ConfigurationServiceImpl.getInstance().getIntegerProperty(createProperty("prop3", null)).intValue());
        // Invalid number
        assertNull(ConfigurationServiceImpl.getInstance().getIntegerProperty(createProperty("prop1", null)));

        assertEquals(5, ConfigurationServiceImpl.getInstance().getIntegerProperty(createProperty("prop5", "5")).intValue());
        assertNull(ConfigurationServiceImpl.getInstance().getIntegerProperty(createProperty("prop4", null)));
    }

    private ScholagestProperty createProperty(String key, String defaultValue) {
        ScholagestProperty property = Mockito.mock(ScholagestProperty.class);

        Mockito.when(property.getKey()).thenReturn(key);
        Mockito.when(property.getDefaultValue()).thenReturn(defaultValue);

        return property;
    }
}
