package net.scholagest.business.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BaseObjectMock;
import net.scholagest.services.kdom.KSet;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BranchBusinessComponentTest extends AbstractTestWithTransaction {
    private static final String BRANCH_PERIOD_SET = UUID.randomUUID().toString();
    private static final String BRANCH_NAME = "Math";
    private static final String CLASS_NAME = "1P A";
    private static final String YEAR_NAME = "2012-2013";
    private static final String BRANCH_KEY = CoreNamespace.branchNs + "/" + YEAR_NAME + "/" + CLASS_NAME + "#" + BRANCH_NAME;
    private static final String CLASS_KEY = CoreNamespace.classNs + "/" + YEAR_NAME + "#" + CLASS_NAME;
    private static final String YEAR_KEY = CoreNamespace.yearNs + "#" + YEAR_NAME;

    private IPeriodManager periodManager;
    private IBranchManager branchManager;
    private IClassManager classManager;
    private IYearManager yearManager;

    private IBranchBusinessComponent testee;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        yearManager = Mockito.mock(IYearManager.class);
        Mockito.when(yearManager.getYearProperties(Mockito.anyString(), Mockito.anySet())).thenReturn(
                BaseObjectMock.createBaseObject(YEAR_KEY, CoreNamespace.tYear, createYearProperties()));

        classManager = Mockito.mock(IClassManager.class);
        Mockito.when(classManager.getClassProperties(Mockito.eq(CLASS_KEY), Mockito.anySet())).thenReturn(
                BaseObjectMock.createBaseObject(CLASS_KEY, CoreNamespace.tClass, createClassProperties()));

        branchManager = Mockito.mock(IBranchManager.class);
        Mockito.when(branchManager.createBranch(Mockito.eq(BRANCH_NAME), Mockito.eq(CLASS_NAME), Mockito.eq(YEAR_NAME))).thenReturn(
                BaseObjectMock.createBaseObject(BRANCH_KEY, CoreNamespace.tBranch, createProperties()));
        Mockito.when(branchManager.getBranchProperties(Mockito.anyString(), Mockito.anySet())).thenReturn(null);
        Mockito.when(branchManager.getBranchProperties(Mockito.eq(BRANCH_KEY), Mockito.anySet())).thenReturn(
                BaseObjectMock.createBaseObject(BRANCH_KEY, CoreNamespace.tBranch, createReadProperties()));

        periodManager = Mockito.mock(IPeriodManager.class);
        Mockito.when(periodManager.createPeriod(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(
                BaseObjectMock.createBaseObject(BRANCH_PERIOD_SET.toString(), CoreNamespace.tPeriod, createReadProperties()));

        testee = new BranchBusinessComponent(periodManager, branchManager, classManager, yearManager);
    }

    private Map<String, Object> createProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(CoreNamespace.pBranchName, BRANCH_NAME);

        return properties;
    }

    private Map<String, Object> createReadProperties() {
        Map<String, Object> properties = createProperties();

        properties.put(CoreNamespace.pBranchPeriods, new DBSet(transaction, BRANCH_PERIOD_SET));

        return properties;
    }

    private Map<String, Object> createExpectedProperties() {
        Map<String, Object> properties = createProperties();

        properties.put(CoreNamespace.pBranchPeriods, new KSet(BRANCH_PERIOD_SET, new HashSet<>()));

        return properties;
    }

    private Map<String, Object> createClassProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(CoreNamespace.pClassName, CLASS_NAME);
        properties.put(CoreNamespace.pClassYear, YEAR_KEY);
        properties.put(CoreNamespace.pClassBranches, new DBSet(transaction, UUID.randomUUID().toString()));

        return properties;
    }

    private Map<String, Object> createYearProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(CoreNamespace.pYearName, YEAR_NAME);

        return properties;
    }

    @Test
    public void testCreateBranch() throws Exception {
        Map<String, Object> mockProperties = createProperties();
        BaseObject branch = testee.createBranch(CLASS_KEY, mockProperties);

        assertEquals(BRANCH_KEY, branch.getKey());
        Mockito.verify(branchManager).createBranch(Mockito.eq(BRANCH_NAME), Mockito.eq(CLASS_NAME), Mockito.eq(YEAR_NAME));
    }

    @Test
    public void testSetBranchProperties() throws Exception {
        Map<String, Object> mockProperties = createProperties();
        testee.setBranchProperties(BRANCH_KEY, mockProperties);

        Mockito.verify(branchManager).setBranchProperties(Mockito.eq(BRANCH_KEY), Mockito.eq(mockProperties));
    }

    @Test
    public void testGetBranchProperties() throws Exception {
        Map<String, Object> mockProperties = createExpectedProperties();
        BaseObject branchProperties = testee.getBranchProperties(BRANCH_KEY, mockProperties.keySet());

        Mockito.verify(branchManager).getBranchProperties(Mockito.eq(BRANCH_KEY), Mockito.eq(mockProperties.keySet()));

        assertMapEquals(mockProperties, branchProperties.getProperties());
    }
}
