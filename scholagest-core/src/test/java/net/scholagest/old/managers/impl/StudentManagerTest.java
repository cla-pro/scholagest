package net.scholagest.old.managers.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.old.managers.IStudentManager;
import net.scholagest.old.managers.impl.StudentManager;
import net.scholagest.old.managers.ontology.OntologyManager;
import net.scholagest.old.managers.ontology.RDF;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.StudentObject;
import net.scholagest.utils.old.AbstractTestWithTransaction;
import net.scholagest.utils.old.DatabaseReaderWriter;
import net.scholagest.utils.old.InMemoryDatabase;
import net.scholagest.utils.old.InMemoryDatabase.InMemoryTransaction;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

public class StudentManagerTest extends AbstractTestWithTransaction {
    private static final String STUDENT_KEY = "student#329c1a51-5469-4eac-a182-29afb1e5ecdb";

    private IStudentManager studentManager = spy(new StudentManager(new OntologyManager()));

    @Test
    public void testCreateNewStudent() throws Exception {
        BaseObject student = studentManager.createStudent(createStudentPersonal());

        Mockito.verify(transaction).insert(Mockito.eq(CoreNamespace.studentsBase), Mockito.anyString(), Mockito.eq(student.getKey()),
                Mockito.anyString());
    }

    @Test
    public void testGetMedicalProperties() throws Exception {
        fillTransactionWithDataSets(new String[] { "Student" });

        Map<String, Object> mockProperties = createStudentMedical();
        BaseObject medicalProperties = studentManager.getMedicalProperties(STUDENT_KEY, mockProperties.keySet());

        assertMapEquals(mockProperties, medicalProperties.getProperties());
    }

    @Test
    public void testGetPersonalProperties() throws Exception {
        fillTransactionWithDataSets(new String[] { "Student" });

        Map<String, Object> mockProperties = createStudentPersonal();
        BaseObject personalProperties = studentManager.getPersonalProperties(STUDENT_KEY, mockProperties.keySet());

        assertMapEquals(mockProperties, personalProperties.getProperties());
    }

    @Test
    public void testGetStudents() throws Exception {
        fillTransactionWithDataSets(new String[] { "Student" });

        Set<StudentObject> students = studentManager.getStudents();

        assertEquals(1, students.size());
        assertEquals(STUDENT_KEY, students.iterator().next().getKey());
    }

    @Test
    public void testGetStudentProperties() throws Exception {
        fillTransactionWithDataSets(new String[] { "Student" });

        StudentObject studentProperties = studentManager.getStudentProperties(STUDENT_KEY, new HashSet<>(Arrays.asList(RDF.type)));

        assertEquals(STUDENT_KEY, studentProperties.getKey());
        assertEquals(CoreNamespace.tStudent, studentProperties.getType());

        assertNotNull(studentProperties.getMedicalInfoKey());
        assertNotNull(studentProperties.getPersonalInfoKey());
    }

    @Test
    @Ignore
    public void testGetStudentGrades() {
        fillTransactionWithDataSets(new String[] { "Student" });

        // studentManager.getStudentGrades(, STUDENT_KEY,
        // examKeys, yearKey);
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
        ScholagestThreadLocal.setTransaction(transaction);

        StudentManager studentManager = new StudentManager(new OntologyManager());

        Map<String, Object> personalProperties = new StudentManagerTest().createStudentPersonal();
        Map<String, Object> medicalProperties = new StudentManagerTest().createStudentMedical();

        BaseObject studentKey = studentManager.createStudent(personalProperties);
        studentManager.setMedicalProperties(studentKey.getKey(), medicalProperties);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
