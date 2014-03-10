package net.scholagest.old.business.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.old.business.IExamBusinessComponent;
import net.scholagest.old.business.impl.ExamBusinessComponent;
import net.scholagest.old.managers.IBranchManager;
import net.scholagest.old.managers.IClassManager;
import net.scholagest.old.managers.IExamManager;
import net.scholagest.old.managers.IPeriodManager;
import net.scholagest.old.managers.IYearManager;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.BaseObjectMock;
import net.scholagest.old.objects.ExamObject;
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

    @Before
    public void setup() throws Exception {
        examManager = Mockito.mock(IExamManager.class);
        when(
                examManager.createExam(eq(EXAM_NAME), eq(CLASS_KEY), eq(PERIOD_NAME), eq(BRANCH_NAME), eq(CLASS_NAME), eq(YEAR_NAME),
                        anyMapOf(String.class, Object.class))).thenReturn(BaseObjectMock.createExamObject(EXAM_KEY, new HashMap<String, Object>()));
        when(examManager.getExamProperties(eq(EXAM_KEY), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createExamObject(EXAM_KEY, createProperties()));

        periodManager = Mockito.mock(IPeriodManager.class);
        when(periodManager.getPeriodProperties(eq(PERIOD_KEY), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createPeriodObject(PERIOD_KEY,
                        createMap(CoreNamespace.pPeriodName, PERIOD_NAME, CoreNamespace.pPeriodExams, new DBSet(transaction, null))));

        branchManager = Mockito.mock(IBranchManager.class);
        when(branchManager.getBranchProperties(eq(BRANCH_KEY), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createBranchObject(BRANCH_KEY, createMap(CoreNamespace.pBranchName, BRANCH_NAME)));

        classManager = Mockito.mock(IClassManager.class);
        when(classManager.getClassProperties(eq(CLASS_KEY), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createClassObject(CLASS_KEY, createMap(CoreNamespace.pClassName, CLASS_NAME)));

        yearManager = Mockito.mock(IYearManager.class);
        when(yearManager.getYearProperties(eq(YEAR_KEY), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createBaseObject(YEAR_KEY, CoreNamespace.tYear, createMap(CoreNamespace.pYearName, YEAR_NAME)));

        testee = new ExamBusinessComponent(examManager, periodManager, branchManager, classManager, yearManager);
    }

    @Test
    public void testCreateExamNameAlreadyUsed() throws Exception {
        when(examManager.checkWhetherExamExistsInPeriod(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        try {
            testee.createExam(YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY, createProperties());
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.OBJECT_ALREADY_EXISTS, e.getErrorCode());
        }
    }

    @Test
    public void testCreateExam() throws Exception {
        ExamObject exam = testee.createExam(YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY, createProperties());

        verify(examManager).createExam(eq(EXAM_NAME), eq(CLASS_KEY), eq(PERIOD_NAME), eq(BRANCH_NAME), eq(CLASS_NAME), eq(YEAR_NAME),
                anyMapOf(String.class, Object.class));

        assertEquals(EXAM_KEY, exam.getKey());
        assertEquals(CoreNamespace.tExam, exam.getType());
        assertMapEquals(new HashMap<String, Object>(), exam.getProperties());
    }

    @Test
    public void testGetExamProperties() throws Exception {
        Map<String, Object> mockProperties = createProperties();
        BaseObject exam = testee.getExamProperties(EXAM_KEY, mockProperties.keySet());

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
