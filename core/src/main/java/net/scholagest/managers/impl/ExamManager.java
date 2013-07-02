package net.scholagest.managers.impl;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class ExamManager extends ObjectManager implements IExamManager {
    @Inject
    public ExamManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createExam(String examName, String periodName, String branchName, String className, String yearName) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String examKey = CoreNamespace.examNs + "/" + yearName + "/" + className + "/" + branchName + "/" + periodName + "#" + examName;

        BaseObject exam = super.createObject(transaction, examKey, CoreNamespace.tExam);

        String gradeSetKey = generatePeriodExamsKey(examKey);
        DBSet.createDBSet(transaction, gradeSetKey);
        transaction.insert(examKey, CoreNamespace.pPeriodExams, gradeSetKey, null);

        return exam;
    }

    private String generatePeriodExamsKey(String examKey) {
        return examKey + "_exams";
    }

    @Override
    public void setExamProperties(String examKey, Map<String, Object> examProperties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        super.setObjectProperties(transaction, examKey, examProperties);
    }

    @Override
    public BaseObject getExamProperties(String examKey, Set<String> properties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        BaseObject branch = new BaseObject(examKey, CoreNamespace.tBranch);
        branch.setProperties(super.getObjectProperties(transaction, examKey, properties));

        return branch;
    }
}
