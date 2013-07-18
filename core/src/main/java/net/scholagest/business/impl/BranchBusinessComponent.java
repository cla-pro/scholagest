package net.scholagest.business.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BranchObject;
import net.scholagest.objects.ExamObject;
import net.scholagest.objects.PeriodObject;

import com.google.inject.Inject;

public class BranchBusinessComponent implements IBranchBusinessComponent {
    private IExamManager examManager;
    private IPeriodManager periodManager;
    private IBranchManager branchManager;
    private IClassManager classManager;
    private IYearManager yearManager;

    @Inject
    public BranchBusinessComponent(IExamManager examManager, IPeriodManager periodManager, IBranchManager branchManager, IClassManager classManager,
            IYearManager yearManager) {
        this.examManager = examManager;
        this.periodManager = periodManager;
        this.branchManager = branchManager;
        this.classManager = classManager;
        this.yearManager = yearManager;
    }

    @Override
    public BranchObject createBranch(String classKey, Map<String, Object> branchProperties) throws Exception {
        String className = getClassName(classKey);
        String yearName = getYearName(classKey);

        String branchName = (String) branchProperties.get(CoreNamespace.pBranchName);
        BranchObject branch = branchManager.createBranch(branchName, classKey, className, yearName, branchProperties);

        addBranchToClass(classKey, branch);

        ExamObject branchMean = createMeanExam(branch, classKey, className, yearName, branchName, "");
        branch.setMeanKey(branchMean.getKey());

        createPeriods(branch, classKey, className, yearName, branchName);

        return branch;
    }

    private ExamObject createMeanExam(BranchObject branch, String classKey, String className, String yearName, String branchName, String periodName)
            throws Exception {
        return examManager.createExam("mean", classKey, periodName, branchName, className, yearName, new HashMap<String, Object>());
    }

    private void createPeriods(BranchObject branch, String classKey, String className, String yearName, String branchName) throws Exception {
        DBSet periodsSet = branch.getPeriods();
        for (int i = 1; i < 4; i++) {
            String periodName = "Trimestre " + i;
            PeriodObject period = periodManager.createPeriod(periodName, classKey, branchName, className, yearName);

            ExamObject branchMean = createMeanExam(branch, classKey, className, yearName, branchName, "");
            period.setMeanKey(branchMean.getKey());

            Map<String, Object> periodProperties = createPeriodProperties(periodName);
            periodManager.setPeriodProperties(period.getKey(), periodProperties);

            periodsSet.add(period.getKey());
        }
    }

    private Map<String, Object> createPeriodProperties(String periodName) {
        Map<String, Object> periodProperties = new HashMap<String, Object>();
        periodProperties.put(CoreNamespace.pPeriodName, periodName);
        return periodProperties;
    }

    private void addBranchToClass(String classKey, BranchObject branch) throws Exception {
        Set<String> properties = new HashSet<>(Arrays.asList(CoreNamespace.pClassBranches));
        DBSet branchesSet = classManager.getClassProperties(classKey, properties).getBranches();

        branchesSet.add(branch.getKey());
    }

    private String getClassName(String classKey) throws Exception {
        Set<String> properties = new HashSet<>(Arrays.asList(CoreNamespace.pClassName));
        String className = (String) classManager.getClassProperties(classKey, properties).getProperty(CoreNamespace.pClassName);
        return className;
    }

    private String getYearName(String classKey) throws Exception {
        Set<String> yearKeyProperties = new HashSet<>(Arrays.asList(CoreNamespace.pClassYear));
        String yearKey = (String) classManager.getClassProperties(classKey, yearKeyProperties).getProperty(CoreNamespace.pClassYear);

        Set<String> yearNameProperties = new HashSet<>(Arrays.asList(CoreNamespace.pYearName));
        String yearName = (String) yearManager.getYearProperties(yearKey, yearNameProperties).getProperty(CoreNamespace.pYearName);

        return yearName;
    }

    @Override
    public BranchObject getBranchProperties(String branchKey, Set<String> propertiesName) throws Exception {
        return branchManager.getBranchProperties(branchKey, propertiesName);
    }

    @Override
    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties) throws Exception {
        branchManager.setBranchProperties(branchKey, branchProperties);
    }
}
