package net.scholagest.managers.impl;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class ExamManager extends ObjectManager implements IExamManager {
    @Inject
    public ExamManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createExam(String requestId, ITransaction transaction, String examName, String periodName, String branchName, String className,
            String yearName) throws Exception {
        String examKey = CoreNamespace.examNs + "/" + yearName + "/" + className + "/" + branchName + "/" + periodName + "#" + examName;

        BaseObject exam = super.createObject(requestId, transaction, examKey, CoreNamespace.tExam);

        String gradeSetKey = generatePeriodExamsKey(examKey);
        DBSet.createDBSet(transaction, gradeSetKey);
        transaction.insert(examKey, CoreNamespace.pPeriodExams, gradeSetKey, null);

        return exam;
    }

    private String generatePeriodExamsKey(String examKey) {
        return examKey + "_exams";
    }

    @Override
    public void setExamProperties(String requestId, ITransaction transaction, String examKey, Map<String, Object> examProperties) throws Exception {
        super.setObjectProperties(requestId, transaction, examKey, examProperties);
    }

    @Override
    public BaseObject getExamProperties(String requestId, ITransaction transaction, String examKey, Set<String> properties) throws Exception {
        BaseObject branch = new BaseObject(examKey, CoreNamespace.tBranch);
        branch.setProperties(super.getObjectProperties(requestId, transaction, examKey, properties));

        return branch;
    }
}
