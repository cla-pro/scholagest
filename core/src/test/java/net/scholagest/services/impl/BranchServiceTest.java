package net.scholagest.services.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.services.IBranchService;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BranchServiceTest {
    private static final String CLASS_KEY = "classKey";
    private InMemoryDatabase database;

    private String requestId = UUID.randomUUID().toString();

    private IBranchBusinessComponent branchBusinessComponent;
    private IBranchService testee;

    @Before
    public void setUpTest() {
        database = Mockito.spy(new InMemoryDatabase());

        branchBusinessComponent = Mockito.mock(IBranchBusinessComponent.class);

        testee = new BranchService(database, branchBusinessComponent);
    }

    @Test
    public void testCreateBranch() throws Exception {
        testee.createBranch(requestId, CLASS_KEY, new HashMap<String, Object>());

        Mockito.verify(branchBusinessComponent).createBranch(Mockito.eq(requestId), (ITransaction) Mockito.any(), Mockito.eq(CLASS_KEY),
                Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testGetBranchProperties() throws Exception {
        String branchKey = "branchKey";
        testee.getBranchProperties(requestId, branchKey, new HashSet<String>());

        Mockito.verify(branchBusinessComponent).getBranchProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(), Mockito.eq(branchKey),
                Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testSetBranchProperties() throws Exception {
        String branchKey = "branchKey";
        Map<String, Object> properties = new HashMap<String, Object>();
        testee.setBranchProperties(requestId, branchKey, properties);

        Mockito.verify(branchBusinessComponent).setBranchProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(), Mockito.eq(branchKey),
                Mockito.eq(properties));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }
}
