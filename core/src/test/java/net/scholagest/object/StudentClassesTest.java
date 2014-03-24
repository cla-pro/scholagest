package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link StudentClasses}
 * 
 * @author CLA
 * @since 0.15.0
 */
public class StudentClassesTest {
    @Test
    public void testConstructorCopy() {
        final StudentClasses toCopy = new StudentClasses("id", Arrays.asList("clazz1", "clazz2"), Arrays.asList("clazz3", "clazz4", "clazz5"));
        final StudentClasses copied = new StudentClasses(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getCurrentClasses(), copied.getCurrentClasses());
        assertEquals(toCopy.getOldClasses(), copied.getOldClasses());
    }

    @Test
    public void testEquals() {
        final StudentClasses studentClasses1 = new StudentClasses(null, new ArrayList<String>(), new ArrayList<String>());
        final StudentClasses studentClasses2 = new StudentClasses("id1", Arrays.asList("clazz1"), Arrays.asList("clazz3"));
        final StudentClasses studentClasses3 = new StudentClasses("id1", Arrays.asList("clazz1"), Arrays.asList("clazz3"));
        final StudentClasses studentClasses4 = new StudentClasses("id2", Arrays.asList("clazz1"), Arrays.asList("clazz3"));
        final StudentClasses studentClasses5 = new StudentClasses("id1", Arrays.asList("clazz2"), Arrays.asList("clazz4"));

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
