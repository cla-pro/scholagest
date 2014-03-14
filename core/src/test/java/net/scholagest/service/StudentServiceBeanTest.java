package net.scholagest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import net.scholagest.business.StudentBusinessLocal;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.Student;
import net.scholagest.utils.AbstractGuiceContextTest;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@see StudentServiceBean}
 * 
 * @author CLA
 * @since 0.13.0
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private StudentBusinessLocal studentBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(StudentBusinessLocal.class).toInstance(studentBusiness);
        module.bind(StudentServiceLocal.class).to(StudentServiceBean.class);
    }

    @Test
    public void testGetStudents() {
        setAdminSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final Student student1 = new Student("1", "FirstName1", "LastName1");
        final Student student2 = new Student("2", "FirstName2", "LastName2");
        final List<Student> expected = Arrays.asList(student1, student2);
        when(studentBusiness.getStudents()).thenReturn(expected);

        final List<Student> result = testee.getStudents();

        assertEquals(expected, result);

        verify(studentBusiness).getStudents();
    }

    @Test
    public void testGetStudent() {
        setAdminSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final Student expected = new Student("1", "firstName", "lastName");
        when(studentBusiness.getStudent("1")).thenReturn(expected);

        assertNull(testee.getStudent(null));
        verify(studentBusiness, never()).getStudent(anyString());

        assertNull(testee.getStudent("2"));
        verify(studentBusiness).getStudent(eq("2"));

        assertEquals(expected, testee.getStudent("1"));
        verify(studentBusiness).getStudent(eq("1"));
    }

    @Test
    public void testCreateStudent() {
        setAdminSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final Student created = new Student("1", "firstName", "lastName");
        when(studentBusiness.createStudent(any(Student.class))).thenReturn(created);

        assertNull(testee.createStudent(null));
        verify(studentBusiness, never()).createStudent(any(Student.class));

        final Student toCreate = new Student();
        assertEquals(created, testee.createStudent(toCreate));
        verify(studentBusiness).createStudent(eq(toCreate));
    }

    @Test
    public void testSaveStudent() {
        setAdminSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final Student saved = new Student("1", "firstName", "lastName");
        when(studentBusiness.saveStudent(eq("1"), any(Student.class))).thenReturn(saved);

        final Student toSave = new Student();
        assertNull(testee.saveStudent(null, toSave));
        assertNull(testee.saveStudent("1", null));
        verify(studentBusiness, never()).saveStudent(anyString(), any(Student.class));

        assertEquals(saved, testee.saveStudent("1", toSave));
        verify(studentBusiness).saveStudent("1", toSave);
    }

    @Test
    public void testCreateStudentAuthorization() {
        setNoRightSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        try {
            testee.createStudent(new Student());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
        }
    }

    @Test
    public void testSaveStudentAuthorization() {
        setNoRightSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final String id = "id";
        try {
            testee.saveStudent(id, new Student());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
            verify(ScholagestThreadLocal.getSubject()).isPermitted(id);
        }
    }
}
