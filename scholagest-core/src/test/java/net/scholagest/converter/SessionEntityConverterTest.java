package net.scholagest.converter;

import static org.junit.Assert.assertEquals;
import net.scholagest.ReflectionUtils;
import net.scholagest.db.entity.SessionEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.object.Session;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Test class for {@link SessionEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class SessionEntityConverterTest {
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

        final SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId("3");
        sessionEntity.setExpirationDate(new DateTime());
        sessionEntity.setUser(userEntity);

        final SessionEntityConverter testee = new SessionEntityConverter();
        final Session session = testee.convertToSession(sessionEntity);

        assertEquals("" + sessionEntity.getId(), session.getId());
        assertEquals(sessionEntity.getExpirationDate().getMillis(), session.getExpirationDate().getMillis());
        assertEquals("" + sessionEntity.getUser().getId(), session.getUser().getId());
    }
}
