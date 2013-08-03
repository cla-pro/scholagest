package net.scholagest.managers.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.mockito.Mockito;

public class PeriodManagerTest extends AbstractTestWithTransaction {
    private static final String YEAR_NAME = "2012-2013";
    private static final String CLASS_NAME = "1P A";
    private static final String CLASS_KEY = CoreNamespace.classNs + "/" + YEAR_NAME + "#" + CLASS_NAME;
    private static final String BRANCH_NAME = "Math";
    private static final String PERIOD_NAME = "Trimestre 1";
    private static final String PERIOD_KEY = CoreNamespace.periodNs + "/" + YEAR_NAME + "/" + CLASS_NAME + "/" + BRANCH_NAME + "#" + PERIOD_NAME;

    private IPeriodManager periodManager = spy(new PeriodManager(new OntologyManager()));

    @Test
    public void testCreateNewPeriod() throws Exception {
        BaseObject period = periodManager.createPeriod(PERIOD_NAME, CLASS_KEY, BRANCH_NAME, CLASS_NAME, YEAR_NAME);

        assertEquals(PERIOD_KEY, period.getKey());
        Mockito.verify(transaction).insert(PERIOD_KEY, RDF.type, CoreNamespace.tPeriod, null);
    }

    @Test
    public void testSetAndGetPeriodProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Period" });

        Map<String, Object> properties = createPeriodProperties();
        periodManager.setPeriodProperties(PERIOD_KEY, properties);
        BaseObject period = periodManager.getPeriodProperties(PERIOD_KEY, properties.keySet());

        assertEquals(PERIOD_NAME, period.getProperty(CoreNamespace.pPeriodName));
    }

    private Map<String, Object> createPeriodProperties() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put(CoreNamespace.pPeriodName, PERIOD_NAME);

        return personalProperties;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Period");
        ScholagestThreadLocal.setTransaction(transaction);

        PeriodManager periodManager = new PeriodManager(new OntologyManager());

        Map<String, Object> periodProperties = new PeriodManagerTest().createPeriodProperties();

        BaseObject period = periodManager.createPeriod(PERIOD_NAME, CLASS_KEY, BRANCH_NAME, CLASS_NAME, YEAR_NAME);
        periodManager.setPeriodProperties(period.getKey(), periodProperties);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
