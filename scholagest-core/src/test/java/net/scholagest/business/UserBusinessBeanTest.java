package net.scholagest.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.UserDaoLocal;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.object.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link UserBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class UserBusinessBeanTest {
    @Mock
    private UserDaoLocal userDao;

    @InjectMocks
    private final UserBusinessLocal testee = new UserBusinessBean();

    @Test
    public void testGetUser() {
        final UserEntity userEntity = createUserEntity();
        when(userDao.getUserEntityById(eq(3L))).thenReturn(userEntity);

        assertNull(testee.getUser(2L));
        verify(userDao).getUserEntityById(eq(2L));

        final User user = testee.getUser(3L);
        assertNotNull(user);
        verify(userDao).getUserEntityById(eq(3L));
    }

    @Test
    public void testGetUserByUsername() {
        final UserEntity userEntity = createUserEntity();
        when(userDao.getUserEntityByUsername(eq("username"))).thenReturn(userEntity);

        assertNull(testee.getUserByUsername("wrong"));
        verify(userDao).getUserEntityByUsername(eq("wrong"));

        final User user = testee.getUserByUsername("username");
        assertNotNull(user);
        verify(userDao).getUserEntityByUsername(eq("username"));
    }

    @Test
    public void testSaveUser() {
        final UserEntity userEntity = createUserEntity();
        when(userDao.getUserEntityById(eq(3L))).thenReturn(userEntity);

        final User invalid = new User("2", null, null, null, null, null, null);
        assertNull(testee.saveUser(invalid));

        final User toSave = new User("3", "newUsername", "newPassword", "TEACHER", new ArrayList<String>(), "5L", null);
        final User user = testee.saveUser(toSave);

        assertNotNull(user);
        verify(userDao).getUserEntityById(eq(3L));
    }

    private UserEntity createUserEntity() {
        final TeacherEntity teacherEntity = new TeacherEntity();
        ReflectionUtils.setField(teacherEntity, "id", Long.valueOf(2L));

        final UserEntity userEntity = new UserEntity();
        ReflectionUtils.setField(userEntity, "id", Long.valueOf(3L));
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setRole("ADMIN");
        userEntity.setTeacher(teacherEntity);

        return userEntity;
    }
}
