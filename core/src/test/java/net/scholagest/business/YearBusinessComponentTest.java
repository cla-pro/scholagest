package net.scholagest.business;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.IYearManager;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class YearBusinessComponentTest extends AbstractTestWithTransaction {
    private static final String YEAR_NAME = "2012-2103";
    private static final String YEAR_KEY = CoreNamespace.yearNs + "#" + YEAR_NAME;

    private IYearManager yearManager;
    private IYearBusinessComponent testee;

    @Before
    public void setup() throws Exception {
        yearManager = Mockito.mock(IYearManager.class);
        Mockito.when(yearManager.createNewYear(Mockito.anyString(), Mockito.eq(transaction), Mockito.anyString())).thenReturn(YEAR_KEY);
        Mockito.when(yearManager.getCurrentYearKey(Mockito.anyString(), Mockito.eq(transaction))).thenReturn(YEAR_KEY);
        Mockito.when(yearManager.getYears(Mockito.anyString(), Mockito.eq(transaction))).thenReturn(new HashSet<String>(Arrays.asList(YEAR_KEY)));
        Mockito.when(
                yearManager.getYearProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(YEAR_KEY),
                        Mockito.eq(createYearProperties().keySet()))).thenReturn(createYearProperties());

        testee = new YearBusinessComponent(yearManager);
    }

    private Map<String, Object> createYearProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(CoreNamespace.pYearName, YEAR_NAME);

        return properties;
    }

    @Test
    public void testStartYear() throws Exception {
        String yearKey = testee.startYear(requestId, transaction, YEAR_NAME);

        assertEquals(YEAR_KEY, yearKey);
        Mockito.verify(yearManager).restoreYear(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(yearKey));
        assertNoCallToTransaction();
    }

    @Test
    public void testStopYear() throws Exception {
        testee.stopYear(requestId, transaction);

        Mockito.verify(yearManager).backupYear(Mockito.anyString(), Mockito.eq(transaction));
        assertNoCallToTransaction();
    }

    @Test
    public void testGetCurrentYearKey() throws Exception {
        String currentYearKey = testee.getCurrentYearKey(requestId, transaction);

        assertEquals(YEAR_KEY, currentYearKey);
        Mockito.verify(yearManager).getCurrentYearKey(Mockito.anyString(), Mockito.eq(transaction));
        assertNoCallToTransaction();
    }

    @Test
    public void testGetYearsWithProperties() throws Exception {
        Map<String, Object> properties = createYearProperties();
        Map<String, Map<String, Object>> yearsWithProperties = testee.getYearsWithProperties(requestId, transaction, properties.keySet());

        assertEquals(1, yearsWithProperties.size());
        Map<String, Object> readProperties = yearsWithProperties.get(YEAR_KEY);
        assertMapEquals(properties, readProperties);
        assertNoCallToTransaction();
    }
}
