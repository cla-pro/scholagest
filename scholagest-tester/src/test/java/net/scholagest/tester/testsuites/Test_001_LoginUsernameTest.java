package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.JsonObject;
import net.scholagest.tester.utils.matcher.UUIDMatcher;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class Test_001_LoginUsernameTest extends AbstractTestSuite {
    private static final String VALID_USERNAME = "validUsername";
    private static final String VALID_PASSWORD = "validPassword";

    private UserEntity userEntity;

    @Before
    public void setUpData() {
        final TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFirstname("firstname");
        teacherEntity.setLastname("lastname");

        userEntity = new UserEntity();
        userEntity.setTeacher(teacherEntity);
        userEntity.setUsername(VALID_USERNAME);
        userEntity.setPassword(VALID_PASSWORD);
        userEntity.setRole("admin");

        persistInTransaction(teacherEntity, userEntity);
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson(VALID_USERNAME, VALID_PASSWORD));
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("token", new UUIDMatcher(), "user", "" + userEntity.getId());
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testLoginUnknowUser() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson("invalidUsername", VALID_PASSWORD));
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
    }

    @Test
    public void testLoginWrongPassword() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson(VALID_USERNAME, "invalidPassword"));
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
    }

    private String buildLoginJson(final String username, final String password) {
        return "{ username: '" + username + "', password: '" + password + "' }";
    }
}
