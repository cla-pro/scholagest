package net.scholagest.old.managers.impl;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IExamManager;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.ExamObject;
import net.scholagest.old.objects.ObjectHelper;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class ExamManager extends ObjectManager implements IExamManager {
    @Inject
    public ExamManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public boolean checkWhetherExamExistsInPeriod(String examName, String yearName, String className, String branchName, String periodName) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String examsBasePropertyName = generateExamsBasePropertyName(examName, periodName, branchName, className, yearName);
        String examKey = (String) transaction.get(CoreNamespace.examsBase, examsBasePropertyName, null);

        return examKey != null;
    }

    @Override
    public ExamObject createExam(String examName, String classKey, String periodName, String branchName, String className, String yearName,
            Map<String, Object> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String examKey = CoreNamespace.examNs + "#" + UUID.randomUUID();

        ExamObject exam = new ExamObject(examKey);
        exam.putAllProperties(properties);

        DBSet gradeSet = DBSet.createDBSet(transaction, generatePeriodExamsKey(examKey));
        exam.setGrades(gradeSet);
        exam.setClassKey(classKey);

        persistObject(transaction, exam);

        String examsBasePropertyName = generateExamsBasePropertyName(examName, periodName, branchName, className, yearName);
        transaction.insert(CoreNamespace.examsBase, examsBasePropertyName, examKey, null);

        return exam;
    }

    private String generateExamsBasePropertyName(String examName, String periodName, String branchName, String className, String yearName) {
        return yearName + "/" + className + "/" + branchName + "/" + periodName + "/" + examName;
    }

    private String generatePeriodExamsKey(String examKey) {
        return examKey + "_exams";
    }

    @Override
    public void setExamProperties(String examKey, Map<String, Object> examProperties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        ExamObject examObject = new ExamObject(transaction, new ObjectHelper(getOntologyManager()), examKey);
        examObject.putAllProperties(examProperties);
    }

    @Override
    public ExamObject getExamProperties(String examKey, Set<String> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        // Lazy object
        return new ExamObject(transaction, new ObjectHelper(getOntologyManager()), examKey);
    }
}
