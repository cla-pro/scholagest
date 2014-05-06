package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@Period}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class PeriodTest {
    @Test
    public void testConstructorCopy() {
        final Period toCopy = new Period("1", "name", "clazz1", Arrays.asList("1", "2"));
        final Period copied = new Period(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getName(), copied.getName());
        assertEquals(toCopy.getClazz(), copied.getClazz());
        assertEquals(toCopy.getBranchPeriods(), copied.getBranchPeriods());
    }

    @Test
    public void testEquals() {
        final Period period1 = new Period(null, null, null, new ArrayList<String>());
        final Period period2 = new Period(null, "name", "clazz1", new ArrayList<String>());
        final Period period3 = new Period("1", "otherName", "clazz2", new ArrayList<String>());
        final Period period4 = new Period("1", "name", "clazz1", new ArrayList<String>());
        final Period period5 = new Period("1", "name", "clazz1", new ArrayList<String>());

        assertFalse(period1.equals(null));
        assertFalse(period1.equals(new Object()));
        assertFalse(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(period4));
        assertFalse(period1.equals(period5));
        assertFalse(period2.equals(period3));
        assertFalse(period2.equals(period4));
        assertFalse(period2.equals(period5));
        assertFalse(period3.equals(period4));
        assertFalse(period3.equals(period5));
        assertTrue(period4.equals(period5));
    }
}
