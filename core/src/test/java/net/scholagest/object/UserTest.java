package net.scholagest.object;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Test class for {@see User}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class UserTest {
    @Test
    public void testEquals() {
        final User user1 = new User(null, null, null, null, null, null);
        final User user2 = new User("1", null, null, null, null, new Teacher("1", null, null, null));
        final User user3 = new User("2", null, null, null, null, new Teacher("1", null, null, null));
        final User user4 = new User("1", null, null, null, null, new Teacher("2", null, null, null));
        final User user5 = new User("1", null, null, null, null, new Teacher("1", null, null, null));
        final User user6 = new User("1", "username", "password", "ADMIN", new ArrayList<String>(), new Teacher("1", null, null, null));

        assertFalse(user1.equals(null));
        assertFalse(user1.equals(new Object()));
        assertFalse(user1.equals(user2));
        assertFalse(user1.equals(user3));
        assertFalse(user1.equals(user4));
        assertFalse(user1.equals(user5));
        assertFalse(user2.equals(user3));
        assertFalse(user2.equals(user4));
        assertTrue(user2.equals(user5));
        assertFalse(user3.equals(user4));
        assertFalse(user3.equals(user5));
        assertFalse(user4.equals(user5));
        assertTrue(user5.equals(user6));
    }
}
