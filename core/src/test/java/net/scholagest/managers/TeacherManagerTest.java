package net.scholagest.managers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;

import org.junit.Test;
import org.mockito.Mockito;

public class TeacherManagerTest extends AbstractTestWithTransaction {
    private static final String TEACHER_KEY = "http://scholagest.net/teacher#e85af55b-8b34-4646-a872-1a6e9c210fe2";

    private ITeacherManager teacherManager = spy(new TeacherManager(new OntologyManager()));

    @Test
    public void testCreateNewTeacher() throws Exception {
        String classKey = teacherManager.createTeacher(requestId, transaction);

        Mockito.verify(transaction).insert(Mockito.eq(CoreNamespace.teachersBase), Mockito.anyString(), Mockito.eq(classKey), Mockito.anyString());
    }

    @Test
    public void testSetAndGetClassProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Teacher" });

        Map<String, Object> properties = createTeacherProperties();
        teacherManager.setTeacherProperties(requestId, transaction, TEACHER_KEY, properties);
        Map<String, Object> readProperties = teacherManager.getTeacherProperties(requestId, transaction, TEACHER_KEY, properties.keySet());

        assertEquals(properties.size(), readProperties.size());
        for (String key : properties.keySet()) {
            assertEquals(properties.get(key), readProperties.get(key));
        }
    }

    @Test
    public void testGetTeachers() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Teacher" });

        Set<String> teachers = teacherManager.getTeachers(requestId, transaction);

        assertEquals(1, teachers.size());
        assertEquals(TEACHER_KEY, teachers.iterator().next());
    }

    private Map<String, Object> createTeacherProperties() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put("pTeacherLastName", "Dupont");
        personalProperties.put("pTeacherFirstName", "Gilles");

        return personalProperties;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Teacher");

        TeacherManager teacherManager = new TeacherManager(new OntologyManager());

        Map<String, Object> teacherProperties = new TeacherManagerTest().createTeacherProperties();

        String teacherKey = teacherManager.createTeacher(UUID.randomUUID().toString(), transaction);
        teacherManager.setTeacherProperties(UUID.randomUUID().toString(), transaction, teacherKey, teacherProperties);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
