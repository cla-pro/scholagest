package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;
import net.scholagest.tester.AbstractTestSuite;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.HttpStatus;
import org.mortbay.jetty.client.ContentExchange;

public class Test_001_LoginUsername extends AbstractTestSuite {
    private static final String VALID_USERNAME = "validUsername";
    private static final String VALID_PASSWORD = "validPassword";

    @Override
    @BeforeClass
    public void setUp() {
        // TODO insert one valid user
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final ContentExchange response = callPOST("/scholagest-app/services/login", buildLoginJson(VALID_USERNAME, VALID_PASSWORD));
        assertEquals(HttpStatus.ORDINAL_200_OK, response.getResponseStatus());

        // TODO convert the response into a json and check the parameters
        // separately
    }

    @Test
    public void testLoginUnknowUser() throws Exception {
        final ContentExchange response = callPOST("/scholagest-app/services/login", buildLoginJson("invalidUsername", VALID_PASSWORD));
        assertEquals(HttpStatus.ORDINAL_401_Unauthorized, response.getResponseStatus());
    }

    @Test
    public void testLoginWrongPassword() throws Exception {
        final ContentExchange response = callPOST("/scholagest-app/services/login", buildLoginJson(VALID_USERNAME, "invalidPassword"));
        assertEquals(HttpStatus.ORDINAL_401_Unauthorized, response.getResponseStatus());
    }

    private String buildLoginJson(final String username, final String password) {
        return "{ username:'" + username + "', password:'" + password + "' }";
    }
}
