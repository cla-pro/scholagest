package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@link Exam}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ExamTest {
    @Test
    public void testConstructorCopy() {
        final Exam toCopy = new Exam("id", "name", 3, "branchPeriod");
        final Exam copied = new Exam(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getName(), copied.getName());
        assertEquals(toCopy.getCoeff(), copied.getCoeff());
        assertEquals(toCopy.getBranchPeriod(), copied.getBranchPeriod());
    }

    @Test
    public void testEquals() {
        final Exam branch1 = new Exam(null, null, 1, null);
        final Exam branch2 = new Exam(null, "name", 2, "branchPeriod1");
        final Exam branch3 = new Exam("1", "otherName", 4, "branchPeriod2");
        final Exam branch4 = new Exam("1", "name", 2, "branchPeriod1");
        final Exam branch5 = new Exam("1", "name", 2, "branchPeriod1");

        assertFalse(branch1.equals(null));
        assertFalse(branch1.equals(new Object()));
        assertFalse(branch1.equals(branch2));
        assertFalse(branch1.equals(branch3));
        assertFalse(branch1.equals(branch4));
        assertFalse(branch1.equals(branch5));
        assertFalse(branch2.equals(branch3));
        assertFalse(branch2.equals(branch4));
        assertFalse(branch2.equals(branch5));
        assertFalse(branch3.equals(branch4));
        assertFalse(branch3.equals(branch5));
        assertTrue(branch4.equals(branch5));
    }
}
