package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@link StudentPersonal}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentPersonalTest {
    @Test
    public void testConstructorCopy() {
        final StudentPersonal toCopy = new StudentPersonal("id", "street", "city", "postcode", "religion");
        final StudentPersonal copied = new StudentPersonal(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getStreet(), copied.getStreet());
        assertEquals(toCopy.getCity(), copied.getCity());
        assertEquals(toCopy.getPostcode(), copied.getPostcode());
        assertEquals(toCopy.getReligion(), copied.getReligion());
    }

    @Test
    public void testEquals() {
        final StudentPersonal studentPersonal1 = new StudentPersonal(null, null, null, null, null);
        final StudentPersonal studentPersonal2 = new StudentPersonal("id1", "street1", "city1", "postcode1", "religion1");
        final StudentPersonal studentPersonal3 = new StudentPersonal("id1", "street1", "city1", "postcode1", "religion1");
        final StudentPersonal studentPersonal4 = new StudentPersonal("id2", "street1", "city1", "postcode1", "religion1");
        final StudentPersonal studentPersonal5 = new StudentPersonal("id1", "street2", "city2", "postcode2", "religion2");

        assertFalse(studentPersonal1.equals(null));
        assertFalse(studentPersonal1.equals(new Object()));
        assertFalse(studentPersonal1.equals(studentPersonal2));
        assertFalse(studentPersonal1.equals(studentPersonal3));
        assertFalse(studentPersonal1.equals(studentPersonal4));
        assertFalse(studentPersonal1.equals(studentPersonal5));
        assertTrue(studentPersonal2.equals(studentPersonal3));
        assertFalse(studentPersonal2.equals(studentPersonal4));
        assertFalse(studentPersonal2.equals(studentPersonal5));
        assertFalse(studentPersonal3.equals(studentPersonal4));
        assertFalse(studentPersonal3.equals(studentPersonal5));
        assertFalse(studentPersonal4.equals(studentPersonal5));
    }
}
