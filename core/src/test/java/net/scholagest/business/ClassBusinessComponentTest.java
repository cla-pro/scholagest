package net.scholagest.business;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BaseObjectMock;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ClassBusinessComponentTest extends AbstractTestWithTransaction {
    private static final String CLASS_NAME = "1P A";
    private static final String YEAR_NAME = "2012-2013";
    private static final String CLASS_KEY = CoreNamespace.classNs + "/" + YEAR_NAME + "#" + CLASS_NAME;
    private static final String YEAR_KEY = CoreNamespace.yearNs + "#" + YEAR_NAME;

    private IClassManager classManager;
    private IYearManager yearManager;

    private IClassBusinessComponent testee;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        yearManager = Mockito.mock(IYearManager.class);
        Mockito.when(yearManager.getYearProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.anyString(), Mockito.anySet())).thenReturn(
                new BaseObject(null, null));

        classManager = Mockito.mock(IClassManager.class);
        Mockito.when(classManager.createClass(Mockito.anyString(), Mockito.eq(transaction), Mockito.anyString(), Mockito.anyString())).thenReturn(
                new BaseObject(CLASS_KEY, CoreNamespace.tClass));
        Mockito.when(classManager.getClasses(Mockito.anyString(), Mockito.eq(transaction), Mockito.anyString()))
                .thenReturn(new HashSet<BaseObject>());

        Set<BaseObject> classKeys = new HashSet<>();
        classKeys.add(new BaseObject(CLASS_KEY, CoreNamespace.tClass));
        Mockito.when(classManager.getClasses(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(YEAR_KEY))).thenReturn(classKeys);

        Mockito.when(classManager.getClassProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.anyString(), Mockito.anySet()))
                .thenReturn(null);
        Mockito.when(
                classManager.getClassProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(CLASS_KEY),
                        Mockito.eq(createProperties().keySet()))).thenReturn(
                BaseObjectMock.createBaseObject(CLASS_KEY, CoreNamespace.tClass, createProperties()));

        testee = new ClassBusinessComponent(classManager, yearManager);
    }

    private Map<String, Object> createProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(CoreNamespace.pClassName, "1P A");

        return properties;
    }

    @Test
    public void testCreateClass() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(CoreNamespace.pClassYear, YEAR_KEY);
        BaseObject classKey = testee.createClass(requestId, transaction, properties);

        assertEquals(CLASS_KEY, classKey.getKey());
        Mockito.verify(classManager).createClass(Mockito.anyString(), Mockito.eq(transaction), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(yearManager).addClassToYear(Mockito.eq(requestId), Mockito.eq(transaction), Mockito.eq(YEAR_KEY), Mockito.eq(CLASS_KEY));
    }

    @Test
    public void testGetClassesForYears() throws Exception {
        Set<String> yearKeySet = new HashSet<>();
        yearKeySet.add(YEAR_KEY);
        yearKeySet.add("testKey");
        Map<String, Set<BaseObject>> classesForYear = testee.getClassesForYears(requestId, transaction, yearKeySet);

        Mockito.verify(classManager).getClasses(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(YEAR_KEY));
        Mockito.verify(classManager).getClasses(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq("testKey"));

        assertEquals(2, classesForYear.size());
        assertEquals(1, classesForYear.get(YEAR_KEY).size());
        assertEquals(0, classesForYear.get("testKey").size());
    }

    @Test
    public void testGetClassProperties() throws Exception {
        Map<String, Object> mockProperties = createProperties();
        BaseObject classProperties = testee.getClassProperties(requestId, transaction, CLASS_KEY, mockProperties.keySet());

        Mockito.verify(classManager).getClassProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(CLASS_KEY),
                Mockito.eq(mockProperties.keySet()));

        assertMapEquals(mockProperties, classProperties.getProperties());
    }

    @Test
    public void testSetClassProperties() throws Exception {
        Map<String, Object> mockProperties = createProperties();
        testee.setClassProperties(requestId, transaction, CLASS_KEY, mockProperties);

        Mockito.verify(classManager).setClassProperties(Mockito.anyString(), Mockito.eq(transaction), Mockito.eq(CLASS_KEY),
                Mockito.eq(mockProperties));
    }
}
