package net.scholagest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.scholagest.business.TeacherBusinessLocal;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;
import net.scholagest.utils.AbstractGuiceContextTest;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.subject.Subject;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TeacherServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private TeacherBusinessLocal teacherBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(TeacherBusinessLocal.class).toInstance(teacherBusiness);
        module.bind(TeacherServiceLocal.class).to(TeacherServiceBean.class);
    }

    private void setAdminSubject() {
        final Subject subject = mock(Subject.class);
        when(subject.hasRole(anyString())).thenReturn(true);
        when(subject.isPermitted(anyString())).thenReturn(true);

        ScholagestThreadLocal.setSubject(subject);
    }

    private void setNoRightSubject() {
        final Subject subject = mock(Subject.class);
        when(subject.hasRole(anyString())).thenReturn(false);
        when(subject.isPermitted(anyString())).thenReturn(false);

        ScholagestThreadLocal.setSubject(subject);
    }

    @Test
    public void testGetTeachers() {
        setAdminSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        final Teacher teacher1 = new Teacher("1", "FirstName1", "LastName1", new TeacherDetail("1", "address1", "email1", "phone1"));
        final Teacher teacher2 = new Teacher("2", "FirstName2", "LastName2", new TeacherDetail("2", "address2", "email2", "phone2"));
        final List<Teacher> expected = Arrays.asList(teacher1, teacher2);
        when(teacherBusiness.getTeachers()).thenReturn(expected);

        final List<Teacher> result = testee.getTeachers();

        assertEquals(expected, result);

        verify(teacherBusiness).getTeachers();
    }

    @Test
    public void testGetTeacherWrong() {
        setAdminSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        final List<Teacher> empty1 = testee.getTeacher(new ArrayList<String>());
        assertTrue(empty1.isEmpty());

        final List<Teacher> empty2 = testee.getTeacher(Arrays.asList("3"));
        assertTrue(empty2.isEmpty());
    }

    @Test
    public void testGetTeacherSingle() {
        setAdminSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        final Teacher teacher1 = new Teacher("1", "FirstName1", "LastName1", new TeacherDetail("1", "address1", "email1", "phone1"));
        final Teacher teacher2 = new Teacher("2", "FirstName2", "LastName2", new TeacherDetail("2", "address2", "email2", "phone2"));
        when(teacherBusiness.getTeacher("1")).thenReturn(teacher1);
        when(teacherBusiness.getTeacher("2")).thenReturn(teacher2);

        final List<Teacher> expected = Arrays.asList(teacher1);
        final List<Teacher> result = testee.getTeacher(Arrays.asList("1"));
        assertEquals(expected, result);

        verify(teacherBusiness).getTeacher(eq("1"));
        verify(teacherBusiness, never()).getTeacher(eq("2"));
    }

    @Test
    public void testGetTeacherMultiple() {
        setAdminSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        final Teacher teacher1 = new Teacher("1", "FirstName1", "LastName1", new TeacherDetail("1", "address1", "email1", "phone1"));
        final Teacher teacher2 = new Teacher("2", "FirstName2", "LastName2", new TeacherDetail("2", "address2", "email2", "phone2"));
        when(teacherBusiness.getTeacher("1")).thenReturn(teacher1);
        when(teacherBusiness.getTeacher("2")).thenReturn(teacher2);

        final List<Teacher> expected = Arrays.asList(teacher1, teacher2);
        final List<Teacher> result = testee.getTeacher(Arrays.asList("1", "2"));
        assertEquals(expected, result);

        verify(teacherBusiness).getTeacher(eq("1"));
        verify(teacherBusiness).getTeacher(eq("2"));
    }

    @Test
    public void testCreateTeacher() {
        setAdminSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        final Teacher teacher = new Teacher(null, "FirstName1", "LastName1", new TeacherDetail());
        final Teacher created = new Teacher("id", "FirstName1", "LastName1", new TeacherDetail("id", null, null, null));
        when(teacherBusiness.createTeacher(any(Teacher.class))).thenReturn(created);

        final Teacher response = testee.createTeacher(teacher);
        assertEquals(created, response);

        verify(teacherBusiness).createTeacher(eq(teacher));
    }

    @Test
    public void testSaveTeacher() {
        setAdminSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        final Teacher teacher = new Teacher(null, "FirstName1", "LastName1", new TeacherDetail());
        final String id = "id";
        final Teacher saved = new Teacher(id, "FirstName2", "LastName2", new TeacherDetail(id, null, null, null));
        when(teacherBusiness.saveTeacher(any(Teacher.class))).thenReturn(saved);

        final Teacher response = testee.saveTeacher(id, teacher);
        assertEquals(saved, response);

        verify(teacherBusiness).saveTeacher(argThat(new BaseMatcher<Teacher>() {
            @Override
            public boolean matches(final Object item) {
                final Teacher toSave = (Teacher) item;
                assertEquals(id, toSave.getId());
                return true;
            }

            @Override
            public void describeTo(final Description description) {}
        }));
    }

    @Test
    public void testGetTeacherDetail() {
        setAdminSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        final String id = "1";
        final TeacherDetail teacherDetail = new TeacherDetail(id, "address", "email", "phone");
        when(teacherBusiness.getTeacherDetail(eq(id))).thenReturn(teacherDetail);

        final TeacherDetail response = testee.getTeacherDetail("1");
        assertEquals(teacherDetail, response);
        verify(teacherBusiness).getTeacherDetail(eq(id));

        assertNull(testee.getTeacherDetail(null));
        assertNull(testee.getTeacherDetail("2"));
    }

    @Test
    public void testSaveTeacherDetail() {
        setAdminSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        final String id = "1";
        final TeacherDetail teacherDetail = new TeacherDetail(id, "address", "email", "phone");
        final TeacherDetail saved = new TeacherDetail(id, "savedA", "savedE", "savedP");
        when(teacherBusiness.saveTeacherDetail(eq(id), any(TeacherDetail.class))).thenReturn(saved);

        final TeacherDetail response = testee.saveTeacherDetail(id, teacherDetail);
        assertEquals(saved, response);

        verify(teacherBusiness).saveTeacherDetail(eq(id), eq(teacherDetail));
    }

    @Test
    public void testCreateTeacherAuthorization() {
        setNoRightSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        try {
            testee.createTeacher(new Teacher());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
        }
    }

    @Test
    public void testSaveTeacherAuthorization() {
        setNoRightSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        final String id = "id";
        try {
            testee.saveTeacher(id, new Teacher());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
            verify(ScholagestThreadLocal.getSubject()).isPermitted(id);
        }
    }

    @Test
    public void testSaveTeacherDetailAuthorization() {
        setNoRightSubject();
        final TeacherServiceLocal testee = getInstance(TeacherServiceLocal.class);

        final String id = "id";
        try {
            testee.saveTeacherDetail(id, new TeacherDetail());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
            verify(ScholagestThreadLocal.getSubject()).isPermitted(id);
        }
    }
}
