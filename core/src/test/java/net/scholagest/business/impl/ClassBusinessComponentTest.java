package net.scholagest.business.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BaseObjectMock;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
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
        Mockito.when(yearManager.getYearProperties(Mockito.anyString(), Mockito.anySet())).thenReturn(new BaseObject(null, null));

        classManager = Mockito.mock(IClassManager.class);
        Mockito.when(classManager.createClass(Mockito.anyString(), Mockito.anyString())).thenReturn(new BaseObject(CLASS_KEY, CoreNamespace.tClass));
        Mockito.when(classManager.getClasses(Mockito.anyString())).thenReturn(new HashSet<BaseObject>());

        Set<BaseObject> classKeys = new HashSet<>();
        classKeys.add(new BaseObject(CLASS_KEY, CoreNamespace.tClass));
        Mockito.when(classManager.getClasses(Mockito.eq(YEAR_KEY))).thenReturn(classKeys);

        Mockito.when(classManager.getClassProperties(Mockito.anyString(), Mockito.anySet())).thenReturn(null);
        Mockito.when(classManager.getClassProperties(Mockito.eq(CLASS_KEY), Mockito.eq(createProperties().keySet()))).thenReturn(
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
        Map<String, Object> classProperties = new HashMap<>();
        BaseObject classKey = testee.createClass(classProperties, CLASS_NAME, YEAR_KEY);

        assertEquals(CLASS_KEY, classKey.getKey());
        Mockito.verify(classManager).createClass(CLASS_NAME, null);
        Mockito.verify(classManager).setClassProperties(Mockito.eq(CLASS_KEY), Mockito.argThat(new BaseMatcher<Map<String, Object>>() {
            @SuppressWarnings("unchecked")
            @Override
            public boolean matches(Object item) {
                Map<String, Object> properties = (Map<String, Object>) item;
                boolean hasClassName = properties.get(CoreNamespace.pClassName).equals(CLASS_NAME);
                boolean hasYearKey = properties.get(CoreNamespace.pClassYear).equals(YEAR_KEY);
                return hasClassName && hasYearKey;
            }

            @Override
            public void describeTo(Description description) {}
        }));
        Mockito.verify(yearManager).addClassToYear(YEAR_KEY, CLASS_KEY);
    }

    @Test
    public void testGetClassesForYears() throws Exception {
        Set<String> yearKeySet = new HashSet<>();
        yearKeySet.add(YEAR_KEY);
        yearKeySet.add("testKey");
        Map<String, Set<BaseObject>> classesForYear = testee.getClassesForYears(yearKeySet);

        Mockito.verify(classManager).getClasses(Mockito.eq(YEAR_KEY));
        Mockito.verify(classManager).getClasses(Mockito.eq("testKey"));

        assertEquals(2, classesForYear.size());
        assertEquals(1, classesForYear.get(YEAR_KEY).size());
        assertEquals(0, classesForYear.get("testKey").size());
    }

    @Test
    public void testGetClassProperties() throws Exception {
        Map<String, Object> mockProperties = createProperties();
        BaseObject classProperties = testee.getClassProperties(CLASS_KEY, mockProperties.keySet());

        Mockito.verify(classManager).getClassProperties(Mockito.eq(CLASS_KEY), Mockito.eq(mockProperties.keySet()));

        assertMapEquals(mockProperties, classProperties.getProperties());
    }

    @Test
    public void testSetClassProperties() throws Exception {
        Map<String, Object> mockProperties = createProperties();
        testee.setClassProperties(CLASS_KEY, mockProperties);

        Mockito.verify(classManager).setClassProperties(Mockito.eq(CLASS_KEY), Mockito.eq(mockProperties));
    }
}
