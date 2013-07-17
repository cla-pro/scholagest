package net.scholagest.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IClassService;
import net.scholagest.services.kdom.KSet;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ClassServiceTest extends AbstractTest {
    private InMemoryDatabase database;
    private IClassBusinessComponent classBusinessComponent;
    private IUserBusinessComponent userBusinessComponent;
    private IOntologyBusinessComponent ontologyBusinessComponent;

    private IClassService testee;

    @Before
    public void setUpTest() {
        database = Mockito.spy(new InMemoryDatabase());
        defineAdminSubject();

        classBusinessComponent = Mockito.mock(IClassBusinessComponent.class);
        userBusinessComponent = Mockito.mock(IUserBusinessComponent.class);
        ontologyBusinessComponent = Mockito.mock(IOntologyBusinessComponent.class);

        testee = new ClassService(database, classBusinessComponent, userBusinessComponent, ontologyBusinessComponent);
    }

    @Test
    public void testCreateClass() throws Exception {
        String className = "CLASS NAME";
        String yearKey = "YEAR KEY";
        HashMap<String, Object> classProperties = new HashMap<String, Object>();
        testee.createClass(classProperties, className, yearKey);

        Mockito.verify(classBusinessComponent).createClass(classProperties, className, yearKey);
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testCreateClassInsufficientPrivileges() throws Exception {
        String className = "CLASS NAME";
        String yearKey = "YEAR KEY";
        HashMap<String, Object> classProperties = new HashMap<String, Object>();
        try {
            defineClassTeacherSubject();
            testee.createClass(classProperties, className, yearKey);
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineClassHelpTeacherSubject();
            testee.createClass(classProperties, className, yearKey);
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.createClass(classProperties, className, yearKey);
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testGetClassesForYears() throws Exception {
        testee.getClassesForYears(new HashSet<String>());

        Mockito.verify(classBusinessComponent).getClassesForYears(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetClassesForYearsOtherUsers() throws Exception {
        defineClassTeacherSubject();
        testee.getClassesForYears(new HashSet<String>());
        Mockito.verify(classBusinessComponent).getClassesForYears(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(classBusinessComponent);
        Mockito.reset(database);

        defineClassHelpTeacherSubject();
        testee.getClassesForYears(new HashSet<String>());
        Mockito.verify(classBusinessComponent).getClassesForYears(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(classBusinessComponent);
        Mockito.reset(database);

        defineOtherTeacherSubject();
        testee.getClassesForYears(new HashSet<String>());
        Mockito.verify(classBusinessComponent).getClassesForYears(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(classBusinessComponent);
        Mockito.reset(database);
    }

    @Test
    public void testGetClassProperties() throws Exception {
        String classKey = "classKey";
        testee.getClassProperties(classKey, new HashSet<String>());

        Mockito.verify(classBusinessComponent).getClassProperties(Mockito.eq(classKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetClassePropertiesOtherUsers() throws Exception {
        String classKey = "classKey";
        defineClassTeacherSubject();
        testee.getClassProperties(classKey, new HashSet<String>());
        Mockito.verify(classBusinessComponent).getClassProperties(Mockito.eq(classKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(classBusinessComponent);
        Mockito.reset(database);

        defineClassHelpTeacherSubject();
        testee.getClassProperties(classKey, new HashSet<String>());
        Mockito.verify(classBusinessComponent).getClassProperties(Mockito.eq(classKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(classBusinessComponent);
        Mockito.reset(database);

        defineOtherTeacherSubject();
        testee.getClassProperties(classKey, new HashSet<String>());
        Mockito.verify(classBusinessComponent).getClassProperties(Mockito.eq(classKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(classBusinessComponent);
        Mockito.reset(database);
    }

    @Test
    public void testSetClassProperties() throws Exception {
        BaseObject classObject = createClassObject();
        Mockito.when(classBusinessComponent.getClassProperties(Mockito.anyString(), Mockito.<Set<String>> any())).thenReturn(classObject);

        String classKey = "classKey";
        Map<String, Object> properties = new HashMap<String, Object>();
        testee.setClassProperties(classKey, properties);

        Mockito.verify(classBusinessComponent).setClassProperties(Mockito.eq(classKey), Mockito.eq(properties));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testSetClassPropertiesInsufficientPrivileges() throws Exception {
        String classKey = "classKey";
        try {
            defineClassTeacherSubject();
            testee.setClassProperties(classKey, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineClassHelpTeacherSubject();
            testee.setClassProperties(classKey, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.setClassProperties(classKey, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    private BaseObject createClassObject() {
        BaseObject classObject = Mockito.mock(BaseObject.class);

        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pClassStudents, new KSet("", new HashSet<>()));
        properties.put(CoreNamespace.pClassTeachers, new KSet("", new HashSet<>()));

        Mockito.when(classObject.getProperties()).thenReturn(properties);

        return classObject;
    }
}
