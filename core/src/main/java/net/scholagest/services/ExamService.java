package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.business.IExamBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class ExamService implements IExamService {
    private final IDatabase database;
    private final IExamBusinessComponent examBusinessComponent;

    @Inject
    public ExamService(IDatabase database, IExamBusinessComponent examBusinessComponent) {
        this.database = database;
        this.examBusinessComponent = examBusinessComponent;
    }

    @Override
    public BaseObject createExam(String requestId, String yearKey, String classKey, String branchKey, String periodKey, Map<String, Object> examInfo)
            throws Exception {
        BaseObject exam = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            exam = examBusinessComponent.createExam(requestId, transaction, yearKey, classKey, branchKey, periodKey, examInfo);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return exam;
    }

    @Override
    public BaseObject getExamProperties(String requestId, String examKey, Set<String> propertiesName) throws Exception {
        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            BaseObject properties = examBusinessComponent.getExamProperties(requestId, transaction, examKey, propertiesName);
            transaction.commit();
            return properties;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return null;
    }
}
