package net.scholagest.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ClassServiceTest extends AbstractTest {
    private InMemoryDatabase database;

    private String requestId = UUID.randomUUID().toString();

    private IClassBusinessComponent classBusinessComponent;
    private IClassService testee;

    @Before
    public void setUpTest() {
        database = Mockito.spy(new InMemoryDatabase());

        classBusinessComponent = Mockito.mock(IClassBusinessComponent.class);

        testee = new ClassService(database, classBusinessComponent);
    }

    @Test
    public void createClass() throws Exception {
        testee.createClass(requestId, new HashMap<String, Object>());

        Mockito.verify(classBusinessComponent).createClass(Mockito.eq(requestId), (ITransaction) Mockito.any(),
                Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void getClasses() throws Exception {
        testee.getClasses(requestId, new HashSet<String>());

        Mockito.verify(classBusinessComponent).getClassesForYear(Mockito.eq(requestId), (ITransaction) Mockito.any(), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void getClassProperties() throws Exception {
        String classKey = "classKey";
        testee.getClassProperties(requestId, classKey, new HashSet<String>());

        Mockito.verify(classBusinessComponent).getClassProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(), Mockito.eq(classKey),
                Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }
}
