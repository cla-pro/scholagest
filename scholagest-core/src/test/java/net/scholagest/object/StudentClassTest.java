package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link StudentClass}
 * 
 * @author CLA
 * @since 0.15.0
 */
public class StudentClassTest {
    @Test
    public void testConstructorCopy() {
        final StudentClass toCopy = new StudentClass("id", Arrays.asList("clazz1", "clazz2"), Arrays.asList("clazz3", "clazz4", "clazz5"));
        final StudentClass copied = new StudentClass(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getCurrentClasses(), copied.getCurrentClasses());
        assertEquals(toCopy.getOldClasses(), copied.getOldClasses());
    }

    @Test
    public void testEquals() {
        final StudentClass studentClasses1 = new StudentClass(null, new ArrayList<String>(), new ArrayList<String>());
        final StudentClass studentClasses2 = new StudentClass("id1", Arrays.asList("clazz1"), Arrays.asList("clazz3"));
        final StudentClass studentClasses3 = new StudentClass("id1", Arrays.asList("clazz1"), Arrays.asList("clazz3"));
        final StudentClass studentClasses4 = new StudentClass("id2", Arrays.asList("clazz1"), Arrays.asList("clazz3"));
        final StudentClass studentClasses5 = new StudentClass("id1", Arrays.asList("clazz2"), Arrays.asList("clazz4"));

        assertFalse(studentClasses1.equals(null));
        assertFalse(studentClasses1.equals(new Object()));
        assertFalse(studentClasses1.equals(studentClasses2));
        assertFalse(studentClasses1.equals(studentClasses3));
        assertFalse(studentClasses1.equals(studentClasses4));
        assertFalse(studentClasses1.equals(studentClasses5));
        assertTrue(studentClasses2.equals(studentClasses3));
        assertFalse(studentClasses2.equals(studentClasses4));
        assertFalse(studentClasses2.equals(studentClasses5));
        assertFalse(studentClasses3.equals(studentClasses4));
        assertFalse(studentClasses3.equals(studentClasses5));
        assertFalse(studentClasses4.equals(studentClasses5));
    }
}
