package net.scholagest.old.managers.ontology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.managers.ontology.RDF;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.utils.old.AbstractTestWithTransaction;

import org.junit.Test;
import org.mockito.Mockito;

public class DBSetTest extends AbstractTestWithTransaction {
    private static final String SET_KEY = "key1";
    private static final String ELEMENT = "element1";

    @Test
    public void testCreateDBSet() throws DatabaseException {
        DBSet dbSet = DBSet.createDBSet(transaction, SET_KEY);

        Mockito.verify(transaction).insert(SET_KEY, RDF.type, CoreNamespace.tSet, null);
        assertEquals(SET_KEY, dbSet.getKey());
    }

    @Test
    public void testAdd() throws Exception {
        DBSet dbSet = DBSet.createDBSet(transaction, SET_KEY);

        dbSet.add(ELEMENT);
        Mockito.verify(transaction).insert(SET_KEY, ELEMENT, ELEMENT, null);
    }

    @Test
    public void testAddTwice() throws Exception {
        DBSet dbSet = DBSet.createDBSet(transaction, SET_KEY);

        dbSet.add(ELEMENT);
        dbSet.add(ELEMENT);
        Mockito.verify(transaction, Mockito.times(2)).insert(SET_KEY, ELEMENT, ELEMENT, null);
    }

    @Test
    public void testContains() throws Exception {
        DBSet dbSet = DBSet.createDBSet(transaction, SET_KEY);

        assertFalse(dbSet.contains(ELEMENT));

        dbSet.add(ELEMENT);
        assertTrue(dbSet.contains(ELEMENT));
    }

    @Test
    public void testRemove() throws Exception {
        DBSet dbSet = DBSet.createDBSet(transaction, SET_KEY);

        dbSet.add(ELEMENT);
        assertTrue(dbSet.contains(ELEMENT));
        dbSet.remove(ELEMENT);
        Mockito.verify(transaction).delete(SET_KEY, ELEMENT, null);
        assertFalse(dbSet.contains(ELEMENT));
    }

    @Test
    public void testSize() throws Exception {
        DBSet dbSet = DBSet.createDBSet(transaction, SET_KEY);

        dbSet.add(ELEMENT);
        assertEquals(1, dbSet.size());

        dbSet.add(ELEMENT + "second");
        assertEquals(2, dbSet.size());

        dbSet.add(ELEMENT);
        assertEquals(2, dbSet.size());
    }

    @Test
    public void testValues() throws Exception {
        DBSet dbSet = DBSet.createDBSet(transaction, SET_KEY);

        String[] elements = { ELEMENT, ELEMENT + "second" };

        for (String e : elements) {
            dbSet.add(e);
        }

        Set<String> values = dbSet.values();
        assertEquals(elements.length, values.size());
        for (String e : elements) {
            assertTrue(values.contains(e));
        }
    }
}
