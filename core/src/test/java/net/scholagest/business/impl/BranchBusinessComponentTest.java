package net.scholagest.business.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IStudentManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BaseObjectMock;
import net.scholagest.objects.BranchType;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BranchBusinessComponentTest extends AbstractTestWithTransaction {
    private static final String MEAN_NAME = "mean";
    private static final String BRANCH_PERIOD_SET = UUID.randomUUID().toString();
    private static final String BRANCH_NAME = "Math";
    private static final String CLASS_NAME = "1P A";
    private static final String YEAR_NAME = "2012-2013";
    private static final String BRANCH_KEY = CoreNamespace.branchNs + "/" + YEAR_NAME + "/" + CLASS_NAME + "#" + BRANCH_NAME;
    private static final String CLASS_KEY = CoreNamespace.classNs + "/" + YEAR_NAME + "#" + CLASS_NAME;
    private static final String YEAR_KEY = CoreNamespace.yearNs + "#" + YEAR_NAME;

    private IExamManager examManager;
    private IPeriodManager periodManager;
    private IBranchManager branchManager;
    private IClassManager classManager;
    private IYearManager yearManager;
    private IStudentManager studentManager;

    private IBranchBusinessComponent testee;

    @Before
    public void setup() throws Exception {
        yearManager = mock(IYearManager.class);
        when(yearManager.getYearProperties(anyString(), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createBaseObject(YEAR_KEY, CoreNamespace.tYear, createYearProperties()));

        classManager = mock(IClassManager.class);
        when(classManager.getClassProperties(eq(CLASS_KEY), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createClassObject(CLASS_KEY, createClassProperties()));

        branchManager = mock(IBranchManager.class);
        when(branchManager.createBranch(eq(BRANCH_NAME), eq(CLASS_KEY), eq(CLASS_NAME), eq(YEAR_NAME), anyMapOf(String.class, Object.class)))
                .thenReturn(BaseObjectMock.createBranchObject(BRANCH_KEY, createProperties()));
        when(branchManager.getBranchProperties(eq(BRANCH_KEY), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createBranchObject(BRANCH_KEY, createReadProperties()));

        periodManager = mock(IPeriodManager.class);
        when(periodManager.createPeriod(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(
                BaseObjectMock.createPeriodObject(BRANCH_PERIOD_SET.toString(), createReadProperties()));

        examManager = mock(IExamManager.class);
        when(
                examManager.createExam(eq(MEAN_NAME), eq(CLASS_KEY), anyString(), eq(BRANCH_NAME), eq(CLASS_NAME), eq(YEAR_NAME),
                        anyMapOf(String.class, Object.class))).thenReturn(
                BaseObjectMock.createExamObject(UUID.randomUUID().toString(), new HashMap<String, Object>()));

        studentManager = mock(IStudentManager.class);

        testee = new BranchBusinessComponent(examManager, periodManager, branchManager, classManager, yearManager, studentManager);
    }

    private Map<String, Object> createProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(CoreNamespace.pBranchName, BRANCH_NAME);
        properties.put(CoreNamespace.pBranchType, BranchType.NUMERICAL.name());
        properties.put(CoreNamespace.pBranchPeriods, new DBSet(transaction, UUID.randomUUID().toString()));

        return properties;
    }

    private Map<String, Object> createReadProperties() {
        Map<String, Object> properties = createProperties();

        properties.put(CoreNamespace.pBranchType, BranchType.NUMERICAL.name());
        properties.put(CoreNamespace.pBranchType, BranchType.NUMERICAL.name());
        properties.put(CoreNamespace.pBranchPeriods, new DBSet(transaction, BRANCH_PERIOD_SET));

        return properties;
    }

    private Map<String, Object> createExpectedProperties() {
        Map<String, Object> properties = createProperties();

        properties.put(CoreNamespace.pBranchPeriods, new DBSet(transaction, BRANCH_PERIOD_SET));

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
        verify(branchManager).createBranch(eq(BRANCH_NAME), eq(CLASS_KEY), eq(CLASS_NAME), eq(YEAR_NAME), anyMapOf(String.class, Object.class));

        verify(periodManager, Mockito.times(3)).createPeriod(anyString(), eq(CLASS_KEY), eq(BRANCH_NAME), eq(CLASS_NAME), eq(YEAR_NAME));
        verify(classManager).getClassProperties(CLASS_KEY, new HashSet<String>(Arrays.asList(CoreNamespace.pClassBranches)));
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
