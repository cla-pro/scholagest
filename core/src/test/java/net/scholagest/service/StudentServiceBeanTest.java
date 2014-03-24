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
import net.scholagest.object.StudentClasses;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;
import net.scholagest.utils.AbstractGuiceContextTest;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link StudentServiceBean}
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

        final Student student1 = new Student("1", "FirstName1", "LastName1", new StudentPersonal(), new StudentMedical());
        final Student student2 = new Student("2", "FirstName2", "LastName2", new StudentPersonal(), new StudentMedical());
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

        final Student expected = new Student("1", "firstName", "lastName", new StudentPersonal(), new StudentMedical());
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

        final Student created = new Student("1", "firstName", "lastName", new StudentPersonal(), new StudentMedical());
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

        final Student saved = new Student("1", "firstName", "lastName", new StudentPersonal(), new StudentMedical());
        when(studentBusiness.saveStudent(eq("1"), any(Student.class))).thenReturn(saved);

        final Student toSave = new Student();
        assertNull(testee.saveStudent(null, toSave));
        assertNull(testee.saveStudent("1", null));
        verify(studentBusiness, never()).saveStudent(anyString(), any(Student.class));

        assertEquals(saved, testee.saveStudent("1", toSave));
        verify(studentBusiness).saveStudent(eq("1"), eq(toSave));
    }

    @Test
    public void testGetStudentPersonal() {
        setAdminSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final Student expected = new Student("1", "firstName", "lastName", new StudentPersonal("1", "street", "city", "postcode", "religion"),
                new StudentMedical());
        when(studentBusiness.getStudent("1")).thenReturn(expected);

        assertNull(testee.getStudentPersonal(null));
        verify(studentBusiness, never()).getStudent(anyString());

        assertNull(testee.getStudentPersonal("2"));
        verify(studentBusiness).getStudent(eq("2"));

        assertEquals(expected.getStudentPersonal(), testee.getStudentPersonal("1"));
        verify(studentBusiness).getStudent(eq("1"));
    }

    @Test
    public void testSaveStudentPersonal() {
        setAdminSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final StudentPersonal saved = new StudentPersonal("1", "street", "city", "postcode", "religion");
        when(studentBusiness.saveStudentPersonal(eq("1"), any(StudentPersonal.class))).thenReturn(saved);

        final StudentPersonal toSave = new StudentPersonal();
        assertNull(testee.saveStudentPersonal(null, toSave));
        assertNull(testee.saveStudentPersonal("1", null));
        verify(studentBusiness, never()).saveStudentPersonal(anyString(), any(StudentPersonal.class));

        assertEquals(saved, testee.saveStudentPersonal("1", toSave));
        verify(studentBusiness).saveStudentPersonal(eq("1"), eq(toSave));
    }

    @Test
    public void testGetStudentMedical() {
        setAdminSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final Student expected = new Student("1", "firstName", "lastName", new StudentPersonal(), new StudentMedical("1", "doctor"));
        when(studentBusiness.getStudent("1")).thenReturn(expected);

        assertNull(testee.getStudentMedical(null));
        verify(studentBusiness, never()).getStudent(anyString());

        assertNull(testee.getStudentMedical("2"));
        verify(studentBusiness).getStudent(eq("2"));

        assertEquals(expected.getStudentMedical(), testee.getStudentMedical("1"));
        verify(studentBusiness).getStudent(eq("1"));
    }

    @Test
    public void testSaveStudentMedical() {
        setAdminSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final StudentMedical saved = new StudentMedical("1", "doctor");
        when(studentBusiness.saveStudentMedical(eq("1"), any(StudentMedical.class))).thenReturn(saved);

        final StudentMedical toSave = new StudentMedical();
        assertNull(testee.saveStudentMedical(null, toSave));
        assertNull(testee.saveStudentMedical("1", null));
        verify(studentBusiness, never()).saveStudentMedical(anyString(), any(StudentMedical.class));

        assertEquals(saved, testee.saveStudentMedical("1", toSave));
        verify(studentBusiness).saveStudentMedical(eq("1"), eq(toSave));
    }

    @Test
    public void testGetStudentClasses() {
        setAdminSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final StudentClasses studentClasses = new StudentClasses("id", Arrays.asList("clazz1"), Arrays.asList("clazz2"));
        when(studentBusiness.getStudentClasses(eq("id"))).thenReturn(studentClasses);

        assertNull(testee.getStudentClasses(null));
        verify(studentBusiness, never()).saveStudentMedical(anyString(), any(StudentMedical.class));

        assertNull(testee.getStudentClasses("wrong"));
        verify(studentBusiness).getStudentClasses(eq("wrong"));

        assertEquals(studentClasses, testee.getStudentClasses("id"));
        verify(studentBusiness).getStudentClasses(eq("id"));
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

    @Test
    public void testSaveStudentPersonalAuthorization() {
        setNoRightSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final String id = "id";
        try {
            testee.saveStudentPersonal(id, new StudentPersonal());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
            verify(ScholagestThreadLocal.getSubject()).isPermitted(id);
        }
    }

    @Test
    public void testSaveStudentMedicalAuthorization() {
        setNoRightSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final String id = "id";
        try {
            testee.saveStudentMedical(id, new StudentMedical());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
            verify(ScholagestThreadLocal.getSubject()).isPermitted(id);
        }
    }

    @Test
    public void testGetStudentClassesAuthorization() {
        setNoRightSubject();
        final StudentServiceLocal testee = getInstance(StudentServiceLocal.class);

        final String id = "id";
        try {
            testee.getStudentClasses(id);
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
            verify(ScholagestThreadLocal.getSubject()).isPermitted(id);
        }
    }
}
