package net.scholagest.old.services.kdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.services.kdom.DBToKdomConverter;
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
        BaseObject converted = new DBToKdomConverter().convertDbToKdom(toConvert, null);

        assertEquals(toConvert.getKey(), converted.getKey());
        assertEquals(toConvert.getType(), converted.getType());
        assertMapEquals(values, converted.getProperties());
    }

    @Test
    public void testConvertDBToKdomNull() throws DatabaseException {
        assertNull(new DBToKdomConverter().convertDbToKdom(null, null));
    }
}
