package net.scholagest.business.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IExamBusinessComponent;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.ExamObject;

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
    public ExamObject createExam(String yearKey, String classKey, String branchKey, String periodKey, Map<String, Object> examProperties)
            throws Exception {
        String yearName = getYearName(yearKey);
        String className = getClassName(classKey);
        String branchName = getBranchName(branchKey);
        String periodName = getPeriodName(periodKey);

        ExamObject exam = examManager.createExam((String) examProperties.get(CoreNamespace.pExamName), classKey, periodName, branchName, className,
                yearName);

        examManager.setExamProperties(exam.getKey(), examProperties);

        DBSet periodExams = (DBSet) periodManager.getPeriodProperties(periodKey, new HashSet<>(Arrays.asList(CoreNamespace.pPeriodExams)))
                .getProperty(CoreNamespace.pPeriodExams);
        periodExams.add(exam.getKey());

        return exam;
    }

    private String getYearName(String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pYearName);
        return (String) yearManager.getYearProperties(yearKey, properties).getProperty(CoreNamespace.pYearName);
    }

    private String getClassName(String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pClassName);
        return (String) classManager.getClassProperties(yearKey, properties).getProperty(CoreNamespace.pClassName);
    }

    private String getBranchName(String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pBranchName);
        return (String) branchManager.getBranchProperties(yearKey, properties).getProperty(CoreNamespace.pBranchName);
    }

    private String getPeriodName(String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pPeriodName);
        return (String) periodManager.getPeriodProperties(yearKey, properties).getProperty(CoreNamespace.pPeriodName);
    }

    @Override
    public ExamObject getExamProperties(String examKey, Set<String> propertiesName) throws Exception {
        return examManager.getExamProperties(examKey, propertiesName);
    }
}
