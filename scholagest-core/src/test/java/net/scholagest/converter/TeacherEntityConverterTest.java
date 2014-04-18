package net.scholagest.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

import org.junit.Test;

/**
 * Test class for {@link TeacherEntityConverter}
 * 
 * @author CLA
 * @since 0.15.0
 */
public class TeacherEntityConverterTest {
    @Test
    public void testConvertToTeacherList() {
        final TeacherEntity teacherEntity1 = createTeacherEntity(1L, "firstname1", "lastname1", createTeacherDetailEntity(11L, null, null, null));
        final TeacherEntity teacherEntity2 = createTeacherEntity(2L, "firstname2", "lastname2", createTeacherDetailEntity(12L, null, null, null));
        final List<TeacherEntity> toConvert = Arrays.asList(teacherEntity1, teacherEntity2);

        final TeacherEntityConverter testee = spy(new TeacherEntityConverter());
        final List<Teacher> converted = testee.convertToTeacherList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final TeacherEntity teacherEntity : toConvert) {
            verify(testee).convertToTeacher(eq(teacherEntity));
        }
    }

    @Test
    public void testConvertToTeacher() {
        final TeacherDetailEntity teacherDetailEntity = createTeacherDetailEntity(11L, "address", "email", "phone");
        final TeacherEntity teacherEntity = createTeacherEntity(1L, "firstname", "lastname", teacherDetailEntity);

        final TeacherEntityConverter testee = spy(new TeacherEntityConverter());
        final Teacher converted = testee.convertToTeacher(teacherEntity);

        assertEquals(teacherEntity.getId().toString(), converted.getId());
        assertEquals(teacherEntity.getFirstname(), converted.getFirstname());
        assertEquals(teacherEntity.getLastname(), converted.getLastname());

        final TeacherDetail convertedDetail = converted.getDetail();
        assertEquals(teacherDetailEntity.getId().toString(), convertedDetail.getId());
        assertEquals(teacherDetailEntity.getAddress(), convertedDetail.getAddress());
        assertEquals(teacherDetailEntity.getEmail(), convertedDetail.getEmail());
        assertEquals(teacherDetailEntity.getPhone(), convertedDetail.getPhone());
    }

    @Test
    public void testConvertToTeacherEntity() {
        final TeacherDetail teacherDetail = new TeacherDetail("3", "address", "email", "phone");
        final Teacher teacher = new Teacher("4", "firstname", "lastname", teacherDetail);

        final TeacherEntityConverter testee = spy(new TeacherEntityConverter());
        final TeacherEntity converted = testee.convertToTeacherEntity(teacher);

        assertNull(converted.getId());
        assertEquals(teacher.getFirstname(), converted.getFirstname());
        assertEquals(teacher.getLastname(), converted.getLastname());

        final TeacherDetailEntity convertedDetail = converted.getTeacherDetail();
        assertNull(convertedDetail.getId());
        assertEquals(teacherDetail.getAddress(), convertedDetail.getAddress());
        assertEquals(teacherDetail.getEmail(), convertedDetail.getEmail());
        assertEquals(teacherDetail.getPhone(), convertedDetail.getPhone());
        assertEquals(converted, convertedDetail.getTeacher());
    }

    private TeacherEntity createTeacherEntity(final Long id, final String firstname, final String lastname,
            final TeacherDetailEntity teacherDetailEntity) {
        final TeacherEntity teacherEntity = new TeacherEntity();
        ReflectionUtils.setField(teacherEntity, "id", id);
        teacherEntity.setFirstname(firstname);
        teacherEntity.setLastname(lastname);
        teacherEntity.setTeacherDetail(teacherDetailEntity);

        return teacherEntity;
    }

    private TeacherDetailEntity createTeacherDetailEntity(final Long id, final String address, final String email, final String phone) {
        final TeacherDetailEntity teacherDetailEntity = new TeacherDetailEntity();
        ReflectionUtils.setField(teacherDetailEntity, "id", id);
        teacherDetailEntity.setAddress(address);
        teacherDetailEntity.setEmail(email);
        teacherDetailEntity.setPhone(phone);

        return teacherDetailEntity;
    }
}
