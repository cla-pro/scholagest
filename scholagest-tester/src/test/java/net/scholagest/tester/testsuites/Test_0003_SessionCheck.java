package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.scholagest.db.entity.SessionEntity;
import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.UrlParameter;
import net.scholagest.tester.utils.creator.SessionEntityCreator;
import net.scholagest.tester.utils.creator.TeacherEntityCreator;
import net.scholagest.tester.utils.creator.UserEntityCreator;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class Test_0003_SessionCheck extends AbstractTestSuite {
    private static final String VALID_SESSION_TOKEN = "validSessionToken";
    private static final String INVALID_SESSION_TOKEN = "invalidSessionToken";
    private static final String EXPIRED_SESSION_TOKEN = "expiredSessionToken";

    private UserEntity userEntity;

    @Before
    public void setUpData() {
        final TeacherEntity teacherEntity = TeacherEntityCreator.createTeacherEntity("firstname", "lastname", null);
        final TeacherDetailEntity teacherDetailEntity = TeacherEntityCreator.createTeacherDetailEntity("", "", "", teacherEntity);
        userEntity = UserEntityCreator.createUserEntity("username", "password", "admin", teacherEntity);

        final SessionEntity validSessionEntity = SessionEntityCreator.createSessionEntity(VALID_SESSION_TOKEN, new DateTime().plusHours(2),
                userEntity);
        final SessionEntity expiredSessionEntity = SessionEntityCreator.createSessionEntity(EXPIRED_SESSION_TOKEN, new DateTime().minusHours(2),
                userEntity);

        persistInTransaction(teacherEntity);
        teacherEntity.setTeacherDetail(teacherDetailEntity);
        persistInTransaction(teacherDetailEntity);
        persistInTransaction(userEntity, validSessionEntity, expiredSessionEntity);
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final ContentResponse response = callGET("/services/teachers", new ArrayList<UrlParameter>(), VALID_SESSION_TOKEN);
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }

    @Test
    public void testLoginUnknownSession() throws Exception {
        final ContentResponse response = callGET("/services/teachers", new ArrayList<UrlParameter>(), INVALID_SESSION_TOKEN);
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
    }

    @Test
    public void testLoginExpiredSession() throws Exception {
        final ContentResponse response = callGET("/services/teachers", new ArrayList<UrlParameter>(), EXPIRED_SESSION_TOKEN);
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
    }

    @Test
    public void testLoginMissingSessionToken() throws Exception {
        final ContentResponse response = callGET("/services/teachers", new ArrayList<UrlParameter>(), "");
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
    }
}
