package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;
import net.scholagest.tester.AbstractTestSuite;

import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.HttpStatus;
import org.mortbay.jetty.client.ContentExchange;

public class Test_002_LoginSessionToken extends AbstractTestSuite {
    private static final String VALID_SESSION_TOKEN = "validSessionToken";
    private static final String EXPIRED_SESSION_TOKEN = "invalidSessionToken";

    @Override
    @Before
    public void setUp() {
        // TODO Create one valid session
        // TODO Create one expired session
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final ContentExchange response = callPOST("/scholagest-app/services/login", buildLoginJson(VALID_SESSION_TOKEN));
        assertEquals(HttpStatus.ORDINAL_200_OK, response.getResponseStatus());

        // TODO convert the response into a json and check the parameters
        // separately
    }

    @Test
    public void testLoginUnknownSession() throws Exception {
        final ContentExchange response = callPOST("/scholagest-app/services/login", buildLoginJson("unknownSession"));
        assertEquals(HttpStatus.ORDINAL_401_Unauthorized, response.getResponseStatus());
    }

    @Test
    public void testLoginExpiredSession() throws Exception {
        final ContentExchange response = callPOST("/scholagest-app/services/login", buildLoginJson(EXPIRED_SESSION_TOKEN));
        assertEquals(HttpStatus.ORDINAL_401_Unauthorized, response.getResponseStatus());
    }

    private String buildLoginJson(final String token) {
        return "{ token: '" + token + "' }";
    }
}
