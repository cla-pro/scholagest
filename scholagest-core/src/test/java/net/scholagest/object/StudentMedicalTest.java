package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@link StudentMedical}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentMedicalTest {
    @Test
    public void testConstructorCopy() {
        final StudentMedical toCopy = new StudentMedical("id", "Dr. J. Büschi");
        final StudentMedical copied = new StudentMedical(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getDoctor(), copied.getDoctor());
    }

    @Test
    public void testEquals() {
        final StudentMedical studentMedical1 = new StudentMedical(null, null);
        final StudentMedical studentMedical2 = new StudentMedical("id1", "doctor1");
        final StudentMedical studentMedical3 = new StudentMedical("id1", "doctor1");
        final StudentMedical studentMedical4 = new StudentMedical("id2", "doctor1");
        final StudentMedical studentMedical5 = new StudentMedical("id1", "doctor2");

        assertFalse(studentMedical1.equals(null));
        assertFalse(studentMedical1.equals(new Object()));
        assertFalse(studentMedical1.equals(studentMedical2));
        assertFalse(studentMedical1.equals(studentMedical3));
        assertFalse(studentMedical1.equals(studentMedical4));
        assertFalse(studentMedical1.equals(studentMedical5));
        assertTrue(studentMedical2.equals(studentMedical3));
        assertFalse(studentMedical2.equals(studentMedical4));
        assertFalse(studentMedical2.equals(studentMedical5));
        assertFalse(studentMedical3.equals(studentMedical4));
        assertFalse(studentMedical3.equals(studentMedical5));
        assertFalse(studentMedical4.equals(studentMedical5));
    }
}
