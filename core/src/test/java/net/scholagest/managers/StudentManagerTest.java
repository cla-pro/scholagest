package net.scholagest.managers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;

import org.junit.Test;
import org.mockito.Mockito;

public class StudentManagerTest extends AbstractTestWithTransaction {
    private static final String STUDENT_KEY = "http://scholagest.net/student#329c1a51-5469-4eac-a182-29afb1e5ecdb";

    private IStudentManager studentManager = spy(new StudentManager(new OntologyManager()));

    @Test
    public void testCreateNewStudent() throws Exception {
        BaseObject student = studentManager.createStudent(requestId, transaction);

        Mockito.verify(transaction).insert(Mockito.eq(CoreNamespace.studentsBase), Mockito.anyString(), Mockito.eq(student.getKey()),
                Mockito.anyString());
    }

    @Test
    public void testGetMedicalProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Student" });

        Map<String, Object> mockProperties = createStudentMedical();
        BaseObject medicalProperties = studentManager.getMedicalProperties(requestId, transaction, STUDENT_KEY, mockProperties.keySet());

        assertMapEquals(mockProperties, medicalProperties.getProperties());
    }

    @Test
    public void testGetPersonalProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Student" });

        Map<String, Object> mockProperties = createStudentPersonal();
        BaseObject personalProperties = studentManager.getPersonalProperties(requestId, transaction, STUDENT_KEY, mockProperties.keySet());

        assertMapEquals(mockProperties, personalProperties.getProperties());
    }

    @Test
    public void testGetStudents() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Student" });

        Set<BaseObject> students = studentManager.getStudents(requestId, transaction);

        assertEquals(1, students.size());
        assertEquals(STUDENT_KEY, students.iterator().next().getKey());
    }

    private Map<String, Object> createStudentPersonal() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put("pStudentLastName", "Dupont");
        personalProperties.put("pStudentFirstName", "Gilles");

        return personalProperties;
    }

    private Map<String, Object> createStudentMedical() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put("pStudentAlergy", "RAS");

        return personalProperties;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Student");

        StudentManager studentManager = new StudentManager(new OntologyManager());

        Map<String, Object> personalProperties = new StudentManagerTest().createStudentPersonal();
        Map<String, Object> medicalProperties = new StudentManagerTest().createStudentMedical();

        BaseObject studentKey = studentManager.createStudent(UUID.randomUUID().toString(), transaction);
        studentManager.setPersonalProperties(UUID.randomUUID().toString(), transaction, studentKey.getKey(), personalProperties);
        studentManager.setMedicalProperties(UUID.randomUUID().toString(), transaction, studentKey.getKey(), medicalProperties);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
