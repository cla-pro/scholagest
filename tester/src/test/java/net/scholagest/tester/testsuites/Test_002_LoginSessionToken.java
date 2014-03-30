package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;
import net.scholagest.tester.AbstractTestSuite;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

public class Test_002_LoginSessionToken extends AbstractTestSuite {
    private static final String VALID_SESSION_TOKEN = "validSessionToken";
    private static final String EXPIRED_SESSION_TOKEN = "invalidSessionToken";

    @BeforeClass
    public static void setUpData() {
        // TODO Create one valid session
        // TODO Create one expired session
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson(VALID_SESSION_TOKEN));
        assertEquals(HttpStatus.OK_200, response.getStatus());

        // TODO convert the response into a json and check the parameters
        // separately
    }

    @Test
    public void testLoginUnknownSession() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson("unknownSession"));
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
    }

    @Test
    public void testLoginExpiredSession() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson(EXPIRED_SESSION_TOKEN));
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
    }

    private String buildLoginJson(final String token) {
        return "{ token: '" + token + "' }";
    }
}
