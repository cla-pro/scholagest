package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link Branch}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchTest {
    @Test
    public void testConstructorCopy() {
        final Branch toCopy = new Branch("id", "name", false, "clazz", Arrays.asList("branchPeriod1", "branchPeriod2"));
        final Branch copied = new Branch(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getName(), copied.getName());
        assertEquals(toCopy.isNumerical(), copied.isNumerical());
        assertEquals(toCopy.getClazz(), copied.getClazz());
        assertEquals(toCopy.getBranchPeriods(), copied.getBranchPeriods());
    }

    @Test
    public void testEquals() {
        final Branch branch1 = new Branch(null, null, false, null, new ArrayList<String>());
        final Branch branch2 = new Branch(null, "name", false, "clazz1", new ArrayList<String>());
        final Branch branch3 = new Branch("1", "otherName", true, "clazz2", new ArrayList<String>());
        final Branch branch4 = new Branch("1", "name", false, "clazz1", new ArrayList<String>());
        final Branch branch5 = new Branch("1", "name", false, "clazz1", new ArrayList<String>());

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
