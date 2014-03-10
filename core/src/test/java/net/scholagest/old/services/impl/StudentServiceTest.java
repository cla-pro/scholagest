package net.scholagest.old.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.old.business.IOntologyBusinessComponent;
import net.scholagest.old.business.IStudentBusinessComponent;
import net.scholagest.old.managers.ontology.OntologyElement;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.StudentObject;
import net.scholagest.old.services.IStudentService;
import net.scholagest.old.services.impl.StudentService;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class StudentServiceTest extends AbstractTest {
    private static final String NOT_RESTRICTED_PROP = "notRestricted";
    private static final String RESTRICTED_PROP = "restricted";
    private InMemoryDatabase database;
    private IStudentBusinessComponent studentBusinessComponent;
    private IOntologyBusinessComponent ontologyBusinessComponent;

    private IStudentService testee;

    @Before
    public void setup() throws Exception {
        database = Mockito.spy(new InMemoryDatabase());
        defineAdminSubject();

        studentBusinessComponent = Mockito.mock(IStudentBusinessComponent.class);
        ontologyBusinessComponent = Mockito.mock(IOntologyBusinessComponent.class);

        OntologyElement restrictedObject = new OntologyElement();
        restrictedObject.setAttribute(CoreNamespace.scholagestNs + "#restricted", "");
        Mockito.when(ontologyBusinessComponent.getElementWithName(Mockito.anyString())).thenReturn(new OntologyElement());
        Mockito.when(ontologyBusinessComponent.getElementWithName(Mockito.eq(RESTRICTED_PROP))).thenReturn(restrictedObject);

        testee = new StudentService(database, studentBusinessComponent, ontologyBusinessComponent);
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
    public void testGetStudentPersonalPropertiesFiltered() throws Exception {
        String studentKey = "studentKey";

        String notRestrictedValue = "notRestricted";
        String restrictedValue = "restricted";

        StudentObject studentObject = new StudentObject(studentKey);
        studentObject.putProperty(NOT_RESTRICTED_PROP, notRestrictedValue);
        studentObject.putProperty(RESTRICTED_PROP, restrictedValue);
        studentObject.flushAllProperties();
        Mockito.when(studentBusinessComponent.getStudentPersonalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class))).thenReturn(
                studentObject);
        defineClassTeacherSubject();
        BaseObject classTeacherStudentObject = testee.getStudentPersonalProperties(studentKey,
                new HashSet<String>(Arrays.asList(NOT_RESTRICTED_PROP, RESTRICTED_PROP)));
        assertEquals(notRestrictedValue, classTeacherStudentObject.getProperty(NOT_RESTRICTED_PROP));
        assertEquals(restrictedValue, classTeacherStudentObject.getProperty(RESTRICTED_PROP));

        studentObject.putProperty(NOT_RESTRICTED_PROP, notRestrictedValue);
        studentObject.putProperty(RESTRICTED_PROP, restrictedValue);
        Mockito.when(studentBusinessComponent.getStudentPersonalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class))).thenReturn(
                studentObject);
        defineClassHelpTeacherSubject();
        BaseObject classHelpTeacherStudentObject = testee.getStudentPersonalProperties(studentKey,
                new HashSet<String>(Arrays.asList(NOT_RESTRICTED_PROP, RESTRICTED_PROP)));
        assertEquals(notRestrictedValue, classHelpTeacherStudentObject.getProperty(NOT_RESTRICTED_PROP));
        assertNull(classHelpTeacherStudentObject.getProperty(RESTRICTED_PROP));

        studentObject.putProperty(NOT_RESTRICTED_PROP, notRestrictedValue);
        studentObject.putProperty(RESTRICTED_PROP, restrictedValue);
        Mockito.when(studentBusinessComponent.getStudentPersonalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class))).thenReturn(
                studentObject);
        defineOtherTeacherSubject();
        BaseObject otherTeacherStudentObject = testee.getStudentPersonalProperties(studentKey,
                new HashSet<String>(Arrays.asList(NOT_RESTRICTED_PROP, RESTRICTED_PROP)));
        assertEquals(notRestrictedValue, otherTeacherStudentObject.getProperty(NOT_RESTRICTED_PROP));
        assertNull(classHelpTeacherStudentObject.getProperty(RESTRICTED_PROP));
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
    public void testGetStudentMedicalPropertiesFiltered() throws Exception {
        String studentKey = "studentKey";

        String notRestrictedValue = "notRestricted";
        String restrictedValue = "restricted";

        BaseObject studentObject = new BaseObject(studentKey, CoreNamespace.tStudent);
        studentObject.putProperty(NOT_RESTRICTED_PROP, notRestrictedValue);
        studentObject.putProperty(RESTRICTED_PROP, restrictedValue);
        Mockito.when(studentBusinessComponent.getStudentMedicalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class))).thenReturn(
                studentObject);
        defineClassTeacherSubject();
        BaseObject classTeacherStudentObject = testee.getStudentMedicalProperties(studentKey,
                new HashSet<String>(Arrays.asList(NOT_RESTRICTED_PROP, RESTRICTED_PROP)));
        assertEquals(notRestrictedValue, classTeacherStudentObject.getProperty(NOT_RESTRICTED_PROP));
        assertEquals(restrictedValue, classTeacherStudentObject.getProperty(RESTRICTED_PROP));

        studentObject.putProperty(NOT_RESTRICTED_PROP, notRestrictedValue);
        studentObject.putProperty(RESTRICTED_PROP, restrictedValue);
        Mockito.when(studentBusinessComponent.getStudentMedicalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class))).thenReturn(
                studentObject);
        defineClassHelpTeacherSubject();
        BaseObject classHelpTeacherStudentObject = testee.getStudentMedicalProperties(studentKey,
                new HashSet<String>(Arrays.asList(NOT_RESTRICTED_PROP, RESTRICTED_PROP)));
        assertEquals(notRestrictedValue, classHelpTeacherStudentObject.getProperty(NOT_RESTRICTED_PROP));
        assertNull(classHelpTeacherStudentObject.getProperty(RESTRICTED_PROP));

        studentObject.putProperty(NOT_RESTRICTED_PROP, notRestrictedValue);
        studentObject.putProperty(RESTRICTED_PROP, restrictedValue);
        Mockito.when(studentBusinessComponent.getStudentMedicalProperties(Mockito.eq(studentKey), Mockito.anySetOf(String.class))).thenReturn(
                studentObject);
        defineOtherTeacherSubject();
        BaseObject otherTeacherStudentObject = testee.getStudentMedicalProperties(studentKey,
                new HashSet<String>(Arrays.asList(NOT_RESTRICTED_PROP, RESTRICTED_PROP)));
        assertEquals(notRestrictedValue, otherTeacherStudentObject.getProperty(NOT_RESTRICTED_PROP));
        assertNull(classHelpTeacherStudentObject.getProperty(RESTRICTED_PROP));
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
    public void testGetStudentsWithPropertiesFiltered() throws Exception {
        String notRestrictedValue = "notRestricted";
        String restrictedValue = "restricted";

        BaseObject studentObject = new BaseObject("studentKey", CoreNamespace.tStudent);
        studentObject.putProperty(NOT_RESTRICTED_PROP, notRestrictedValue);
        studentObject.putProperty(RESTRICTED_PROP, restrictedValue);
        Mockito.when(studentBusinessComponent.getStudentsWithProperties(Mockito.anySetOf(String.class))).thenReturn(
                new HashSet<>(Arrays.asList(studentObject)));
        defineClassTeacherSubject();
        Set<BaseObject> classTeacherStudentObjects = testee.getStudentsWithProperties(new HashSet<String>(Arrays.asList(NOT_RESTRICTED_PROP,
                RESTRICTED_PROP)));
        assertEquals(notRestrictedValue, classTeacherStudentObjects.iterator().next().getProperty(NOT_RESTRICTED_PROP));
        assertEquals(restrictedValue, classTeacherStudentObjects.iterator().next().getProperty(RESTRICTED_PROP));

        studentObject.putProperty(NOT_RESTRICTED_PROP, notRestrictedValue);
        studentObject.putProperty(RESTRICTED_PROP, restrictedValue);
        Mockito.when(studentBusinessComponent.getStudentsWithProperties(Mockito.anySetOf(String.class))).thenReturn(
                new HashSet<>(Arrays.asList(studentObject)));
        defineClassHelpTeacherSubject();
        Set<BaseObject> classHelpTeacherStudentObject = testee.getStudentsWithProperties(new HashSet<String>(Arrays.asList(NOT_RESTRICTED_PROP,
                RESTRICTED_PROP)));
        assertEquals(notRestrictedValue, classHelpTeacherStudentObject.iterator().next().getProperty(NOT_RESTRICTED_PROP));
        assertNull(classHelpTeacherStudentObject.iterator().next().getProperty(RESTRICTED_PROP));

        studentObject.putProperty(NOT_RESTRICTED_PROP, notRestrictedValue);
        studentObject.putProperty(RESTRICTED_PROP, restrictedValue);
        Mockito.when(studentBusinessComponent.getStudentsWithProperties(Mockito.anySetOf(String.class))).thenReturn(
                new HashSet<>(Arrays.asList(studentObject)));
        defineOtherTeacherSubject();
        Set<BaseObject> otherTeacherStudentObject = testee.getStudentsWithProperties(new HashSet<String>(Arrays.asList(NOT_RESTRICTED_PROP,
                RESTRICTED_PROP)));
        assertEquals(notRestrictedValue, otherTeacherStudentObject.iterator().next().getProperty(NOT_RESTRICTED_PROP));
        assertNull(classHelpTeacherStudentObject.iterator().next().getProperty(RESTRICTED_PROP));
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
