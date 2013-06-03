package net.scholagest.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.UserObject;
import net.scholagest.services.ITeacherService;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TeacherServiceTest extends AbstractTest {
    private InMemoryDatabase database;

    private ITeacherBusinessComponent teacherBusinessComponent;
    private IUserBusinessComponent userBusinessComponent;
    private ITeacherService testee;

    @Before
    public void setUpTest() {
        database = Mockito.spy(new InMemoryDatabase());
        defineAdminSubject();

        teacherBusinessComponent = Mockito.mock(ITeacherBusinessComponent.class);
        userBusinessComponent = Mockito.mock(IUserBusinessComponent.class);

        testee = new TeacherService(database, teacherBusinessComponent, userBusinessComponent);
    }

    @Test
    public void testCreateTeacher() throws Exception {
        Mockito.when(teacherBusinessComponent.createTeacher(Mockito.anyString(), Mockito.<Map<String, Object>> any())).thenReturn(
                new BaseObject("", CoreNamespace.tTeacher));
        Mockito.when(userBusinessComponent.createUser("")).thenReturn(createUserObject());

        testee.createTeacher(null, new HashMap<String, Object>());

        Mockito.verify(teacherBusinessComponent).createTeacher(Mockito.anyString(), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    private UserObject createUserObject() {
        UserObject userObject = new UserObject("");

        userObject.setRoles(new DBSet(Mockito.mock(ITransaction.class), ""));
        userObject.setPermissions(new DBSet(Mockito.mock(ITransaction.class), ""));

        return userObject;
    }

    @Test
    public void testCreateTeacherInsufficientPrivileges() throws Exception {
        try {
            defineClassTeacherSubject();
            testee.createTeacher(null, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineClassHelpTeacherSubject();
            testee.createTeacher(null, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.createTeacher(null, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testGetTeachers() throws Exception {
        testee.getTeachers();

        Mockito.verify(teacherBusinessComponent).getTeachers();
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetTeachersOtherUsers() throws Exception {
        defineClassTeacherSubject();
        testee.getTeachers();
        Mockito.verify(teacherBusinessComponent).getTeachers();
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(teacherBusinessComponent);
        Mockito.reset(database);

        defineClassHelpTeacherSubject();
        testee.getTeachers();
        Mockito.verify(teacherBusinessComponent).getTeachers();
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(teacherBusinessComponent);
        Mockito.reset(database);

        defineOtherTeacherSubject();
        testee.getTeachers();
        Mockito.verify(teacherBusinessComponent).getTeachers();
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(teacherBusinessComponent);
        Mockito.reset(database);
    }

    @Test
    public void testGetTeachersWithProperties() throws Exception {
        testee.getTeachersWithProperties(new HashSet<String>());

        Mockito.verify(teacherBusinessComponent).getTeachersWithProperties(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetTeachersWithPropertiesOtherUsers() throws Exception {
        defineClassTeacherSubject();
        testee.getTeachersWithProperties(new HashSet<String>());
        Mockito.verify(teacherBusinessComponent).getTeachersWithProperties(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(teacherBusinessComponent);
        Mockito.reset(database);

        defineClassHelpTeacherSubject();
        testee.getTeachersWithProperties(new HashSet<String>());
        Mockito.verify(teacherBusinessComponent).getTeachersWithProperties(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(teacherBusinessComponent);
        Mockito.reset(database);

        defineOtherTeacherSubject();
        testee.getTeachersWithProperties(new HashSet<String>());
        Mockito.verify(teacherBusinessComponent).getTeachersWithProperties(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(teacherBusinessComponent);
        Mockito.reset(database);
    }

    @Test
    public void testSetTeacherProperties() throws Exception {
        String teacherKey = "teacherKey";
        testee.setTeacherProperties(teacherKey, new HashMap<String, Object>());

        Mockito.verify(teacherBusinessComponent).setTeacherProperties(Mockito.eq(teacherKey), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testSetTeacherPropertiesInsufficientPrivileges() throws Exception {
        String teacherKey = "teacherKey";

        defineClassTeacherSubject();
        testee.setTeacherProperties(teacherKey, new HashMap<String, Object>());
        Mockito.verify(teacherBusinessComponent).setTeacherProperties(Mockito.eq(teacherKey), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());

        try {
            defineClassHelpTeacherSubject();
            testee.setTeacherProperties(teacherKey, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.setTeacherProperties(teacherKey, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testGetTeacherProperties() throws Exception {
        String teacherKey = "teacherKey";
        testee.getTeacherProperties(teacherKey, new HashSet<String>());

        Mockito.verify(teacherBusinessComponent).getTeacherProperties(Mockito.eq(teacherKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetTeacherPropertiesOtherUsers() throws Exception {
        String teacherKey = "teacherKey";

        defineClassTeacherSubject();
        testee.getTeacherProperties(teacherKey, new HashSet<String>());
        Mockito.verify(teacherBusinessComponent).getTeacherProperties(Mockito.eq(teacherKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(teacherBusinessComponent);
        Mockito.reset(database);

        defineClassHelpTeacherSubject();
        testee.getTeacherProperties(teacherKey, new HashSet<String>());
        Mockito.verify(teacherBusinessComponent).getTeacherProperties(Mockito.eq(teacherKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(teacherBusinessComponent);
        Mockito.reset(database);

        defineOtherTeacherSubject();
        testee.getTeacherProperties(teacherKey, new HashSet<String>());
        Mockito.verify(teacherBusinessComponent).getTeacherProperties(Mockito.eq(teacherKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(teacherBusinessComponent);
        Mockito.reset(database);
    }
}
