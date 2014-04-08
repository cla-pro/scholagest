package net.scholagest.old.managers.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.old.managers.IPeriodManager;
import net.scholagest.old.managers.impl.PeriodManager;
import net.scholagest.old.managers.ontology.OntologyManager;
import net.scholagest.old.managers.ontology.RDF;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.utils.old.AbstractTestWithTransaction;
import net.scholagest.utils.old.DatabaseReaderWriter;
import net.scholagest.utils.old.InMemoryDatabase;
import net.scholagest.utils.old.InMemoryDatabase.InMemoryTransaction;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;

public class PeriodManagerTest extends AbstractTestWithTransaction {
    private static final String YEAR_NAME = "2012-2013";
    private static final String CLASS_NAME = "1P A";
    private static final String CLASS_KEY = CoreNamespace.classNs + "/" + YEAR_NAME + "#" + CLASS_NAME;
    private static final String PERIOD_NAME = "Trimestre 1";
    private static final String PERIOD_KEY = CoreNamespace.periodNs + "#" + "fddb452e-5d0d-4dfb-9898-19a686a0fe9e";

    private IPeriodManager periodManager = spy(new PeriodManager(new OntologyManager()));

    @Test
    public void testCreateNewPeriod() throws Exception {
        BaseObject period = periodManager.createPeriod(PERIOD_NAME, CLASS_KEY);

        assertEquals(CoreNamespace.tPeriod, period.getType());
        verify(transaction).insert(anyString(), eq(RDF.type), eq(CoreNamespace.tPeriod), anyString());
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

        BaseObject period = periodManager.createPeriod(PERIOD_NAME, CLASS_KEY);
        periodManager.setPeriodProperties(period.getKey(), periodProperties);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
