package net.scholagest.business.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.managers.IStudentManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BaseObjectMock;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class StudentBusinessComponentTest extends AbstractTestWithTransaction {
    private static final String STUDENT_KEY = "http://scholagest.net/student#329c1a51-5469-4eac-a182-29afb1e5ecdb";

    private IStudentManager studentManager;

    private IStudentBusinessComponent testee;

    @Before
    public void setup() throws Exception {
        studentManager = Mockito.mock(IStudentManager.class);
        Mockito.when(studentManager.createStudent()).thenReturn(new BaseObject(STUDENT_KEY, CoreNamespace.tStudent));
        Mockito.when(studentManager.getMedicalProperties(Mockito.eq(STUDENT_KEY), Mockito.eq(createStudentMedicalProperties().keySet()))).thenReturn(
                BaseObjectMock.createBaseObject(null, null, createStudentMedicalProperties()));
        Mockito.when(studentManager.getPersonalProperties(Mockito.eq(STUDENT_KEY), Mockito.eq(createStudentPersonalProperties().keySet())))
                .thenReturn(BaseObjectMock.createBaseObject(null, null, createStudentPersonalProperties()));
        Mockito.when(studentManager.getStudents()).thenReturn(new HashSet<>(Arrays.asList(new BaseObject(STUDENT_KEY, CoreNamespace.tStudent))));

        testee = new StudentBusinessComponent(studentManager, null, null, null, null, null);
    }

    private Map<String, Object> createStudentPersonalProperties() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put("pStudentLastName", "Dupont");
        personalProperties.put("pStudentFirstName", "Gilles");

        return personalProperties;
    }

    private Map<String, Object> createStudentMedicalProperties() {
        Map<String, Object> medicalProperties = new HashMap<String, Object>();

        medicalProperties.put("pStudentAlergy", "RAS");

        return medicalProperties;
    }

    @Test
    public void testCreateStudent() throws Exception {
        Map<String, Object> properties = createStudentPersonalProperties();
        BaseObject studentKey = testee.createStudent(properties);

        assertEquals(STUDENT_KEY, studentKey.getKey());
        Mockito.verify(studentManager).createStudent();
    }

    @Test
    public void testUpdateStudentProperties() throws Exception {
        Map<String, Object> personalProperties = createStudentPersonalProperties();
        Map<String, Object> medicalProperties = createStudentPersonalProperties();
        testee.updateStudentProperties(STUDENT_KEY, personalProperties, medicalProperties);

        Mockito.verify(studentManager).setMedicalProperties(Mockito.eq(STUDENT_KEY), Mockito.eq(medicalProperties));
        Mockito.verify(studentManager).setPersonalProperties(Mockito.eq(STUDENT_KEY), Mockito.eq(personalProperties));
    }

    @Test
    public void testGetStudentPersonalProperties() throws Exception {
        Map<String, Object> mockProperties = createStudentPersonalProperties();
        BaseObject personalProperties = testee.getStudentPersonalProperties(STUDENT_KEY, mockProperties.keySet());

        Mockito.verify(studentManager).getPersonalProperties(Mockito.eq(STUDENT_KEY), Mockito.eq(mockProperties.keySet()));

        assertMapEquals(mockProperties, personalProperties.getProperties());
    }

    @Test
    public void testGetStudentMedicalProperties() throws Exception {
        Map<String, Object> mockProperties = createStudentMedicalProperties();
        BaseObject medicalProperties = testee.getStudentMedicalProperties(STUDENT_KEY, mockProperties.keySet());

        Mockito.verify(studentManager).getMedicalProperties(Mockito.eq(STUDENT_KEY), Mockito.eq(mockProperties.keySet()));

        assertMapEquals(mockProperties, medicalProperties.getProperties());
    }

    @Test
    public void testGetStudentsWithProperties() throws Exception {
        Map<String, Object> personalProperties = createStudentPersonalProperties();
        Set<BaseObject> studentsWithProperties = testee.getStudentsWithProperties(personalProperties.keySet());

        Map<String, Object> studentProperties = studentsWithProperties.iterator().next().getProperties();
        assertEquals(1, studentsWithProperties.size());
        assertMapEquals(personalProperties, studentProperties);
    }
}
