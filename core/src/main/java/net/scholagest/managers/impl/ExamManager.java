package net.scholagest.managers.impl;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.ExamObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class ExamManager extends ObjectManager implements IExamManager {
    @Inject
    public ExamManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public ExamObject createExam(String examName, String classKey, String periodName, String branchName, String className, String yearName)
            throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String examKey = CoreNamespace.examNs + "/" + yearName + "/" + className + "/" + branchName + "/" + periodName + "#" + examName;

        ExamObject exam = new ExamObject(examKey);

        DBSet gradeSet = DBSet.createDBSet(transaction, generatePeriodExamsKey(examKey));
        exam.setGrades(gradeSet);
        exam.setClassKey(classKey);

        persistObject(transaction, exam);

        return exam;
    }

    private String generatePeriodExamsKey(String examKey) {
        return examKey + "_exams";
    }

    @Override
    public void setExamProperties(String examKey, Map<String, Object> examProperties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        setObjectProperties(transaction, examKey, examProperties);
    }

    @Override
    public ExamObject getExamProperties(String examKey, Set<String> properties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        ExamObject exam = new ExamObject(examKey);
        exam.setProperties(getObjectProperties(transaction, examKey, properties));

        return exam;
    }
}
