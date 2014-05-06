package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;
import net.scholagest.db.entity.SessionEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.JsonObject;
import net.scholagest.tester.utils.creator.SessionEntityCreator;
import net.scholagest.tester.utils.creator.TeacherEntityCreator;
import net.scholagest.tester.utils.creator.UserEntityCreator;
import net.scholagest.tester.utils.matcher.UUIDMatcher;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class Test_0002_LoginSessionToken extends AbstractTestSuite {
    private static final String VALID_SESSION_TOKEN = "validSessionToken";
    private static final String EXPIRED_SESSION_TOKEN = "expiredSessionToken";

    private UserEntity userEntity;

    @Before
    public void setUpData() {
        final TeacherEntity teacherEntity = TeacherEntityCreator.createTeacherEntity("firstname", "lastname", null);
        userEntity = UserEntityCreator.createUserEntity("username", "password", "admin", teacherEntity);

        final SessionEntity validSessionEntity = SessionEntityCreator.createSessionEntity(VALID_SESSION_TOKEN, new DateTime().plusHours(2),
                userEntity);
        final SessionEntity expiredSessionEntity = SessionEntityCreator.createSessionEntity(EXPIRED_SESSION_TOKEN, new DateTime().minusHours(2),
                userEntity);

        persistInTransaction(teacherEntity, userEntity, validSessionEntity, expiredSessionEntity);
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson(VALID_SESSION_TOKEN), null);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("token", new UUIDMatcher(), "user", "" + userEntity.getId());
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testLoginUnknownSessionToken() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson("unknownSession"), null);
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
    }

    @Test
    public void testLoginExpiredSession() throws Exception {
        final ContentResponse response = callPOST("/services/login", buildLoginJson(EXPIRED_SESSION_TOKEN), null);
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
    }

    @Test
    public void testLoginMissingSessionToken() throws Exception {
        final ContentResponse response = callPOST("/services/login", "{}", null);
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
    }

    private String buildLoginJson(final String token) {
        return "{ token: '" + token + "' }";
    }
}
