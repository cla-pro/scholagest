package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link Clazz}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ClazzTest {
    @Test
    public void testConstructorCopy() {
        final Clazz toCopy = new Clazz("id", "name", "year", Arrays.asList("period1", "period2"), Arrays.asList("teacher1", "teacher2"),
                Arrays.asList("student1", "student2"), Arrays.asList("branch1", "branch2"));
        final Clazz copied = new Clazz(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getName(), copied.getName());
        assertEquals(toCopy.getYear(), copied.getYear());
        assertEquals(toCopy.getPeriods(), copied.getPeriods());
        assertEquals(toCopy.getTeachers(), copied.getTeachers());
        assertEquals(toCopy.getStudents(), copied.getStudents());
        assertEquals(toCopy.getBranches(), copied.getBranches());
    }

    @Test
    public void testEquals() {
        final Clazz clazz1 = new Clazz(null, null, null, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
        final Clazz clazz2 = new Clazz(null, "name", "year1", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
        final Clazz clazz3 = new Clazz("1", "otherName", "year2", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
        final Clazz clazz4 = new Clazz("1", "name", "year1", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
        final Clazz clazz5 = new Clazz("1", "name", "year1", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());

        assertFalse(clazz1.equals(null));
        assertFalse(clazz1.equals(new Object()));
        assertFalse(clazz1.equals(clazz2));
        assertFalse(clazz1.equals(clazz3));
        assertFalse(clazz1.equals(clazz4));
        assertFalse(clazz1.equals(clazz5));
        assertFalse(clazz2.equals(clazz3));
        assertFalse(clazz2.equals(clazz4));
        assertFalse(clazz2.equals(clazz5));
        assertFalse(clazz3.equals(clazz4));
        assertFalse(clazz3.equals(clazz5));
        assertTrue(clazz4.equals(clazz5));
    }
}
