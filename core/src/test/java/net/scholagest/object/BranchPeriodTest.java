package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link BranchPeriodTest}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchPeriodTest {
    @Test
    public void testConstructorCopy() {
        final BranchPeriod toCopy = new BranchPeriod("id", "branch", "period", Arrays.asList("exam1", "exam2"), Arrays.asList("studentResult1",
                "studentResult2"));
        final BranchPeriod copied = new BranchPeriod(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getBranch(), copied.getBranch());
        assertEquals(toCopy.getPeriod(), copied.getPeriod());
        assertEquals(toCopy.getExams(), copied.getExams());
        assertEquals(toCopy.getStudentResults(), copied.getStudentResults());
    }

    @Test
    public void testEquals() {
        final BranchPeriod branchPeriod1 = new BranchPeriod(null, null, null, new ArrayList<String>(), new ArrayList<String>());
        final BranchPeriod branchPeriod2 = new BranchPeriod(null, "branch1", "period1", new ArrayList<String>(), new ArrayList<String>());
        final BranchPeriod branchPeriod3 = new BranchPeriod("1", "branch2", "period2", new ArrayList<String>(), new ArrayList<String>());
        final BranchPeriod branchPeriod4 = new BranchPeriod("1", "branch1", "period1", new ArrayList<String>(), new ArrayList<String>());
        final BranchPeriod branchPeriod5 = new BranchPeriod("1", "branch1", "period1", new ArrayList<String>(), new ArrayList<String>());

        assertFalse(branchPeriod1.equals(null));
        assertFalse(branchPeriod1.equals(new Object()));
        assertFalse(branchPeriod1.equals(branchPeriod2));
        assertFalse(branchPeriod1.equals(branchPeriod3));
        assertFalse(branchPeriod1.equals(branchPeriod4));
        assertFalse(branchPeriod1.equals(branchPeriod5));
        assertFalse(branchPeriod2.equals(branchPeriod3));
        assertFalse(branchPeriod2.equals(branchPeriod4));
        assertFalse(branchPeriod2.equals(branchPeriod5));
        assertFalse(branchPeriod3.equals(branchPeriod4));
        assertFalse(branchPeriod3.equals(branchPeriod5));
        assertTrue(branchPeriod4.equals(branchPeriod5));
    }
}
