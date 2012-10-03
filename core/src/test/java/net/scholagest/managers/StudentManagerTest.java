package net.scholagest.managers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;

import org.junit.Test;

public class StudentManagerTest extends AbstractTestWithTransaction {
    private static final String STUDENT_KEY = "http://scholagest.net/student#329c1a51-5469-4eac-a182-29afb1e5ecdb";

    private StudentManager studentManager = spy(new StudentManager());

    @Test
    public void testGetMedicalInfoProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Student" });

        Set<String> properties = new HashSet<>();
        properties.add("pStudentAlergy");
        Map<String, Object> readProperties = studentManager.getMedicalInfoProperties(UUID.randomUUID().toString(), transaction, STUDENT_KEY,
                properties);
        Map<String, Object> mockProperties = createStudentMedicalInfo();

        assertEquals(mockProperties.size(), readProperties.size());
        for (String key : mockProperties.keySet()) {
            assertEquals(mockProperties.get(key), readProperties.get(key));
        }
    }

    @Test
    public void testGetPersonalInfoProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Student" });

        Set<String> properties = new HashSet<>();
        properties.add("pStudentLastName");
        properties.add("pStudentFirstName");
        Map<String, Object> readProperties = studentManager.getPersonalInfoProperties(UUID.randomUUID().toString(), transaction, STUDENT_KEY,
                properties);
        Map<String, Object> mockProperties = createStudentPersonalInfo();

        assertEquals(mockProperties.size(), readProperties.size());
        for (String key : mockProperties.keySet()) {
            assertEquals(mockProperties.get(key), readProperties.get(key));
        }
    }

    @Test
    public void testGetStudents() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Student" });

        Set<String> students = studentManager.getStudents(UUID.randomUUID().toString(), transaction);

        assertEquals(1, students.size());
        assertEquals(STUDENT_KEY, students.iterator().next());
    }

    private Map<String, Object> createStudentPersonalInfo() {
        Map<String, Object> personalInfo = new HashMap<String, Object>();

        personalInfo.put("pStudentLastName", "Dupont");
        personalInfo.put("pStudentFirstName", "Gilles");

        return personalInfo;
    }

    private Map<String, Object> createStudentMedicalInfo() {
        Map<String, Object> personalInfo = new HashMap<String, Object>();

        personalInfo.put("pStudentAlergy", "RAS");

        return personalInfo;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Student");

        StudentManager studentManager = new StudentManager();

        Map<String, Object> personalInfo = new StudentManagerTest().createStudentPersonalInfo();
        Map<String, Object> medicalInfo = new StudentManagerTest().createStudentMedicalInfo();

        String studentKey = studentManager.createStudent(UUID.randomUUID().toString(), transaction);
        studentManager.setPersonalInfoProperties(UUID.randomUUID().toString(), transaction, studentKey, personalInfo);
        studentManager.setMedicalInfoProperties(UUID.randomUUID().toString(), transaction, studentKey, medicalInfo);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
