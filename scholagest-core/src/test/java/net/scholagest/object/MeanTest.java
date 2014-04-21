package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@link Mean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class MeanTest {
    @Test
    public void testConstructorCopy() {
        final Mean toCopy = new Mean("id", "3.5", "studentResult");
        final Mean copied = new Mean(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getGrade(), copied.getGrade());
        assertEquals(toCopy.getStudentResult(), copied.getStudentResult());
    }

    @Test
    public void testEquals() {
        final Mean mean1 = new Mean(null, null, null);
        final Mean mean2 = new Mean(null, "3.5", "studentResutl1");
        final Mean mean3 = new Mean("1", "4.5", "studentMean2");
        final Mean mean4 = new Mean("1", "3.5", "studentMean1");
        final Mean mean5 = new Mean("1", "3.5", "studentMean1");

        assertFalse(mean1.equals(null));
        assertFalse(mean1.equals(new Object()));
        assertFalse(mean1.equals(mean2));
        assertFalse(mean1.equals(mean3));
        assertFalse(mean1.equals(mean4));
        assertFalse(mean1.equals(mean5));
        assertFalse(mean2.equals(mean3));
        assertFalse(mean2.equals(mean4));
        assertFalse(mean2.equals(mean5));
        assertFalse(mean3.equals(mean4));
        assertFalse(mean3.equals(mean5));
        assertTrue(mean4.equals(mean5));
    }
}
