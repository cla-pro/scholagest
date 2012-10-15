package net.scholagest.services.kdom;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.database.DatabaseException;

import org.junit.Test;

public class DBToKdomConverterTest {
    @Test
    public void testConvertDBToKdomBasicTypes() throws DatabaseException {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("key1", "value1");
        values.put("key2", 2);
        values.put("key3", false);

        Map<String, Object> converted = new DBToKdomConverter().convertDBToKdom(values);

        assertEquals(values.size(), converted.size());
        for (String key : values.keySet()) {
            assertEquals(values.get(key), converted.get(key));
        }
    }
}
