package net.scholagest.old.business.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.old.business.IExamBusinessComponent;
import net.scholagest.old.managers.IBranchManager;
import net.scholagest.old.managers.IClassManager;
import net.scholagest.old.managers.IExamManager;
import net.scholagest.old.managers.IPeriodManager;
import net.scholagest.old.managers.IYearManager;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.ExamObject;

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
            throws ScholagestException {
        String yearName = getYearName(yearKey);
        String className = getClassName(classKey);
        String branchName = getBranchName(branchKey);
        String periodName = getPeriodName(periodKey);
        String examName = (String) examProperties.get(CoreNamespace.pExamName);

        if (examManager.checkWhetherExamExistsInPeriod(examName, yearName, className, branchName, periodName)) {
            throw new ScholagestException(ScholagestExceptionErrorCode.OBJECT_ALREADY_EXISTS, "An exam with the same name " + branchName
                    + " already exists for period=" + periodKey + " and branch=" + branchKey);
        }

        ExamObject exam = examManager.createExam(examName, classKey, periodName, branchName, className, yearName, examProperties);

        DBSet periodExams = periodManager.getPeriodProperties(periodKey, new HashSet<>(Arrays.asList(CoreNamespace.pPeriodExams))).getExams();
        periodExams.add(exam.getKey());

        return exam;
    }

    private String getYearName(String yearKey) {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pYearName);
        return (String) yearManager.getYearProperties(yearKey, properties).getProperty(CoreNamespace.pYearName);
    }

    private String getClassName(String yearKey) {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pClassName);
        return (String) classManager.getClassProperties(yearKey, properties).getProperty(CoreNamespace.pClassName);
    }

    private String getBranchName(String yearKey) {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pBranchName);
        return (String) branchManager.getBranchProperties(yearKey, properties).getProperty(CoreNamespace.pBranchName);
    }

    private String getPeriodName(String yearKey) {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pPeriodName);
        return (String) periodManager.getPeriodProperties(yearKey, properties).getProperty(CoreNamespace.pPeriodName);
    }

    @Override
    public ExamObject getExamProperties(String examKey, Set<String> propertiesName) {
        return examManager.getExamProperties(examKey, propertiesName);
    }
}
