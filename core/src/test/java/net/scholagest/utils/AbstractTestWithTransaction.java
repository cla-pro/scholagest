package net.scholagest.utils;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;

import java.util.Map;
import java.util.UUID;

import net.scholagest.database.DatabaseException;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;

public abstract class AbstractTestWithTransaction extends AbstractTest {
    private static final String KEYSPACE = "ScholagestSecheron";

    private InMemoryDatabase database;

    protected InMemoryTransaction transaction;
    protected String requestId = UUID.randomUUID().toString();

    @Before
    public void setUpTest() {
        database = new InMemoryDatabase();
        database.startup();
        transaction = spy(database.getTransaction(KEYSPACE));
    }

    @After
    public void shutdownTest() {
        if (transaction != null) {
            try {
                transaction.rollback();
            } catch (DatabaseException e) {
                fail(e.getMessage());
            }
        }
        if (database != null) {
            database.shutdown();
        }
    }

    public void fillTransactionWithDataSets(String[] dataSets) {
        Map<String, Map<String, Map<String, Object>>> dataSetsValues = new DatabaseReaderWriter().readDataSetsFromFile(
                "D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data", dataSets);

        for (String set : dataSets) {
            transaction.addValues(dataSetsValues.get(set));
        }
    }

    public void assertNoCallToTransaction() throws DatabaseException {
        Mockito.verify(transaction, Mockito.never()).insert(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyString());
        Mockito.verify(transaction, Mockito.never()).get(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(transaction, Mockito.never()).delete(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(transaction, Mockito.never()).getColumns(Mockito.anyString());
    }
}
