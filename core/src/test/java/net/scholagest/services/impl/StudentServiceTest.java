package net.scholagest.services.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.services.IStudentService;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class StudentServiceTest extends AbstractTest {
    private InMemoryDatabase database;

    private String requestId = UUID.randomUUID().toString();

    private IStudentBusinessComponent studentBusinessComponent;
    private IStudentService testee;

    @Before
    public void setup() {
        database = Mockito.spy(new InMemoryDatabase());

        studentBusinessComponent = Mockito.mock(IStudentBusinessComponent.class);

        testee = new StudentService(database, studentBusinessComponent);
    }

    @Test
    public void testCreateStudent() throws Exception {
        testee.createStudent(requestId, new HashMap<String, Object>());

        Mockito.verify(studentBusinessComponent).createStudent(Mockito.eq(requestId), (ITransaction) Mockito.any(),
                Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testUpdateStudentProperties() throws Exception {
        String studentKey = "studentKey";
        testee.updateStudentProperties(requestId, studentKey, new HashMap<String, Object>(), new HashMap<String, Object>());

        Mockito.verify(studentBusinessComponent).updateStudentProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(), Mockito.eq(studentKey),
                Mockito.anyMapOf(String.class, Object.class), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testGetStudentPersonalProperties() throws Exception {
        String studentKey = "studentKey";
        testee.getStudentPersonalProperties(requestId, studentKey, new HashSet<String>());

        Mockito.verify(studentBusinessComponent).getStudentPersonalProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(),
                Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testGetStudentMedicalProperties() throws Exception {
        String studentKey = "studentKey";
        testee.getStudentMedicalProperties(requestId, studentKey, new HashSet<String>());

        Mockito.verify(studentBusinessComponent).getStudentMedicalProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(),
                Mockito.eq(studentKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testGetStudentsWithProperties() throws Exception {
        testee.getStudentsWithProperties(requestId, new HashSet<String>());

        Mockito.verify(studentBusinessComponent).getStudentsWithProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(),
                Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }
}
