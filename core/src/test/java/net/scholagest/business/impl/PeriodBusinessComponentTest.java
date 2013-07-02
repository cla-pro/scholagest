package net.scholagest.business.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.business.IPeriodBusinessComponent;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BaseObjectMock;

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
        BaseObject baseObject = BaseObjectMock.createBaseObject(periodKey, CoreNamespace.tPeriod, periodProperties);
        Mockito.when(periodManager.getPeriodProperties(periodKey, periodProperties.keySet())).thenReturn(baseObject);

        BaseObject periodObject = testee.getPeriodProperties(periodKey, periodProperties.keySet());

        assertEquals(baseObject.getKey(), periodObject.getKey());
        assertEquals(baseObject.getType(), periodObject.getType());
        assertEquals(baseObject.getProperties(), periodObject.getProperties());
        Mockito.verify(periodManager).getPeriodProperties(periodKey, periodProperties.keySet());
    }

    private Map<String, Object> createPeriodProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put("name", "Trimestre 1");

        return properties;
    }
}
