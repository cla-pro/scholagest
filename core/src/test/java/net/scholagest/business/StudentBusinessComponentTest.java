package net.scholagest.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.scholagest.managers.IStudentManager;
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
        Mockito.when(studentManager.createStudent(Mockito.anyString(), Mockito.eq(transaction))).thenReturn(STUDENT_KEY);
        Mockito.when(
                studentManager.getMedicalProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(STUDENT_KEY),
                        Mockito.eq(createStudentMedicalProperties().keySet()))).thenReturn(createStudentMedicalProperties());
        Mockito.when(
                studentManager.getPersonalProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(STUDENT_KEY),
                        Mockito.eq(createStudentPersonalProperties().keySet()))).thenReturn(createStudentPersonalProperties());
        Mockito.when(studentManager.getStudents(Mockito.anyString(), Mockito.eq(transaction))).thenReturn(
                new HashSet<String>(Arrays.asList(STUDENT_KEY)));

        testee = new StudentBusinessComponent(studentManager);
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
        String studentKey = testee.createStudent(requestId, transaction, properties);

        assertEquals(STUDENT_KEY, studentKey);
        Mockito.verify(studentManager).createStudent(Mockito.anyString(), Mockito.eq(transaction));
    }

    @Test
    public void testUpdateStudentProperties() throws Exception {
        Map<String, Object> personalProperties = createStudentPersonalProperties();
        Map<String, Object> medicalProperties = createStudentPersonalProperties();
        testee.updateStudentProperties(requestId, transaction, STUDENT_KEY, personalProperties, medicalProperties);

        Mockito.verify(studentManager).setMedicalProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(STUDENT_KEY),
                Mockito.eq(medicalProperties));
        Mockito.verify(studentManager).setPersonalProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(STUDENT_KEY),
                Mockito.eq(personalProperties));
    }

    @Test
    public void testGetStudentPersonalProperties() throws Exception {
        Map<String, Object> personalProperties = createStudentPersonalProperties();
        Map<String, Object> readProperties = testee.getStudentPersonalProperties(requestId, transaction, STUDENT_KEY, personalProperties.keySet());

        Mockito.verify(studentManager).getPersonalProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(STUDENT_KEY),
                Mockito.eq(personalProperties.keySet()));

        assertMapEquals(personalProperties, readProperties);
    }

    @Test
    public void testGetStudentMedicalProperties() throws Exception {
        Map<String, Object> medicalProperties = createStudentMedicalProperties();
        Map<String, Object> readProperties = testee.getStudentMedicalProperties(requestId, transaction, STUDENT_KEY, medicalProperties.keySet());

        Mockito.verify(studentManager).getMedicalProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(STUDENT_KEY),
                Mockito.eq(medicalProperties.keySet()));

        assertMapEquals(medicalProperties, readProperties);
    }

    @Test
    public void testGetStudentsWithProperties() throws Exception {
        Map<String, Object> personalProperties = createStudentPersonalProperties();
        Map<String, Map<String, Object>> studentsWithProperties = testee.getStudentsWithProperties(requestId, transaction, personalProperties.keySet());

        assertEquals(1, studentsWithProperties.size());
        assertNotNull(studentsWithProperties.get(STUDENT_KEY));
        assertMapEquals(personalProperties, studentsWithProperties.get(STUDENT_KEY));
    }
}
