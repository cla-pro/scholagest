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

public class TeacherManagerTest extends AbstractTestWithTransaction {
    private static final String TEACHER_KEY = "http://scholagest.net/teacher#e85af55b-8b34-4646-a872-1a6e9c210fe2";
    private TeacherManager teacherManager = spy(new TeacherManager());

    @Test
    public void testGetProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Teacher" });

        Set<String> properties = new HashSet<>();
        properties.add("pTeacherLastName");
        properties.add("pTeacherFirstName");
        Map<String, Object> readProperties = teacherManager.getTeacherProperties(UUID.randomUUID().toString(), transaction, TEACHER_KEY, properties);
        Map<String, Object> mockProperties = createTeacherInfo();

        assertEquals(mockProperties.size(), readProperties.size());
        for (String key : mockProperties.keySet()) {
            assertEquals(mockProperties.get(key), readProperties.get(key));
        }
    }

    @Test
    public void testGetTeachers() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Teacher" });

        Set<String> teachers = teacherManager.getTeachers(UUID.randomUUID().toString(), transaction);

        assertEquals(1, teachers.size());
        assertEquals(TEACHER_KEY, teachers.iterator().next());
    }

    private Map<String, Object> createTeacherInfo() {
        Map<String, Object> personalInfo = new HashMap<String, Object>();

        personalInfo.put("pTeacherLastName", "Dupont");
        personalInfo.put("pTeacherFirstName", "Gilles");

        return personalInfo;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Teacher");

        TeacherManager teacherManager = new TeacherManager();

        Map<String, Object> teacherInfo = new TeacherManagerTest().createTeacherInfo();

        String teacherKey = teacherManager.createTeacher(UUID.randomUUID().toString(), transaction);
        teacherManager.setTeacherProperties(UUID.randomUUID().toString(), transaction, teacherKey, teacherInfo);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
