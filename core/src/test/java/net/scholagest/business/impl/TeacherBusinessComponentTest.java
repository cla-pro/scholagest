package net.scholagest.business.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.business.impl.TeacherBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.managers.impl.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TeacherBusinessComponentTest extends AbstractTestWithTransaction {
    private static final String TEACHER_KEY = "http://scholagest.net/teacher#e85af55b-8b34-4646-a872-1a6e9c210fe2";

    private ITeacherManager teacherManager;

    private ITeacherBusinessComponent testee;

    @Before
    public void setup() throws Exception {
        teacherManager = Mockito.mock(ITeacherManager.class);
        Mockito.when(teacherManager.createTeacher(Mockito.anyString(), Mockito.eq(transaction))).thenReturn(
                new BaseObject(TEACHER_KEY, CoreNamespace.tTeacher));

        BaseObject teacherObject = new BaseObject(TEACHER_KEY, CoreNamespace.tTeacher);
        teacherObject.setProperties(createTeacherProperties());

        Mockito.when(
                teacherManager.getTeacherProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(TEACHER_KEY),
                        Mockito.eq(createTeacherProperties().keySet()))).thenReturn(teacherObject);
        Mockito.when(teacherManager.getTeachers(Mockito.anyString(), Mockito.eq(transaction))).thenReturn(
                new HashSet<BaseObject>(Arrays.asList(new BaseObject(TEACHER_KEY, CoreNamespace.tTeacher))));

        testee = new TeacherBusinessComponent(teacherManager);
    }

    private Map<String, Object> createTeacherProperties() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put("pTeacherLastName", "Dupont");
        personalProperties.put("pTeacherFirstName", "Gilles");

        return personalProperties;
    }

    @Test(expected = ScholagestException.class)
    public void testGetTeacherTypes() throws Exception {
        testee.getTeacherTypes();
        fail("Exception expected");
    }

    @Test
    public void testCreateTeacher() throws Exception {
        Map<String, Object> teacherProperties = createTeacherProperties();
        BaseObject teacher = testee.createTeacher(requestId, transaction, null, teacherProperties);

        assertEquals(TEACHER_KEY, teacher.getKey());
        Mockito.verify(teacherManager).createTeacher(Mockito.anyString(), Mockito.eq(transaction));
        assertNoCallToTransaction();
    }

    @Test
    public void testGetTeachers() throws Exception {
        Set<BaseObject> teachers = testee.getTeachers(requestId, transaction);

        assertEquals(1, teachers.size());
        assertEquals(TEACHER_KEY, teachers.iterator().next().getKey());
        assertNoCallToTransaction();
    }

    @Test
    public void testGetTeachersWithProperties() throws Exception {
        Map<String, Object> teacherProperties = createTeacherProperties();
        Set<BaseObject> studentsWithProperties = testee.getTeachersWithProperties(requestId, transaction, teacherProperties.keySet());

        assertEquals(1, studentsWithProperties.size());
        BaseObject teacher = studentsWithProperties.iterator().next();
        assertNotNull(teacher);
        assertNotNull(teacher.getProperties());
        assertMapEquals(teacherProperties, teacher.getProperties());
        assertNoCallToTransaction();
    }

    @Test(expected = ScholagestException.class)
    public void testGetTeacherClasses() throws Exception {
        testee.getTeacherClasses(requestId, transaction, TEACHER_KEY);
        fail("Exception expected");
    }

    @Test
    public void testSetTeacherProperties() throws Exception {
        Map<String, Object> teacherProperties = createTeacherProperties();
        testee.setTeacherProperties(requestId, transaction, TEACHER_KEY, teacherProperties);

        Mockito.verify(teacherManager).setTeacherProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(TEACHER_KEY),
                Mockito.eq(teacherProperties));
        assertNoCallToTransaction();
    }

    @Test
    public void testGetTeacherProperties() throws Exception {
        Map<String, Object> teacherProperties = createTeacherProperties();
        BaseObject teacher = testee.getTeacherProperties(requestId, transaction, TEACHER_KEY, teacherProperties.keySet());

        Mockito.verify(teacherManager).getTeacherProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(TEACHER_KEY),
                Mockito.eq(teacherProperties.keySet()));

        assertMapEquals(teacherProperties, teacher.getProperties());
        assertNoCallToTransaction();
    }
}
