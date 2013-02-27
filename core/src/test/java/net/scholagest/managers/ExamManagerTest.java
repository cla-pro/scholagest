package net.scholagest.managers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;

import org.junit.Test;
import org.mockito.Mockito;

public class ExamManagerTest extends AbstractTestWithTransaction {
    private static final String YEAR_NAME = "2012-2013";
    private static final String CLASS_NAME = "1P A";
    private static final String BRANCH_NAME = "Math";
    private static final String PERIOD_NAME = "Trimestre 1";
    private static final String EXAM_NAME = "Recitation 1";
    private static final String EXAM_KEY = CoreNamespace.examNs + "/" + YEAR_NAME + "/" + CLASS_NAME + "/" + BRANCH_NAME + "/" + PERIOD_NAME + "#"
            + EXAM_NAME;

    private IExamManager examManager = spy(new ExamManager(new OntologyManager()));

    @Test
    public void testCreateNewExam() throws Exception {
        BaseObject exam = examManager.createExam(requestId, transaction, EXAM_NAME, PERIOD_NAME, BRANCH_NAME, CLASS_NAME, YEAR_NAME);

        assertEquals(EXAM_KEY, exam.getKey());
        Mockito.verify(transaction).insert(EXAM_KEY, RDF.type, CoreNamespace.tExam, null);
    }

    @Test
    public void testSetAndGetExamProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Exam" });

        Map<String, Object> properties = createExamProperties();
        examManager.setExamProperties(requestId, transaction, EXAM_KEY, properties);
        BaseObject exam = examManager.getExamProperties(requestId, transaction, EXAM_KEY, properties.keySet());

        Map<String, Object> readProperties = exam.getProperties();
        assertEquals(properties.size(), readProperties.size());
        for (String key : properties.keySet()) {
            assertEquals(properties.get(key), readProperties.get(key));
        }
    }

    private Map<String, Object> createExamProperties() {
        Map<String, Object> personalProperties = new HashMap<String, Object>();

        personalProperties.put(CoreNamespace.pExamName, EXAM_NAME);

        return personalProperties;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Exam");

        ExamManager examManager = new ExamManager(new OntologyManager());

        Map<String, Object> examProperties = new ExamManagerTest().createExamProperties();

        BaseObject exam = examManager.createExam(UUID.randomUUID().toString(), transaction, EXAM_NAME, PERIOD_NAME, BRANCH_NAME, CLASS_NAME,
                YEAR_NAME);
        examManager.setExamProperties(UUID.randomUUID().toString(), transaction, exam.getKey(), examProperties);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }
}
