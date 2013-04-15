package net.scholagest.managers.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.scholagest.exception.ScholagestException;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;

import org.junit.Test;
import org.mockito.Mockito;

public class YearManagerTest extends AbstractTestWithTransaction {
    private static final String YEAR_NAME = "2012-2103";
    private static final String YEAR_KEY = CoreNamespace.yearNs + "#" + YEAR_NAME;
    private IYearManager yearManager = spy(new YearManager(new OntologyManager()));

    @Test
    public void testCreateNewYear() throws Exception {
        BaseObject year = yearManager.createNewYear(requestId, transaction, YEAR_NAME);

        Mockito.verify(transaction).insert(Mockito.anyString(), Mockito.eq(CoreNamespace.pYearName), Mockito.eq(YEAR_NAME), Mockito.anyString());
        assertEquals(YEAR_KEY, year.getKey());
    }

    @Test
    public void testGetCurrentYearKey() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "YearRunning" });

        assertEquals(YEAR_KEY, yearManager.getCurrentYearKey(requestId, transaction).getKey());
    }

    @Test
    public void testRestoreYear() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Year" });

        assertNull(yearManager.getCurrentYearKey(requestId, transaction));
        yearManager.restoreYear(requestId, transaction, YEAR_KEY);
        assertEquals(YEAR_KEY, yearManager.getCurrentYearKey(requestId, transaction).getKey());
    }

    @Test(expected = ScholagestException.class)
    public void testRestoreInexistentYear() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Year" });

        yearManager.restoreYear(requestId, transaction, YEAR_KEY + "bla");

        fail("Exception expected");
    }

    @Test(expected = ScholagestException.class)
    public void testRestoreYearWithAlreadyRunning() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "YearRunning" });

        yearManager.restoreYear(requestId, transaction, YEAR_KEY);

        fail("Exception expected");
    }

    @Test
    public void testBackupYear() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "YearRunning" });

        assertEquals(YEAR_KEY, yearManager.getCurrentYearKey(requestId, transaction).getKey());
        yearManager.backupYear(requestId, transaction);
        assertNull(yearManager.getCurrentYearKey(requestId, transaction));
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("YearRunning");

        IYearManager yearManager = new YearManager(new OntologyManager());

        BaseObject year = yearManager.createNewYear(UUID.randomUUID().toString(), transaction, YEAR_NAME);
        yearManager.restoreYear(UUID.randomUUID().toString(), transaction, year.getKey());
        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
