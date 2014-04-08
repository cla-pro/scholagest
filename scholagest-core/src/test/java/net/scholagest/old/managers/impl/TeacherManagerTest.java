package net.scholagest.old.managers.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.old.managers.ITeacherManager;
import net.scholagest.old.managers.impl.TeacherManager;
import net.scholagest.old.managers.ontology.OntologyManager;
import net.scholagest.old.managers.ontology.RDF;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.TeacherObject;
import net.scholagest.utils.old.AbstractTestWithTransaction;
import net.scholagest.utils.old.DatabaseReaderWriter;
import net.scholagest.utils.old.InMemoryDatabase;
import net.scholagest.utils.old.InMemoryDatabase.InMemoryTransaction;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;

public class TeacherManagerTest extends AbstractTestWithTransaction {
    private static final String TEACHER_KEY = "teacher#e85af55b-8b34-4646-a872-1a6e9c210fe2";

    private ITeacherManager teacherManager = spy(new TeacherManager(new OntologyManager()));

    @Test
    public void testCreateNewTeacher() throws Exception {
        BaseObject teacher = teacherManager.createTeacher(new HashMap<String, Object>());

        verify(transaction).insert(anyString(), eq(RDF.type), eq(CoreNamespace.tTeacher), anyString());
        verify(transaction).insert(eq(CoreNamespace.teachersBase), anyString(), eq(teacher.getKey()), anyString());
    }

    @Test
    public void testSetAndGetTeacherProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Teacher" });

        Map<String, Object> properties = createTeacherProperties();
        teacherManager.setTeacherProperties(TEACHER_KEY, properties);
        TeacherObject teacher = teacherManager.getTeacherProperties(TEACHER_KEY, properties.keySet());

        assertEquals("Dupont", teacher.getProperty("pTeacherLastName"));
    }

    @Test
    public void testGetTeachers() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Teacher" });

        Set<TeacherObject> teachers = teacherManager.getTeachers();

        assertEquals(1, teachers.size());
        assertEquals(TEACHER_KEY, teachers.iterator().next().getKey());
    }

    private Map<String, Object> createTeacherProperties() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put("pTeacherLastName", "Dupont");
        personalProperties.put("pTeacherFirstName", "Gilles");

        return personalProperties;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Teacher");
        ScholagestThreadLocal.setTransaction(transaction);

        TeacherManager teacherManager = new TeacherManager(new OntologyManager());

        Map<String, Object> teacherProperties = new TeacherManagerTest().createTeacherProperties();

        teacherManager.createTeacher(teacherProperties);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
