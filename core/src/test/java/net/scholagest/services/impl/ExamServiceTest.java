package net.scholagest.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import net.scholagest.business.IExamBusinessComponent;
import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObjectMock;
import net.scholagest.objects.ExamObject;
import net.scholagest.services.IExamService;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ExamServiceTest extends AbstractTest {
    private static final String YEAR_KEY = "yearKey";
    private static final String CLASS_KEY = "classKey";
    private static final String BRANCH_KEY = "branchKey";
    private static final String PERIOD_KEY = "periodKey";

    private InMemoryDatabase database;
    private IExamBusinessComponent examBusinessComponent;
    private IOntologyBusinessComponent ontologyBusinessComponent;

    private IExamService testee;

    @Before
    public void setUpTest() {
        database = Mockito.spy(new InMemoryDatabase());
        defineAdminSubject();

        examBusinessComponent = Mockito.mock(IExamBusinessComponent.class);
        ontologyBusinessComponent = Mockito.mock(IOntologyBusinessComponent.class);

        testee = new ExamService(database, examBusinessComponent, ontologyBusinessComponent);
    }

    @Test
    public void testCreateExam() throws Exception {
        testee.createExam(YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY, new HashMap<String, Object>());

        Mockito.verify(examBusinessComponent).createExam(Mockito.eq(YEAR_KEY), Mockito.eq(CLASS_KEY), Mockito.eq(BRANCH_KEY), Mockito.eq(PERIOD_KEY),
                Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testCreateBranchInsufficientPrivileges() throws Exception {
        defineClassTeacherSubject();
        testee.createExam(YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY, new HashMap<String, Object>());
        Mockito.verify(examBusinessComponent).createExam(Mockito.eq(YEAR_KEY), Mockito.eq(CLASS_KEY), Mockito.eq(BRANCH_KEY), Mockito.eq(PERIOD_KEY),
                Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());

        try {
            defineClassHelpTeacherSubject();
            testee.createExam(YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.createExam(YEAR_KEY, CLASS_KEY, BRANCH_KEY, PERIOD_KEY, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testGetExamPropertiesNoClass() throws Exception {
        String examKey = "examKey";
        assertNull(testee.getExamProperties(examKey, new HashSet<String>()));

        Mockito.verify(examBusinessComponent).getExamProperties(Mockito.eq(examKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetExamPropertiesWithClass() throws Exception {
        String examKey = "examKey";
        Mockito.when(examBusinessComponent.getExamProperties(examKey, new HashSet<String>(Arrays.asList(CoreNamespace.pExamClass)))).thenReturn(
                createClassKeyObject());

        testee.getExamProperties(examKey, new HashSet<String>());

        Mockito.verify(examBusinessComponent, Mockito.times(2)).getExamProperties(Mockito.eq(examKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetBranchPropertiesOtherUsers() throws Exception {
        String examKey = "examKey";
        Mockito.when(examBusinessComponent.getExamProperties(examKey, new HashSet<String>(Arrays.asList(CoreNamespace.pExamClass)))).thenReturn(
                createClassKeyObject());

        defineClassTeacherSubject();
        testee.getExamProperties(examKey, new HashSet<String>());
        Mockito.verify(examBusinessComponent, Mockito.times(2)).getExamProperties(Mockito.eq(examKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());

        try {
            defineClassHelpTeacherSubject();
            testee.getExamProperties(examKey, new HashSet<String>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.getExamProperties(examKey, new HashSet<String>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    private ExamObject createClassKeyObject() {
        ExamObject classKeyObject = BaseObjectMock.createExamObject(null, new HashMap<String, Object>());

        classKeyObject.putProperty(CoreNamespace.pExamClass, CLASS_KEY);

        return classKeyObject;
    }
}
