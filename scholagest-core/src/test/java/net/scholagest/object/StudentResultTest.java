package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link StudentResultTest}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class StudentResultTest {
    @Test
    public void testConstructorCopy() {
        final StudentResult toCopy = new StudentResult("id", "student", "branchPeriod", Arrays.asList("result1", "result2"), "mean", false);
        final StudentResult copied = new StudentResult(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getStudent(), copied.getStudent());
        assertEquals(toCopy.isActive(), copied.isActive());
        assertEquals(toCopy.getMean(), copied.getMean());
        assertEquals(toCopy.getBranchPeriod(), copied.getBranchPeriod());
        assertEquals(toCopy.getResults(), copied.getResults());
    }

    @Test
    public void testEquals() {
        final StudentResult studentResult1 = new StudentResult(null, null, null, new ArrayList<String>(), null, false);
        final StudentResult studentResult2 = new StudentResult(null, "student1", "branchPeriod1", new ArrayList<String>(), "mean1", false);
        final StudentResult studentResult3 = new StudentResult("1", "student2", "branchPeriod2", new ArrayList<String>(), "mean2", true);
        final StudentResult studentResult4 = new StudentResult("1", "student1", "branchPeriod1", new ArrayList<String>(), "mean1", false);
        final StudentResult studentResult5 = new StudentResult("1", "student1", "branchPeriod1", new ArrayList<String>(), "mean1", false);

        assertFalse(studentResult1.equals(null));
        assertFalse(studentResult1.equals(new Object()));
        assertFalse(studentResult1.equals(studentResult2));
        assertFalse(studentResult1.equals(studentResult3));
        assertFalse(studentResult1.equals(studentResult4));
        assertFalse(studentResult1.equals(studentResult5));
        assertFalse(studentResult2.equals(studentResult3));
        assertFalse(studentResult2.equals(studentResult4));
        assertFalse(studentResult2.equals(studentResult5));
        assertFalse(studentResult3.equals(studentResult4));
        assertFalse(studentResult3.equals(studentResult5));
        assertTrue(studentResult4.equals(studentResult5));
    }
}
