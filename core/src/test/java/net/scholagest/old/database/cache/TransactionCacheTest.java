package net.scholagest.old.database.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.old.database.cache.CacheResult;
import net.scholagest.old.database.cache.TransactionCache;
import net.scholagest.old.database.commiter.DeleteDBAction;
import net.scholagest.old.database.commiter.InsertDBAction;

import org.junit.Test;

public class TransactionCacheTest {
    private final String key = "key";
    private final String column = "column";
    private final String value = "value";
    private final TransactionCache testee = new TransactionCache();

    @Test
    public void testAddInsertAndGet() {
        testee.addAction(new InsertDBAction(null, key, column, value, null));

        CacheResult result1 = testee.get(key, column);
        assertTrue(result1.isFound());
        assertEquals(value, result1.getValue());
        CacheResult result2 = testee.get("key2", column);
        assertFalse(result2.isFound());
        assertNull(result2.getValue());
        CacheResult result3 = testee.get(key, "column2");
        assertFalse(result3.isFound());
        assertNull(result3.getValue());
    }

    @Test
    public void testAddDeleteAndGet() {
        testee.addAction(new DeleteDBAction(null, key, column, null));

        CacheResult result1 = testee.get(key, column);
        assertTrue(result1.isFound());
        assertNull(result1.getValue());
        CacheResult result2 = testee.get("key2", column);
        assertFalse(result2.isFound());
        assertNull(result2.getValue());
        CacheResult result3 = testee.get(key, "column2");
        assertFalse(result3.isFound());
        assertNull(result3.getValue());
    }

    @Test
    public void testAddInsertAndDeleteAndGet() {
        {
            testee.addAction(new InsertDBAction(null, key, column, value, null));
            testee.addAction(new DeleteDBAction(null, key, column, null));

            CacheResult result = testee.get(key, column);
            assertTrue(result.isFound());
            assertNull(result.getValue());
        }
        {
            testee.addAction(new DeleteDBAction(null, key, column, null));
            testee.addAction(new InsertDBAction(null, key, column, value, null));

            CacheResult result = testee.get(key, column);
            assertTrue(result.isFound());
            assertEquals(value, result.getValue());
        }
    }

    @Test
    public void testAddInsertDeleteAndUpdateColumns() {
        Set<String> columns = new HashSet<>();
        columns.add("column0");
        columns.add("column1");
        columns.add("column2");

        testee.addAction(new InsertDBAction(null, key, "column2", value, null));
        testee.addAction(new InsertDBAction(null, key, "column3", value, null));
        testee.addAction(new DeleteDBAction(null, key, "column4", null));
        testee.addAction(new DeleteDBAction(null, key, "column1", null));

        Set<String> updated = testee.updateColumns(key, columns);
        assertTrue(updated.contains("column0"));
        assertFalse(updated.contains("column1"));
        assertTrue(updated.contains("column2"));
        assertTrue(updated.contains("column3"));
        assertFalse(updated.contains("column4"));
    }

    @Test
    public void testAddInsertAndSameDeleteAndUpdateColumns() {
        {
            Set<String> columns = new HashSet<>();

            testee.addAction(new InsertDBAction(null, key, "column", value, null));
            testee.addAction(new DeleteDBAction(null, key, "column", null));

            Set<String> updated = testee.updateColumns(key, columns);

            assertFalse(updated.contains("column"));
        }
        {
            Set<String> columns = new HashSet<>();

            testee.addAction(new DeleteDBAction(null, key, "column", null));
            testee.addAction(new InsertDBAction(null, key, "column", value, null));

            Set<String> updated = testee.updateColumns(key, columns);

            assertTrue(updated.contains("column"));
        }
    }
}
