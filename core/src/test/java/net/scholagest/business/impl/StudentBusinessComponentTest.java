package net.scholagest.business.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.exception.ScholagestException;
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
import net.scholagest.objects.BranchObject;
import net.scholagest.objects.BranchType;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StudentBusinessComponentTest extends AbstractTestWithTransaction {
    private static final String STUDENT_KEY = "http://scholagest.net/student#329c1a51-5469-4eac-a182-29afb1e5ecdb";

    private static final String YEAR_KEY = "yearKey";
    private static final String CLASS_KEY = "classKey";
    private static final String BRANCH_KEY = "branchKey";
    private static final String PERIOD_KEY = "periodKey";

    @Mock
    private IStudentManager studentManager;

    @Mock
    private IYearManager yearManager;

    @Mock
    private IClassManager classManager;

    @Mock
    private IBranchManager branchManager;

    @Mock
    private IPeriodManager periodManager;

    @Mock
    private IExamManager examManager;

    private IStudentBusinessComponent testee;

    @Before
    public void setup() throws Exception {
        when(studentManager.createStudent()).thenReturn(BaseObjectMock.createStudentObject(STUDENT_KEY, new HashMap<String, Object>()));
        when(studentManager.getMedicalProperties(STUDENT_KEY, createStudentMedicalProperties().keySet())).thenReturn(
                BaseObjectMock.createBaseObject(null, null, createStudentMedicalProperties()));
        when(studentManager.getPersonalProperties(STUDENT_KEY, createStudentPersonalProperties().keySet())).thenReturn(
                BaseObjectMock.createBaseObject(null, null, createStudentPersonalProperties()));
        when(studentManager.getStudents()).thenReturn(
                new HashSet<>(Arrays.asList(BaseObjectMock.createStudentObject(STUDENT_KEY, new HashMap<String, Object>()))));

        when(yearManager.getYearProperties(anyString(), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createBaseObject(UUID.randomUUID().toString(), CoreNamespace.tYear, new HashMap<String, Object>()));

        HashMap<String, Object> periodProperties = new HashMap<String, Object>();
        periodProperties.put(CoreNamespace.pPeriodExams, new DBSet(transaction, UUID.randomUUID().toString()));
        when(periodManager.getPeriodProperties(anyString(), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createPeriodObject(UUID.randomUUID().toString(), periodProperties));

        when(classManager.getClassProperties(anyString(), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createClassObject(UUID.randomUUID().toString(), new HashMap<String, Object>()));

        HashMap<String, Object> branchProperties = new HashMap<String, Object>();
        branchProperties.put(CoreNamespace.pBranchPeriods, new DBSet(transaction, UUID.randomUUID().toString()));
        when(branchManager.getBranchProperties(anyString(), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createBranchObject(UUID.randomUUID().toString(), branchProperties));

        when(examManager.getExamProperties(anyString(), anySetOf(String.class))).thenReturn(
                BaseObjectMock.createExamObject(UUID.randomUUID().toString(), new HashMap<String, Object>()));

        testee = new StudentBusinessComponent(studentManager, yearManager, classManager, branchManager, periodManager, examManager);
    }

    private Map<String, Object> createStudentPersonalProperties() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put("pStudentLastName", "Dupont");
        personalProperties.put("pStudentFirstName", "Gilles");

        return personalProperties;
    }

    private Map<String, Object> createStudentMedicalProperties() {
        Map<String, Object> medicalProperties = new HashMap<String, Object>();

        medicalProperties.put("pStudentAlergy", "RAS");

        return medicalProperties;
    }

    @Test
    public void testCreateStudent() throws Exception {
        Map<String, Object> properties = createStudentPersonalProperties();
        BaseObject studentKey = testee.createStudent(properties);

        assertEquals(STUDENT_KEY, studentKey.getKey());
        verify(studentManager).createStudent();
    }

    @Test
    public void testUpdateStudentProperties() throws Exception {
        Map<String, Object> personalProperties = createStudentPersonalProperties();
        Map<String, Object> medicalProperties = createStudentPersonalProperties();
        testee.updateStudentProperties(STUDENT_KEY, personalProperties, medicalProperties);

        verify(studentManager).setMedicalProperties(STUDENT_KEY, medicalProperties);
        verify(studentManager).setPersonalProperties(STUDENT_KEY, personalProperties);
    }

    @Test
    public void testGetStudentPersonalProperties() throws Exception {
        Map<String, Object> mockProperties = createStudentPersonalProperties();
        BaseObject personalProperties = testee.getStudentPersonalProperties(STUDENT_KEY, mockProperties.keySet());

        verify(studentManager).getPersonalProperties(STUDENT_KEY, mockProperties.keySet());

        assertMapEquals(mockProperties, personalProperties.getProperties());
    }

    @Test
    public void testGetStudentMedicalProperties() throws Exception {
        Map<String, Object> mockProperties = createStudentMedicalProperties();
        BaseObject medicalProperties = testee.getStudentMedicalProperties(STUDENT_KEY, mockProperties.keySet());

        verify(studentManager).getMedicalProperties(STUDENT_KEY, mockProperties.keySet());

        assertMapEquals(mockProperties, medicalProperties.getProperties());
    }

    @Test
    public void testGetStudentsWithProperties() throws Exception {
        Map<String, Object> personalProperties = createStudentPersonalProperties();
        Set<BaseObject> studentsWithProperties = testee.getStudentsWithProperties(personalProperties.keySet());

        Map<String, Object> studentProperties = studentsWithProperties.iterator().next().getProperties();
        assertEquals(1, studentsWithProperties.size());
        assertMapEquals(personalProperties, studentProperties);
    }

    @Test
    public void testSetStudentGradesInNumericalBranch() throws Exception {
        BranchObject branchObject = mock(BranchObject.class);
        when(branchObject.getBranchType()).thenReturn(BranchType.NUMERICAL);

        Map<String, BaseObject> studentGrades = createNumericalGrades();
        testee.setStudentGrades(STUDENT_KEY, studentGrades, YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY);

        verify(studentManager).persistStudentGrade(eq(STUDENT_KEY), eq(YEAR_KEY), eq(studentGrades.keySet().iterator().next()),
                argThat(new BaseMatcher<BaseObject>() {
                    @Override
                    public boolean matches(Object item) {
                        return ((BaseObject) item).getType().equals(CoreNamespace.tGrade);
                    }

                    @Override
                    public void describeTo(Description description) {}
                }));
    }

    @Test
    public void testSetStudentGradesNullInNumericalBranch() throws Exception {
        BranchObject branchObject = mock(BranchObject.class);
        when(branchObject.getBranchType()).thenReturn(BranchType.NUMERICAL);

        Map<String, BaseObject> studentGrades = createNullGrades();
        testee.setStudentGrades(STUDENT_KEY, studentGrades, YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY);

        verify(studentManager).persistStudentGrade(eq(STUDENT_KEY), eq(YEAR_KEY), eq(studentGrades.keySet().iterator().next()),
                argThat(new BaseMatcher<BaseObject>() {
                    @Override
                    public boolean matches(Object item) {
                        return ((BaseObject) item).getType().equals(CoreNamespace.tGrade);
                    }

                    @Override
                    public void describeTo(Description description) {}
                }));
    }

    @Test(expected = ScholagestException.class)
    public void testSetStudentNotNumericalGradesInNumericalBranch() throws Exception {
        BranchObject branchObject = mock(BranchObject.class);
        when(branchObject.getBranchType()).thenReturn(BranchType.NUMERICAL);
        Mockito.when(branchManager.getBranchProperties(eq(BRANCH_KEY), anySetOf(String.class))).thenReturn(branchObject);

        Map<String, BaseObject> studentGrades = createAlphaNumericalGrades();
        testee.setStudentGrades(STUDENT_KEY, studentGrades, YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY);
        fail("Exception expected");
    }

    @Test
    public void testSetStudentGradesInAlphaNumericalBranch() throws Exception {
        BranchObject branchObject = mock(BranchObject.class);
        when(branchObject.getBranchType()).thenReturn(BranchType.ALPHA_NUMERICAL);
        Mockito.when(branchManager.getBranchProperties(eq(BRANCH_KEY), anySetOf(String.class))).thenReturn(branchObject);

        Map<String, BaseObject> studentGrades = createAlphaNumericalGrades();
        testee.setStudentGrades(STUDENT_KEY, studentGrades, YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY);

        verify(studentManager).persistStudentGrade(eq(STUDENT_KEY), eq(YEAR_KEY), eq(studentGrades.keySet().iterator().next()),
                argThat(new BaseMatcher<BaseObject>() {
                    @Override
                    public boolean matches(Object item) {
                        return ((BaseObject) item).getType().equals(CoreNamespace.tGrade);
                    }

                    @Override
                    public void describeTo(Description description) {}
                }));
    }

    @Test
    public void testSetStudentGradesNullInAlphaNumericalBranch() throws Exception {
        BranchObject branchObject = mock(BranchObject.class);
        when(branchObject.getBranchType()).thenReturn(BranchType.ALPHA_NUMERICAL);
        Mockito.when(branchManager.getBranchProperties(eq(BRANCH_KEY), anySetOf(String.class))).thenReturn(branchObject);

        Map<String, BaseObject> studentGrades = createNullGrades();
        testee.setStudentGrades(STUDENT_KEY, studentGrades, YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY);

        verify(studentManager).persistStudentGrade(eq(STUDENT_KEY), eq(YEAR_KEY), eq(studentGrades.keySet().iterator().next()),
                argThat(new BaseMatcher<BaseObject>() {
                    @Override
                    public boolean matches(Object item) {
                        return ((BaseObject) item).getType().equals(CoreNamespace.tGrade);
                    }

                    @Override
                    public void describeTo(Description description) {}
                }));
    }

    private Map<String, BaseObject> createNumericalGrades() {
        return createGrades("5.5");
    }

    private Map<String, BaseObject> createAlphaNumericalGrades() {
        return createGrades("Bien");
    }

    private Map<String, BaseObject> createNullGrades() {
        return createGrades(null);
    }

    private Map<String, BaseObject> createGrades(String gradeValue) {
        Map<String, BaseObject> grades = new HashMap<>();

        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pGradeValue, gradeValue);
        grades.put(UUID.randomUUID().toString(), BaseObjectMock.createBaseObject(UUID.randomUUID().toString(), CoreNamespace.tGrade, properties));

        return grades;
    }
}
