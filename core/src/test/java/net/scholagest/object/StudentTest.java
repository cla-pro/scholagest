package net.scholagest.object;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StudentTest {
    @Test
    public void testEquals() {
        final Student student1 = new Student(null, null, null);
        final Student student2 = new Student(null, "firstName", "lastName");
        final Student student3 = new Student(null, "otherFirstName", "otherLastName");
        final Student student4 = new Student("1", "firstName", "lastName");
        final Student student5 = new Student("1", "firstName", "lastName");

        assertFalse(student1.equals(null));
        assertFalse(student1.equals(new Object()));
        assertFalse(student1.equals(student2));
        assertFalse(student1.equals(student3));
        assertFalse(student1.equals(student4));
        assertFalse(student1.equals(student5));
        assertFalse(student2.equals(student3));
        assertFalse(student2.equals(student4));
        assertFalse(student2.equals(student5));
        assertFalse(student3.equals(student4));
        assertFalse(student3.equals(student5));
        assertTrue(student4.equals(student5));
    }
}
