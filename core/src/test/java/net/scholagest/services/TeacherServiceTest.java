package net.scholagest.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TeacherServiceTest extends AbstractTest {
    private InMemoryDatabase database;

    private String requestId = UUID.randomUUID().toString();

    private ITeacherBusinessComponent teacherBusinessComponent;
    private ITeacherService testee;

    @Before
    public void setUpTest() {
        database = Mockito.spy(new InMemoryDatabase());

        teacherBusinessComponent = Mockito.mock(ITeacherBusinessComponent.class);

        testee = new TeacherService(database, teacherBusinessComponent);
    }

    @Test
    public void testCreateTeacher() throws Exception {
        testee.createTeacher(requestId, null, new HashMap<String, Object>());

        Mockito.verify(teacherBusinessComponent).createTeacher(Mockito.eq(requestId), (ITransaction) Mockito.any(), Mockito.anyString(),
                Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testGetTeachers() throws Exception {
        testee.getTeachers(requestId);

        Mockito.verify(teacherBusinessComponent).getTeachers(Mockito.eq(requestId), (ITransaction) Mockito.any());
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testGetTeachersWithProperties() throws Exception {
        testee.getTeachersWithProperties(requestId, new HashSet<String>());

        Mockito.verify(teacherBusinessComponent).getTeachersWithProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(),
                Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testSetTeacherProperties() throws Exception {
        String teacherKey = "teacherKey";
        testee.setTeacherProperties(requestId, teacherKey, new HashMap<String, Object>());

        Mockito.verify(teacherBusinessComponent).setTeacherProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(), Mockito.eq(teacherKey),
                Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testGetTeacherProperties() throws Exception {
        String teacherKey = "teacherKey";
        testee.getTeacherProperties(requestId, teacherKey, new HashSet<String>());

        Mockito.verify(teacherBusinessComponent).getTeacherProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(), Mockito.eq(teacherKey),
                Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }
}
