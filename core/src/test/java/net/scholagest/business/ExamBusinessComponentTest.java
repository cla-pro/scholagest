package net.scholagest.business;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BaseObjectMock;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ExamBusinessComponentTest extends AbstractTestWithTransaction {
    private static final String EXAM_NAME = "Récitation 1";
    private static final String PERIOD_NAME = "Trimestre 1";
    private static final String BRANCH_NAME = "Math";
    private static final String CLASS_NAME = "1P A";
    private static final String YEAR_NAME = "2012-2013";

    private static final String EXAM_KEY = CoreNamespace.examNs + "/" + YEAR_NAME + "/" + CLASS_NAME + "/" + BRANCH_NAME + "/" + PERIOD_NAME + "#"
            + EXAM_NAME;
    private static final String PERIOD_KEY = "periodKey";
    private static final String BRANCH_KEY = "branchKey";
    private static final String CLASS_KEY = "classKey";
    private static final String YEAR_KEY = "yearKey";

    private IExamManager examManager;
    private IPeriodManager periodManager;
    private IBranchManager branchManager;
    private IClassManager classManager;
    private IYearManager yearManager;

    private IExamBusinessComponent testee;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        examManager = Mockito.mock(IExamManager.class);
        Mockito.when(examManager.createExam(requestId, transaction, EXAM_NAME, PERIOD_NAME, BRANCH_NAME, CLASS_NAME, YEAR_NAME)).thenReturn(
                BaseObjectMock.createBaseObject(EXAM_KEY, CoreNamespace.tExam, new HashMap<String, Object>()));
        Mockito.when(examManager.getExamProperties(Mockito.eq(requestId), Mockito.eq(transaction), Mockito.eq(EXAM_KEY), Mockito.anySet()))
                .thenReturn(BaseObjectMock.createBaseObject(EXAM_KEY, CoreNamespace.tExam, createProperties()));

        periodManager = Mockito.mock(IPeriodManager.class);
        Mockito.when(periodManager.getPeriodProperties(Mockito.eq(requestId), Mockito.eq(transaction), Mockito.eq(PERIOD_KEY), Mockito.anySet()))
                .thenReturn(
                        BaseObjectMock.createBaseObject(PERIOD_KEY, CoreNamespace.tPeriod,
                                createMap(CoreNamespace.pPeriodName, PERIOD_NAME, CoreNamespace.pPeriodExams, new DBSet(transaction, null))));

        branchManager = Mockito.mock(IBranchManager.class);
        Mockito.when(branchManager.getBranchProperties(Mockito.eq(requestId), Mockito.eq(transaction), Mockito.eq(BRANCH_KEY), Mockito.anySet()))
                .thenReturn(BaseObjectMock.createBaseObject(BRANCH_KEY, CoreNamespace.tBranch, createMap(CoreNamespace.pBranchName, BRANCH_NAME)));

        classManager = Mockito.mock(IClassManager.class);
        Mockito.when(classManager.getClassProperties(Mockito.eq(requestId), Mockito.eq(transaction), Mockito.eq(CLASS_KEY), Mockito.anySet()))
                .thenReturn(BaseObjectMock.createBaseObject(CLASS_KEY, CoreNamespace.tClass, createMap(CoreNamespace.pClassName, CLASS_NAME)));

        yearManager = Mockito.mock(IYearManager.class);
        Mockito.when(yearManager.getYearProperties(Mockito.eq(requestId), Mockito.eq(transaction), Mockito.eq(YEAR_KEY), Mockito.anySet()))
                .thenReturn(BaseObjectMock.createBaseObject(YEAR_KEY, CoreNamespace.tYear, createMap(CoreNamespace.pYearName, YEAR_NAME)));

        testee = new ExamBusinessComponent(examManager, periodManager, branchManager, classManager, yearManager);
    }

    @Test
    public void testCreateExam() throws Exception {
        BaseObject exam = testee.createExam(requestId, transaction, YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY, createProperties());

        Mockito.verify(examManager).createExam(requestId, transaction, EXAM_NAME, PERIOD_NAME, BRANCH_NAME, CLASS_NAME, YEAR_NAME);

        assertEquals(EXAM_KEY, exam.getKey());
        assertEquals(CoreNamespace.tExam, exam.getType());
        assertMapEquals(new HashMap<String, Object>(), exam.getProperties());
    }

    @Test
    public void testGetExamProperties() throws Exception {
        Map<String, Object> mockProperties = createProperties();
        BaseObject exam = testee.getExamProperties(requestId, transaction, EXAM_KEY, mockProperties.keySet());

        assertEquals(EXAM_KEY, exam.getKey());
        assertEquals(CoreNamespace.tExam, exam.getType());
        assertMapEquals(mockProperties, exam.getProperties());
    }

    private Map<String, Object> createProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pExamName, EXAM_NAME);
        return properties;
    }

    private Map<String, Object> createMap(Object... keyValues) {
        Map<String, Object> properties = new HashMap<>();

        for (int i = 0; i < keyValues.length; i += 2) {
            properties.put((String) keyValues[i], keyValues[i + 1]);
        }

        return properties;
    }
}
