package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.scholagest.business.UserBusinessBean;
import net.scholagest.object.User;
import net.scholagest.tester.utils.AbstractTestSuite;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

public class Test_001_LoginUsernameTest extends AbstractTestSuite {
    private static final String VALID_USERNAME = "validUsername";
    private static final String VALID_PASSWORD = "validPassword";

    @BeforeClass
    public static void setUpData() {
        // TODO insert one valid user
        UserBusinessBean.users.put(VALID_USERNAME, new User(VALID_USERNAME, VALID_USERNAME, VALID_PASSWORD, "", new ArrayList<String>(), null, null));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson(VALID_USERNAME, VALID_PASSWORD));
        assertEquals(HttpStatus.OK_200, response.getStatus());

        // TODO convert the response into a json and check the parameters
        // separately
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
