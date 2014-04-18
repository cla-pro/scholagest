package net.scholagest.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.scholagest.ReflectionUtils;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.object.User;

import org.junit.Test;

/**
 * Test class for {@link UserEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class UserEntityConverterTest {
    @Test
    public void testConvertToUser() {
        final TeacherEntity teacherEntity = new TeacherEntity();
        ReflectionUtils.setField(teacherEntity, "id", Long.valueOf(2L));

        final UserEntity userEntity = new UserEntity();
        ReflectionUtils.setField(userEntity, "id", Long.valueOf(3L));
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setRole("ADMIN");
        userEntity.setTeacher(teacherEntity);

        final UserEntityConverter testee = new UserEntityConverter();
        final User user = testee.convertToUser(userEntity);

        assertEquals("" + userEntity.getId(), user.getId());
        assertEquals(userEntity.getUsername(), user.getUsername());
        assertEquals(userEntity.getPassword(), user.getPassword());
        assertEquals(userEntity.getRole(), user.getRole());
        assertEquals("" + userEntity.getTeacher().getId(), user.getTeacher());
        assertTrue(user.getPermissions().isEmpty());
        assertNull(user.getClazz());
    }
}
