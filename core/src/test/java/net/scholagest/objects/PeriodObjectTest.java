package net.scholagest.objects;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;

import org.junit.Test;
import org.mockito.Mockito;

public class PeriodObjectTest {
    @Test
    public void testBranchObject() {
        ITransaction transaction = Mockito.mock(ITransaction.class);

        String periodKey = "periodKey";
        String classKey = "classKey";
        String name = "periodName";
        DBSet exams = new DBSet(transaction, "");
        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pPeriodClass, classKey);
        properties.put(CoreNamespace.pPeriodName, name);
        properties.put(CoreNamespace.pPeriodExams, exams);

        PeriodObject periodObject = new PeriodObject(periodKey);
        periodObject.setProperties(properties);

        assertEquals(periodKey, periodObject.getKey());
        assertEquals(CoreNamespace.tPeriod, periodObject.getType());
        assertEquals(classKey, periodObject.getClassKey());
        assertEquals(exams, periodObject.getExams());

        periodObject.setClassKey(classKey);
        assertEquals(classKey, periodObject.getClassKey());

        periodObject.setExams(exams);
        assertEquals(exams, periodObject.getExams());
    }
}
