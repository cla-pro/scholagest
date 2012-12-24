package net.scholagest.services.kdom;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.database.DatabaseException;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.AbstractTest;

import org.junit.Test;

public class DBToKdomConverterTest extends AbstractTest {
    @Test
    public void testConvertDBToKdomBasicTypes() throws DatabaseException {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("key1", "value1");
        values.put("key2", 2);
        values.put("key3", false);

        BaseObject toConvert = new BaseObject("key", "type");
        toConvert.setProperties(values);
        BaseObject converted = new DBToKdomConverter().convertDbToKdom(toConvert);

        assertEquals(toConvert.getKey(), converted.getKey());
        assertEquals(toConvert.getType(), converted.getType());
        assertMapEquals(values, converted.getProperties());
    }
}
