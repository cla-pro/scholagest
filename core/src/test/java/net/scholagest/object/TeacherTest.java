package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@link Teacher}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class TeacherTest {
    @Test
    public void testConstructorCopy() {
        final Teacher toCopy = new Teacher("1", "firstName", "lastName", new TeacherDetail("1", null, null, null));
        final Teacher copied = new Teacher(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getFirstName(), copied.getFirstName());
        assertEquals(toCopy.getLastName(), copied.getLastName());
        assertEquals(toCopy.getDetail().getId(), copied.getDetail().getId());
    }

    @Test
    public void testEquals() {
        final Teacher teacher1 = new Teacher(null, null, null, null);
        final Teacher teacher2 = new Teacher(null, "firstName", "lastName", new TeacherDetail());
        final Teacher teacher3 = new Teacher(null, "otherFirstName", "otherLastName", new TeacherDetail());
        final Teacher teacher4 = new Teacher("1", "firstName", "lastName", new TeacherDetail());
        final Teacher teacher5 = new Teacher("1", "firstName", "lastName", new TeacherDetail());

        assertFalse(teacher1.equals(null));
        assertFalse(teacher1.equals(new Object()));
        assertFalse(teacher1.equals(teacher2));
        assertFalse(teacher1.equals(teacher3));
        assertFalse(teacher1.equals(teacher4));
        assertFalse(teacher1.equals(teacher5));
        assertFalse(teacher2.equals(teacher3));
        assertFalse(teacher2.equals(teacher4));
        assertFalse(teacher2.equals(teacher5));
        assertFalse(teacher3.equals(teacher4));
        assertFalse(teacher3.equals(teacher5));
        assertTrue(teacher4.equals(teacher5));
    }
}
