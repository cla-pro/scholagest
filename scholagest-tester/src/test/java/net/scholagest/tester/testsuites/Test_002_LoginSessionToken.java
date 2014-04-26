package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;
import net.scholagest.db.entity.SessionEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.JsonObject;
import net.scholagest.tester.utils.matcher.UUIDMatcher;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class Test_002_LoginSessionToken extends AbstractTestSuite {
    private static final String VALID_SESSION_TOKEN = "validSessionToken";
    private static final String EXPIRED_SESSION_TOKEN = "expiredSessionToken";

    private UserEntity userEntity;

    @Before
    public void setUpData() {
        final TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFirstname("firstname");
        teacherEntity.setLastname("lastname");

        userEntity = new UserEntity();
        userEntity.setTeacher(teacherEntity);
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setRole("admin");

        final SessionEntity validSessionEntity = new SessionEntity();
        validSessionEntity.setExpirationDate(new DateTime().plusHours(2));
        validSessionEntity.setId(VALID_SESSION_TOKEN);
        validSessionEntity.setUser(userEntity);

        final SessionEntity expiredSessionEntity = new SessionEntity();
        expiredSessionEntity.setExpirationDate(new DateTime().minusHours(2));
        expiredSessionEntity.setId(EXPIRED_SESSION_TOKEN);
        expiredSessionEntity.setUser(userEntity);

        persistInTransaction(teacherEntity);
        persistInTransaction(userEntity);
        persistInTransaction(validSessionEntity);
        persistInTransaction(expiredSessionEntity);
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson(VALID_SESSION_TOKEN));
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("token", new UUIDMatcher(), "user", "" + userEntity.getId());
        jsonObject.assertEquals(response.getContentAsString());
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
