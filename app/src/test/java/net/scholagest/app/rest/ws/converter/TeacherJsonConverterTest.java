package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.TeacherDetailJson;
import net.scholagest.app.rest.ws.objects.TeacherJson;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

import org.junit.Test;

/**
 * Test class for {@link TeacherJsonConverter}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class TeacherJsonConverterTest {
    @Test
    public void testConvertToTeacherJsonList() {
        final Teacher teacher1 = new Teacher("1", "Pierre1", "Dupont1", new TeacherDetail("1", "address1", "email1", "phone1"));
        final Teacher teacher2 = new Teacher("2", "Pierre2", "Dupont2", new TeacherDetail("2", "address2", "email2", "phone2"));
        final List<Teacher> teacherList = Arrays.asList(teacher1, teacher2);

        final TeacherJsonConverter testee = spy(new TeacherJsonConverter());
        final List<TeacherJson> teacherJsonList = testee.convertToTeacherJsonList(teacherList);

        assertEquals(teacherList.size(), teacherJsonList.size());
        for (final Teacher teacher : teacherList) {
            verify(testee).convertToTeacherJson(eq(teacher));
        }
    }

    @Test
    public void testConvertToTeacherJson() {
        final Teacher teacher = new Teacher("1", "Pierre", "Dupont", new TeacherDetail("1", "address", "email", "phone"));
        final TeacherJson teacherJson = new TeacherJsonConverter().convertToTeacherJson(teacher);

        assertEquals(teacher.getId(), teacherJson.getId());
        assertEquals(teacher.getFirstname(), teacherJson.getFirstName());
        assertEquals(teacher.getLastname(), teacherJson.getLastName());
        assertEquals(teacher.getDetail().getId(), teacherJson.getDetail());
    }

    @Test
    public void testConvertToTeacherDetailJson() {
        final TeacherDetail teacherDetail = new TeacherDetail("1", "address", "email", "phone");
        final TeacherDetailJson teacherDetailJson = new TeacherJsonConverter().convertToTeacherDetailJson(teacherDetail);

        assertEquals(teacherDetail.getId(), teacherDetailJson.getId());
        assertEquals(teacherDetail.getAddress(), teacherDetailJson.getAddress());
        assertEquals(teacherDetail.getEmail(), teacherDetailJson.getEmail());
        assertEquals(teacherDetail.getPhone(), teacherDetailJson.getPhone());
    }

    @Test
    public void testConvertToTeacher() {
        final TeacherJson teacherJson = new TeacherJson("1", "Pierre", "Dupont", "1");
        final Teacher teacher = new TeacherJsonConverter().convertToTeacher(teacherJson);

        assertEquals(teacherJson.getId(), teacher.getId());
        assertEquals(teacherJson.getFirstName(), teacher.getFirstname());
        assertEquals(teacherJson.getLastName(), teacher.getLastname());
        // A default empty teacher detail is created
        assertEquals(new TeacherDetail(), teacher.getDetail());
    }

    @Test
    public void testConvertToTeacherDetail() {
        final TeacherDetailJson teacherDetailJson = new TeacherDetailJson("1", "address", "email", "phone");
        final TeacherDetail teacherDetail = new TeacherJsonConverter().convertToTeacherDetail(teacherDetailJson);

        assertEquals(teacherDetailJson.getId(), teacherDetail.getId());
        assertEquals(teacherDetailJson.getAddress(), teacherDetail.getAddress());
        assertEquals(teacherDetailJson.getEmail(), teacherDetail.getEmail());
        assertEquals(teacherDetailJson.getPhone(), teacherDetail.getPhone());
    }
}
