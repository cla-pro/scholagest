package net.scholagest.old.business.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.old.business.IYearBusinessComponent;
import net.scholagest.old.business.impl.YearBusinessComponent;
import net.scholagest.old.managers.IYearManager;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.BaseObjectMock;
import net.scholagest.utils.old.AbstractTestWithTransaction;

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
        Mockito.when(yearManager.createNewYear(Mockito.anyString())).thenReturn(new BaseObject(YEAR_KEY, CoreNamespace.tYear));
        Mockito.when(yearManager.getCurrentYearKey()).thenReturn(new BaseObject(YEAR_KEY, CoreNamespace.tYear));
        Mockito.when(yearManager.getYears()).thenReturn(new HashSet<>(Arrays.asList(new BaseObject(YEAR_KEY, CoreNamespace.tYear))));
        Mockito.when(yearManager.getYearProperties(Mockito.eq(YEAR_KEY), Mockito.eq(createYearProperties().keySet()))).thenReturn(
                BaseObjectMock.createBaseObject(YEAR_KEY, CoreNamespace.tYear, createYearProperties()));

        testee = new YearBusinessComponent(yearManager);
    }

    private Map<String, Object> createYearProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(CoreNamespace.pYearName, YEAR_NAME);

        return properties;
    }

    @Test
    public void testStartYear() throws Exception {
        BaseObject year = testee.startYear(YEAR_NAME);

        assertEquals(YEAR_KEY, year.getKey());
        Mockito.verify(yearManager).restoreYear(year.getKey());
        assertNoCallToTransaction();
    }

    @Test
    public void testStartYearNameAlreadyUsed() throws Exception {
        Mockito.when(yearManager.checkWhetherYearExists(YEAR_NAME)).thenReturn(true);
        try {
            BaseObject year = testee.startYear(YEAR_NAME);

            assertEquals(YEAR_KEY, year.getKey());
            Mockito.verify(yearManager).restoreYear(year.getKey());
            assertNoCallToTransaction();
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.OBJECT_ALREADY_EXISTS, e.getErrorCode());
        }
    }

    @Test
    public void testStopYear() throws Exception {
        testee.stopYear();

        Mockito.verify(yearManager).backupYear();
        assertNoCallToTransaction();
    }

    @Test
    public void testGetCurrentYearKey() throws Exception {
        BaseObject currentYear = testee.getCurrentYearKey();

        assertEquals(YEAR_KEY, currentYear.getKey());
        Mockito.verify(yearManager).getCurrentYearKey();
        assertNoCallToTransaction();
    }

    @Test
    public void testGetYearsWithProperties() throws Exception {
        Map<String, Object> properties = createYearProperties();
        Set<BaseObject> yearsWithProperties = testee.getYearsWithProperties(properties.keySet());

        assertEquals(1, yearsWithProperties.size());
        Map<String, Object> readProperties = yearsWithProperties.iterator().next().getProperties();
        assertMapEquals(properties, readProperties);
        assertNoCallToTransaction();
    }
}
