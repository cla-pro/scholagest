package net.scholagest.business.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.business.IPeriodBusinessComponent;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BaseObjectMock;
import net.scholagest.objects.PeriodObject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PeriodBusinessComponentTest {
    private IPeriodManager periodManager;

    private IPeriodBusinessComponent testee;

    @Before
    public void setup() {
        periodManager = Mockito.mock(IPeriodManager.class);

        testee = new PeriodBusinessComponent(periodManager);
    }

    @Test
    public void testSetPeriodProperties() throws Exception {
        String periodKey = "PERIOD_KEY";
        Map<String, Object> periodProperties = createPeriodProperties();
        testee.setPeriodProperties(periodKey, periodProperties);

        Mockito.verify(periodManager).setPeriodProperties(periodKey, periodProperties);
    }

    @Test
    public void testeGetPeriodProperties() throws Exception {
        String periodKey = "PERIOD_KEY";
        Map<String, Object> periodProperties = createPeriodProperties();
        new BaseObjectMock();
        PeriodObject mockPeriod = BaseObjectMock.createPeriodObject(periodKey, periodProperties);
        Mockito.when(periodManager.getPeriodProperties(periodKey, periodProperties.keySet())).thenReturn(mockPeriod);

        BaseObject periodObject = testee.getPeriodProperties(periodKey, periodProperties.keySet());

        assertEquals(mockPeriod.getKey(), periodObject.getKey());
        assertEquals(mockPeriod.getType(), periodObject.getType());
        assertEquals(mockPeriod.getProperties(), periodObject.getProperties());
        Mockito.verify(periodManager).getPeriodProperties(periodKey, periodProperties.keySet());
    }

    private Map<String, Object> createPeriodProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put("name", "Trimestre 1");

        return properties;
    }
}
