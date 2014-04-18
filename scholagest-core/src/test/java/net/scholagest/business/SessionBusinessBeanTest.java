package net.scholagest.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.SessionDaoLocal;
import net.scholagest.dao.UserDaoLocal;
import net.scholagest.db.entity.SessionEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.object.Session;
import net.scholagest.object.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * Test class for {@link SessionBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionBusinessBeanTest {
    @Mock
    private SessionDaoLocal sessionDao;

    @Mock
    private UserDaoLocal userDao;

    @InjectMocks
    private final SessionBusinessLocal testee = new SessionBusinessBean();

    @Test
    public void testGetSession() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setTeacher(new TeacherEntity());

        final SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId("3");
        sessionEntity.setUser(userEntity);

        when(sessionDao.getSessionEntityById(eq("3"))).thenReturn(sessionEntity);

        assertNull(testee.getSession("2"));
        verify(sessionDao).getSessionEntityById(eq("2"));

        final Session session = testee.getSession("3");
        assertEquals("" + sessionEntity.getId(), session.getId());
        verify(sessionDao).getSessionEntityById(eq("3"));
    }

    @Test
    public void testCreateSession() {
        when(sessionDao.persistSessionEntity(any(SessionEntity.class))).thenAnswer(new Answer<SessionEntity>() {
            @Override
            public SessionEntity answer(final InvocationOnMock invocation) throws Throwable {
                return (SessionEntity) invocation.getArguments()[0];
            }
        });

        final UserEntity userEntity = new UserEntity();
        ReflectionUtils.setField(userEntity, "id", Long.valueOf(2L));
        userEntity.setTeacher(new TeacherEntity());

        when(userDao.getUserEntityById(eq(2L))).thenReturn(userEntity);

        final User user = new User("2", "username", "password", "ADMIN", new ArrayList<String>(), "2", null);

        final Session session = testee.createSession(user);
        assertNotNull(session.getId());
        assertEquals("" + userEntity.getId(), session.getUser().getId());

        verify(sessionDao).persistSessionEntity(any(SessionEntity.class));
        verify(userDao).getUserEntityById(eq(2L));
    }
}
