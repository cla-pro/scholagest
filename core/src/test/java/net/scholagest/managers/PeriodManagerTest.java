package net.scholagest.managers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;

import org.junit.Test;
import org.mockito.Mockito;

public class PeriodManagerTest extends AbstractTestWithTransaction {
    private static final String YEAR_NAME = "2012-2013";
    private static final String CLASS_NAME = "1P A";
    private static final String BRANCH_NAME = "Math";
    private static final String PERIOD_NAME = "Trimestre 1";
    private static final String PERIOD_KEY = CoreNamespace.periodNs + "/" + YEAR_NAME + "/" + CLASS_NAME + "/" + BRANCH_NAME + "#" + PERIOD_NAME;

    private IPeriodManager periodManager = spy(new PeriodManager(new OntologyManager()));

    @Test
    public void testCreateNewPeriod() throws Exception {
        BaseObject period = periodManager.createPeriod(requestId, transaction, PERIOD_NAME, BRANCH_NAME, CLASS_NAME, YEAR_NAME);

        assertEquals(PERIOD_KEY, period.getKey());
        Mockito.verify(transaction).insert(PERIOD_KEY, RDF.type, CoreNamespace.tPeriod, null);
    }

    @Test
    public void testSetAndGetPeriodProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Period" });

        Map<String, Object> properties = createPeriodProperties();
        periodManager.setPeriodProperties(requestId, transaction, PERIOD_KEY, properties);
        BaseObject period = periodManager.getPeriodProperties(requestId, transaction, PERIOD_KEY, properties.keySet());

        Map<String, Object> readProperties = period.getProperties();
        assertEquals(properties.size(), readProperties.size());
        for (String key : properties.keySet()) {
            assertEquals(properties.get(key), readProperties.get(key));
        }
    }

    private Map<String, Object> createPeriodProperties() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put(CoreNamespace.pPeriodName, PERIOD_NAME);

        return personalProperties;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Period");

        PeriodManager periodManager = new PeriodManager(new OntologyManager());

        Map<String, Object> periodProperties = new PeriodManagerTest().createPeriodProperties();

        BaseObject period = periodManager.createPeriod(UUID.randomUUID().toString(), transaction, PERIOD_NAME, BRANCH_NAME, CLASS_NAME, YEAR_NAME);
        periodManager.setPeriodProperties(UUID.randomUUID().toString(), transaction, period.getKey(), periodProperties);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
