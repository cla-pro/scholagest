package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@Student}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentTest {
    @Test
    public void testConstructorCopy() {
        final Student toCopy = new Student("1", "firstName", "lastName", new StudentPersonal("1", "address", "city", "postcode", "religion"),
                new StudentMedical("1", "doctor"));
        final Student copied = new Student(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getFirstName(), copied.getFirstName());
        assertEquals(toCopy.getLastName(), copied.getLastName());
        assertEquals(toCopy.getStudentPersonal(), copied.getStudentPersonal());
        assertEquals(toCopy.getStudentMedical(), copied.getStudentMedical());
    }

    @Test
    public void testEquals() {
        final Student student1 = new Student(null, null, null, null, null);
        final Student student2 = new Student(null, "firstName", "lastName", null, null);
        final Student student3 = new Student(null, "otherFirstName", "otherLastName", null, null);
        final Student student4 = new Student("1", "firstName", "lastName", null, null);
        final Student student5 = new Student("1", "firstName", "lastName", null, null);
        final Student student6 = new Student("1", "firstName", "lastName", new StudentPersonal("1", "address", "city", "postcode", "religion"),
                new StudentMedical("1", "doctor"));

        assertFalse(student1.equals(null));
        assertFalse(student1.equals(new Object()));
        assertFalse(student1.equals(student2));
        assertFalse(student1.equals(student3));
        assertFalse(student1.equals(student4));
        assertFalse(student1.equals(student5));
        assertFalse(student1.equals(student6));
        assertFalse(student2.equals(student3));
        assertFalse(student2.equals(student4));
        assertFalse(student2.equals(student5));
        assertFalse(student2.equals(student6));
        assertFalse(student3.equals(student4));
        assertFalse(student3.equals(student5));
        assertFalse(student3.equals(student6));
        assertTrue(student4.equals(student5));
        assertTrue(student4.equals(student6));
        assertTrue(student5.equals(student6));
    }
}
