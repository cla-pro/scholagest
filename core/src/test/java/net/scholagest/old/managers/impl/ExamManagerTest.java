package net.scholagest.old.managers.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.old.managers.IExamManager;
import net.scholagest.old.managers.impl.ExamManager;
import net.scholagest.old.managers.ontology.OntologyManager;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.ExamObject;
import net.scholagest.utils.old.AbstractTestWithTransaction;
import net.scholagest.utils.old.DatabaseReaderWriter;
import net.scholagest.utils.old.InMemoryDatabase;
import net.scholagest.utils.old.InMemoryDatabase.InMemoryTransaction;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.mockito.Mockito;

public class ExamManagerTest extends AbstractTestWithTransaction {
    private static final String YEAR_NAME = "2012-2013";
    private static final String CLASS_NAME = "1P A";
    private static final String CLASS_KEY = "classKey";
    private static final String BRANCH_NAME = "Math";
    private static final String PERIOD_NAME = "Trimestre 1";
    private static final String EXAM_NAME = "Recitation 1";
    private static final String EXAM_KEY = CoreNamespace.examNs + "#83da81b7-47d7-441d-a682-3ef219983b14";
    private static final String EXAMS_BASE_PROPERTY_NAME = YEAR_NAME + "/" + CLASS_NAME + "/" + BRANCH_NAME + "/" + PERIOD_NAME + "/" + EXAM_NAME;

    private IExamManager examManager = new ExamManager(new OntologyManager());

    @Test
    public void testCheckWhetherExamExistsInPeriod() {
        fillTransactionWithDataSets(new String[] { "Exam" });

        assertTrue(examManager.checkWhetherExamExistsInPeriod(EXAM_NAME, YEAR_NAME, CLASS_NAME, BRANCH_NAME, PERIOD_NAME));
        assertFalse(examManager.checkWhetherExamExistsInPeriod("Recitation 2", YEAR_NAME, CLASS_NAME, BRANCH_NAME, PERIOD_NAME));
        assertFalse(examManager.checkWhetherExamExistsInPeriod(EXAM_NAME, "2011-2012", CLASS_NAME, BRANCH_NAME, PERIOD_NAME));
        assertFalse(examManager.checkWhetherExamExistsInPeriod(EXAM_NAME, YEAR_NAME, "2P B", BRANCH_NAME, PERIOD_NAME));
        assertFalse(examManager.checkWhetherExamExistsInPeriod(EXAM_NAME, YEAR_NAME, CLASS_NAME, "Histoire", PERIOD_NAME));
        assertFalse(examManager.checkWhetherExamExistsInPeriod(EXAM_NAME, YEAR_NAME, CLASS_NAME, BRANCH_NAME, "Trimestre 2"));
    }

    @Test
    public void testCreateNewExam() throws Exception {
        ExamObject exam = examManager
                .createExam(EXAM_NAME, CLASS_KEY, PERIOD_NAME, BRANCH_NAME, CLASS_NAME, YEAR_NAME, new HashMap<String, Object>());

        assertEquals(CoreNamespace.tExam, exam.getType());
        Mockito.verify(transaction).insert(eq(CoreNamespace.examsBase), eq(EXAMS_BASE_PROPERTY_NAME), anyString(), anyString());
    }

    @Test
    public void testSetAndGetExamProperties() throws Exception {
        fillTransactionWithDataSets(new String[] { "Exam" });

        Map<String, Object> properties = createExamProperties();
        examManager.setExamProperties(EXAM_KEY, properties);
        ExamObject exam = examManager.getExamProperties(EXAM_KEY, properties.keySet());

        assertEquals(CLASS_KEY, exam.getClassKey());
    }

    private Map<String, Object> createExamProperties() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put(CoreNamespace.pExamName, EXAM_NAME);
        personalProperties.put(CoreNamespace.pExamClass, CLASS_KEY);

        return personalProperties;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Exam");
        ScholagestThreadLocal.setTransaction(transaction);

        ExamManager examManager = new ExamManager(new OntologyManager());

        Map<String, Object> examProperties = new ExamManagerTest().createExamProperties();

        BaseObject exam = examManager
                .createExam(EXAM_NAME, CLASS_KEY, PERIOD_NAME, BRANCH_NAME, CLASS_NAME, YEAR_NAME, new HashMap<String, Object>());
        examManager.setExamProperties(exam.getKey(), examProperties);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
