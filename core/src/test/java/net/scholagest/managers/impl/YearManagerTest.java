package net.scholagest.managers.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.exception.ScholagestException;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.mockito.Mockito;

public class YearManagerTest extends AbstractTestWithTransaction {
    private static final String YEAR_NAME = "2012-2103";
    private static final String YEAR_KEY = CoreNamespace.yearNs + "#" + YEAR_NAME;
    private IYearManager yearManager = spy(new YearManager(new OntologyManager()));

    @Test
    public void testCheckWhetherYearExists() {
        super.fillTransactionWithDataSets(new String[] { "Year" });

        assertTrue(yearManager.checkWhetherYearExists(YEAR_NAME));
        assertFalse(yearManager.checkWhetherYearExists("2011-2012"));
    }

    @Test
    public void testCreateNewYear() throws Exception {
        BaseObject year = yearManager.createNewYear(YEAR_NAME);

        assertEquals(CoreNamespace.tYear, year.getType());
        Mockito.verify(transaction).insert(Mockito.anyString(), Mockito.eq(CoreNamespace.pYearName), Mockito.eq(YEAR_NAME), Mockito.anyString());
    }

    @Test
    public void testGetCurrentYearKey() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "YearRunning" });

        assertEquals(YEAR_KEY, yearManager.getCurrentYearKey().getKey());
    }

    @Test
    public void testRestoreYear() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Year" });

        assertNull(yearManager.getCurrentYearKey());
        yearManager.restoreYear(YEAR_KEY);
        assertEquals(YEAR_KEY, yearManager.getCurrentYearKey().getKey());
    }

    @Test(expected = ScholagestException.class)
    public void testRestoreInexistentYear() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Year" });

        yearManager.restoreYear(YEAR_KEY + "bla");

        fail("Exception expected");
    }

    @Test(expected = ScholagestException.class)
    public void testRestoreYearWithAlreadyRunning() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "YearRunning" });

        yearManager.restoreYear(YEAR_KEY);

        fail("Exception expected");
    }

    @Test
    public void testBackupYear() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "YearRunning" });

        assertEquals(YEAR_KEY, yearManager.getCurrentYearKey().getKey());
        yearManager.backupYear();
        assertNull(yearManager.getCurrentYearKey());
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("YearRunning");
        ScholagestThreadLocal.setTransaction(transaction);

        IYearManager yearManager = new YearManager(new OntologyManager());

        BaseObject year = yearManager.createNewYear(YEAR_NAME);
        yearManager.restoreYear(year.getKey());
        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
