package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import net.scholagest.app.rest.ws.objects.UserJson;
import net.scholagest.object.User;

import org.junit.Test;

/**
 * Test class for {@ UserJsonConverter}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class UserJsonConverterTest {
    @Test
    public void testConvertToUserJsonEmptyPermissionList() {
        final User user = new User("1", "username", "password", "ADMIN", new ArrayList<String>(), "1", "1");
        final UserJson userJson = new UserJsonConverter().convertToUserJson(user);

        assertEquals(user.getId(), userJson.getId());
        assertEquals(user.getRole(), userJson.getRole());
        assertEquals(user.getTeacher(), userJson.getTeacher());
        assertEquals(user.getClazz(), userJson.getClazz());
    }

    @Test
    public void testConvertToUserJsonFullyPermissionList() {
        final User user = new User("1", "username", "password", "ADMIN", Arrays.asList("1", "2"), "1", "1");
        final UserJson userJson = new UserJsonConverter().convertToUserJson(user);

        assertEquals(user.getId(), userJson.getId());
        assertEquals(user.getRole(), userJson.getRole());
        assertEquals(user.getTeacher(), userJson.getTeacher());
        assertEquals(user.getClazz(), userJson.getClazz());
    }
}
