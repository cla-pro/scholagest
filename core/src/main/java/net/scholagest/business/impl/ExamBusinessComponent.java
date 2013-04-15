package net.scholagest.business.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IExamBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.impl.CoreNamespace;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class ExamBusinessComponent implements IExamBusinessComponent {
    private IExamManager examManager;
    private IPeriodManager periodManager;
    private IBranchManager branchManager;
    private IClassManager classManager;
    private IYearManager yearManager;

    @Inject
    public ExamBusinessComponent(IExamManager examManager, IPeriodManager periodManager, IBranchManager branchManager, IClassManager classManager,
            IYearManager yearManager) {
        this.examManager = examManager;
        this.periodManager = periodManager;
        this.branchManager = branchManager;
        this.classManager = classManager;
        this.yearManager = yearManager;
    }

    @Override
    public BaseObject createExam(String requestId, ITransaction transaction, String yearKey, String classKey, String branchKey, String periodKey,
            Map<String, Object> examProperties) throws Exception {
        String yearName = getYearName(requestId, transaction, yearKey);
        String className = getClassName(requestId, transaction, classKey);
        String branchName = getBranchName(requestId, transaction, branchKey);
        String periodName = getPeriodName(requestId, transaction, periodKey);

        BaseObject exam = examManager.createExam(requestId, transaction, (String) examProperties.get(CoreNamespace.pExamName), periodName, branchName,
                className, yearName);
        examManager.setExamProperties(requestId, transaction, exam.getKey(), examProperties);

        DBSet periodExams = (DBSet) periodManager.getPeriodProperties(requestId, transaction, periodKey,
                new HashSet<>(Arrays.asList(CoreNamespace.pPeriodExams))).getProperty(CoreNamespace.pPeriodExams);
        periodExams.add(exam.getKey());

        return exam;
    }

    private String getYearName(String requestId, ITransaction transaction, String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pYearName);
        return (String) yearManager.getYearProperties(requestId, transaction, yearKey, properties).getProperty(CoreNamespace.pYearName);
    }

    private String getClassName(String requestId, ITransaction transaction, String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pClassName);
        return (String) classManager.getClassProperties(requestId, transaction, yearKey, properties).getProperty(CoreNamespace.pClassName);
    }

    private String getBranchName(String requestId, ITransaction transaction, String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pBranchName);
        return (String) branchManager.getBranchProperties(requestId, transaction, yearKey, properties).getProperty(CoreNamespace.pBranchName);
    }

    private String getPeriodName(String requestId, ITransaction transaction, String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pPeriodName);
        return (String) periodManager.getPeriodProperties(requestId, transaction, yearKey, properties).getProperty(CoreNamespace.pPeriodName);
    }

    @Override
    public BaseObject getExamProperties(String requestId, ITransaction transaction, String examKey, Set<String> propertiesName) throws Exception {
        return examManager.getExamProperties(requestId, transaction, examKey, propertiesName);
    }
}
