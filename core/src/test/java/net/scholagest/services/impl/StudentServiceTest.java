package net.scholagest.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IStudentService;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class StudentServiceTest extends AbstractTest {
    private InMemoryDatabase database;

    private IStudentBusinessComponent studentBusinessComponent;
    private IStudentService testee;

    @Before
    public void setup() {
        database = Mockito.spy(new InMemoryDatabase());
        defineAdminSubject();

        studentBusinessComponent = Mockito.mock(IStudentBusinessComponent.class);

        testee = new StudentService(database, studentBusinessComponent);
    }

    @Test
    public void testCreateStudent() throws Exception {
        testee.createStudent(new HashMap<String, Object>());

        Mockito.verify(studentBusinessComponent).createStudent(Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testCreateStudentInsufficientPrivileges() throws Exception {
        try {
            defineClassTeacherSubject();
            testee.createStudent(new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineClassHelpTeacherSubject();
            testee.createStudent(new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.createStudent(new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testUpdateStudentProperties() throws Exception {
        String studentKey = "studentKey";
        testee.updateStudentProperties(studentKey, new HashMap<String, Object>(), new HashMap<String, Object>());

        Mockito.verify(studentBusinessComponent).updateStudentProperties(Mockito.eq(studentKey), Mockito.anyMapOf(String.class, Object.class),
                Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testUpdateStudentPropertiesInsufficientPrivileges() throws Exception {
        String studentKey = "studentKey";

        {
            defineClassTeacherSubject();
            testee.updateStudentProperties(studentKey, new HashMap<String, Object>(), new HashMap<String, Object>());
            Mockito.verify(studentBusinessComponent).updateStudentProperties(Mockito.eq(studentKey), Mockito.anyMapOf(String.class, Object.class),
                    Mockito.anyMapOf(String.class, Object.class));
            Mockito.verify(database).getTransaction(Mockito.anyString());
        }

        try {
            defineClassHelpTeacherSubject();
            testee.updateStudentProperties(studentKey, new HashMap<String, Object>(), new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.updateStudentProperties(studentKey, new HashMap<String, Object>(), new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testGetStudentPersonalProperties() throws Exception {
        String studentKey = "studentKey";
        testee.getStudentPersonalProperties(studentKey, new HashSet<String>());

        Mockito.verify(studentBusinessComponent).getStudentPersonalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetStudentPersonalPropertiesOtherUsers() throws Exception {
        String studentKey = "studentKey";

        defineClassTeacherSubject();
        testee.getStudentPersonalProperties(studentKey, new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentPersonalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);

        defineClassHelpTeacherSubject();
        testee.getStudentPersonalProperties(studentKey, new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentPersonalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);

        defineOtherTeacherSubject();
        testee.getStudentPersonalProperties(studentKey, new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentPersonalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);
    }

    @Test
    public void testGetStudentMedicalProperties() throws Exception {
        String studentKey = "studentKey";
        testee.getStudentMedicalProperties(studentKey, new HashSet<String>());

        Mockito.verify(studentBusinessComponent).getStudentMedicalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(Mockito.anyString());
    }

    @Test
    public void testGetStudentMedicalPropertiesOtherUsers() throws Exception {
        String studentKey = "studentKey";

        defineClassTeacherSubject();
        testee.getStudentMedicalProperties(studentKey, new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentMedicalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);

        defineClassHelpTeacherSubject();
        testee.getStudentMedicalProperties(studentKey, new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentMedicalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);

        defineOtherTeacherSubject();
        testee.getStudentMedicalProperties(studentKey, new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentMedicalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);
    }

    @Test
    public void testGetStudentsWithProperties() throws Exception {
        testee.getStudentsWithProperties(new HashSet<String>());

        Mockito.verify(studentBusinessComponent).getStudentsWithProperties(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(Mockito.anyString());
    }

    @Test
    public void testGetStudentsWithPropertiesOtherUsers() throws Exception {
        defineClassTeacherSubject();
        testee.getStudentsWithProperties(new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentsWithProperties(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);

        defineClassHelpTeacherSubject();
        testee.getStudentsWithProperties(new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentsWithProperties(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);

        defineOtherTeacherSubject();
        testee.getStudentsWithProperties(new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentsWithProperties(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);
    }

    @Test
    public void testGetStudentWithProperties() throws Exception {
        String studentKey = "studentKey";
        testee.getStudentProperties(studentKey, new HashSet<String>());

        Mockito.verify(studentBusinessComponent).getStudentProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetTeachersWithPropertiesOtherUsers() throws Exception {
        String studentKey = "studentKey";

        defineClassTeacherSubject();
        testee.getStudentProperties(studentKey, new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);

        defineClassHelpTeacherSubject();
        testee.getStudentProperties(studentKey, new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);

        defineOtherTeacherSubject();
        testee.getStudentProperties(studentKey, new HashSet<String>());
        Mockito.verify(studentBusinessComponent).getStudentProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(studentBusinessComponent);
        Mockito.reset(database);
    }

    @Test
    public void testGetGrades() throws Exception {
        Set<String> studentKeys = new HashSet<>();
        studentKeys.add("studentKey1");
        studentKeys.add("studentKey2");

        String yearKey = "yearKey";

        testee.getGrades(studentKeys, new HashSet<String>(), yearKey);

        Mockito.verify(studentBusinessComponent).getGrades(Mockito.anySetOf(String.class), Mockito.anySetOf(String.class), Mockito.eq(yearKey));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetGradesInsufficientPrivileges() throws Exception {
        Set<String> studentKeys = new HashSet<>();
        studentKeys.add("studentKey1");
        studentKeys.add("studentKey2");
        String yearKey = "yearKey";

        {
            defineClassTeacherSubject();
            testee.getGrades(studentKeys, new HashSet<String>(), yearKey);
            Mockito.verify(studentBusinessComponent).getGrades(Mockito.anySetOf(String.class), Mockito.anySetOf(String.class), Mockito.eq(yearKey));
            Mockito.verify(database).getTransaction(getKeyspace());
        }

        try {
            defineClassHelpTeacherSubject();
            testee.getGrades(studentKeys, new HashSet<String>(), yearKey);
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.getGrades(studentKeys, new HashSet<String>(), yearKey);
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testSetGrades() throws Exception {
        String studentKey = "studentKey";
        String yearKey = "yearKey";
        String classKey = "classKey";
        String periodKey = "periodKey";
        String branchKey = "branchKey";

        testee.setGrades(studentKey, new HashMap<String, BaseObject>(), yearKey, classKey, branchKey, periodKey);

        Mockito.verify(studentBusinessComponent).setStudentGrades(studentKey, new HashMap<String, BaseObject>(), yearKey, classKey, branchKey,
                periodKey);
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testSetGradesInsufficientPrivileges() throws Exception {
        String studentKey = "studentKey";
        String yearKey = "yearKey";
        String classKey = "classKey";
        String periodKey = "periodKey";
        String branchKey = "branchKey";

        {
            defineClassTeacherSubject();
            testee.setGrades(studentKey, new HashMap<String, BaseObject>(), yearKey, classKey, branchKey, periodKey);
            Mockito.verify(studentBusinessComponent).setStudentGrades(studentKey, new HashMap<String, BaseObject>(), yearKey, classKey, branchKey,
                    periodKey);
            Mockito.verify(database).getTransaction(getKeyspace());
        }

        try {
            defineClassHelpTeacherSubject();
            testee.setGrades(studentKey, new HashMap<String, BaseObject>(), yearKey, classKey, branchKey, periodKey);
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.setGrades(studentKey, new HashMap<String, BaseObject>(), yearKey, classKey, branchKey, periodKey);
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }
}
