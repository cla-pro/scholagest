package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link Year}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class YearTest {
    @Test
    public void testConstructorCopy() {
        final Year toCopy = new Year("1", "name", false, Arrays.asList("1", "2"));
        final Year copied = new Year(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getName(), copied.getName());
        assertEquals(toCopy.isRunning(), copied.isRunning());
        assertEquals(toCopy.getClasses(), copied.getClasses());
    }

    @Test
    public void testEquals() {
        final Year year1 = new Year(null, null, false, null);
        final Year year2 = new Year(null, "name", true, null);
        final Year year3 = new Year("1", "otherName", false, null);
        final Year year4 = new Year("1", "name", true, null);
        final Year year5 = new Year("1", "name", true, null);

        assertFalse(year1.equals(null));
        assertFalse(year1.equals(new Object()));
        assertFalse(year1.equals(year2));
        assertFalse(year1.equals(year3));
        assertFalse(year1.equals(year4));
        assertFalse(year1.equals(year5));
        assertFalse(year2.equals(year3));
        assertFalse(year2.equals(year4));
        assertFalse(year2.equals(year5));
        assertFalse(year3.equals(year4));
        assertFalse(year3.equals(year5));
        assertTrue(year4.equals(year5));
    }
}
