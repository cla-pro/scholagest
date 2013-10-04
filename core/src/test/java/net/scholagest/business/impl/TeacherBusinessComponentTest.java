package net.scholagest.business.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BaseObjectMock;
import net.scholagest.objects.TeacherObject;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Before;
import org.junit.Test;

public class TeacherBusinessComponentTest extends AbstractTestWithTransaction {
    private static final String TEACHER_KEY = "http://scholagest.net/teacher#e85af55b-8b34-4646-a872-1a6e9c210fe2";

    private ITeacherManager teacherManager;

    private ITeacherBusinessComponent testee;

    @Before
    public void setup() throws Exception {
        teacherManager = mock(ITeacherManager.class);
        when(teacherManager.createTeacher(anyMapOf(String.class, Object.class))).thenReturn(
                BaseObjectMock.createTeacherObject(TEACHER_KEY, new HashMap<String, Object>()));

        TeacherObject teacherObject = BaseObjectMock.createTeacherObject(TEACHER_KEY, new HashMap<String, Object>());
        teacherObject.setProperties(createTeacherProperties());

        when(teacherManager.getTeacherProperties(TEACHER_KEY, createTeacherProperties().keySet())).thenReturn(teacherObject);
        when(teacherManager.getTeachers()).thenReturn(
                new HashSet<TeacherObject>(Arrays.asList(BaseObjectMock.createTeacherObject(TEACHER_KEY, new HashMap<String, Object>()))));

        testee = new TeacherBusinessComponent(teacherManager);
    }

    private Map<String, Object> createTeacherProperties() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put("pTeacherLastName", "Dupont");
        personalProperties.put("pTeacherFirstName", "Gilles");

        return personalProperties;
    }

    @Test(expected = ScholagestRuntimeException.class)
    public void testGetTeacherTypes() throws Exception {
        testee.getTeacherTypes();
        fail("Exception expected");
    }

    @Test
    public void testCreateTeacher() throws Exception {
        Map<String, Object> teacherProperties = createTeacherProperties();
        BaseObject teacher = testee.createTeacher(null, teacherProperties);

        assertEquals(TEACHER_KEY, teacher.getKey());
        verify(teacherManager).createTeacher(teacherProperties);
        verify(teacherManager, never()).setTeacherProperties(teacher.getKey(), teacherProperties);
        assertNoCallToTransaction();
    }

    @Test
    public void testGetTeachers() throws Exception {
        Set<TeacherObject> teachers = testee.getTeachers();

        assertEquals(1, teachers.size());
        assertEquals(TEACHER_KEY, teachers.iterator().next().getKey());
        assertNoCallToTransaction();
    }

    @Test
    public void testGetTeachersWithProperties() throws Exception {
        Map<String, Object> teacherProperties = createTeacherProperties();
        Set<TeacherObject> studentsWithProperties = testee.getTeachersWithProperties(teacherProperties.keySet());

        assertEquals(1, studentsWithProperties.size());
        TeacherObject teacher = studentsWithProperties.iterator().next();
        assertNotNull(teacher);
        assertNotNull(teacher.getProperties());
        assertMapEquals(teacherProperties, teacher.getProperties());
        assertNoCallToTransaction();
    }

    @Test(expected = ScholagestRuntimeException.class)
    public void testGetTeacherClasses() throws Exception {
        testee.getTeacherClasses(TEACHER_KEY);
        fail("Exception expected");
    }

    @Test
    public void testSetTeacherProperties() throws Exception {
        Map<String, Object> teacherProperties = createTeacherProperties();
        testee.setTeacherProperties(TEACHER_KEY, teacherProperties);

        verify(teacherManager).setTeacherProperties(TEACHER_KEY, teacherProperties);
        assertNoCallToTransaction();
    }

    @Test
    public void testGetTeacherProperties() throws Exception {
        Map<String, Object> teacherProperties = createTeacherProperties();
        BaseObject teacher = testee.getTeacherProperties(TEACHER_KEY, teacherProperties.keySet());

        verify(teacherManager).getTeacherProperties(TEACHER_KEY, teacherProperties.keySet());

        assertMapEquals(teacherProperties, teacher.getProperties());
        assertNoCallToTransaction();
    }
}
