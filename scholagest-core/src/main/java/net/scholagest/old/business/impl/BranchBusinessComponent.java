package net.scholagest.old.business.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.old.business.IBranchBusinessComponent;
import net.scholagest.old.managers.IBranchManager;
import net.scholagest.old.managers.IClassManager;
import net.scholagest.old.managers.IExamManager;
import net.scholagest.old.managers.IPeriodManager;
import net.scholagest.old.managers.IStudentManager;
import net.scholagest.old.managers.IYearManager;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.BranchObject;
import net.scholagest.old.objects.ClassObject;
import net.scholagest.old.objects.ExamObject;
import net.scholagest.old.objects.PeriodObject;

import com.google.inject.Inject;

public class BranchBusinessComponent implements IBranchBusinessComponent {
    private IExamManager examManager;
    private IPeriodManager periodManager;
    private IBranchManager branchManager;
    private IClassManager classManager;
    private IYearManager yearManager;
    private IStudentManager studentManager;

    @Inject
    public BranchBusinessComponent(IExamManager examManager, IPeriodManager periodManager, IBranchManager branchManager, IClassManager classManager,
            IYearManager yearManager, IStudentManager studentManager) {
        this.examManager = examManager;
        this.periodManager = periodManager;
        this.branchManager = branchManager;
        this.classManager = classManager;
        this.yearManager = yearManager;
        this.studentManager = studentManager;
    }

    @Override
    public BranchObject createBranch(String classKey, Map<String, Object> branchProperties) throws Exception {
        String className = getClassName(classKey);
        String yearName = getYearName(classKey);
        String branchName = (String) branchProperties.get(CoreNamespace.pBranchName);

        if (branchManager.checkWhetherBranchExistsInClass(branchName, className, yearName)) {
            throw new ScholagestException(ScholagestExceptionErrorCode.OBJECT_ALREADY_EXISTS, "A branch with the same name " + branchName
                    + " already exists for class=" + classKey);
        }

        BranchObject branch = branchManager.createBranch(branchName, classKey, className, yearName, branchProperties);

        addBranchToClass(classKey, branch);

        ExamObject branchMean = createMeanExam(branch, classKey, className, yearName, branchName, "");
        branch.setMeanKey(branchMean.getKey());

        createPeriods(branch, classKey, className, yearName, branchName);

        return branch;
    }

    private ExamObject createMeanExam(BranchObject branch, String classKey, String className, String yearName, String branchName, String periodName)
            throws Exception {
        ExamObject examObject = examManager.createExam("mean", classKey, periodName, branchName, className, yearName, new HashMap<String, Object>());
        examObject.setCoeff(1);
        return examObject;
    }

    private void createPeriods(BranchObject branch, String classKey, String className, String yearName, String branchName) throws Exception {
        DBSet periodsSet = branch.getPeriods();
        for (int i = 1; i < 4; i++) {
            String periodName = "Trimestre " + i;
            PeriodObject period = periodManager.createPeriod(periodName, classKey);

            ExamObject branchMean = createMeanExam(branch, classKey, className, yearName, branchName, periodName);
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

    @Override
    public Map<String, Map<String, BaseObject>> getPeriodMeans(String branchKey, Set<String> studentKeys) {
        BranchObject branchObject = branchManager.getBranchProperties(branchKey, new HashSet<String>());
        String meanExamKey = branchObject.getMeanKey();

        ClassObject classObject = classManager.getClassProperties(branchObject.getClassKey(), new HashSet<String>());
        String yearKey = classObject.getYearKey();

        Map<String, BaseObject> grades = new HashMap<>();
        for (String studentKey : studentKeys) {
            BaseObject gradeObject = studentManager.getStudentGrades(studentKey, new HashSet<String>(Arrays.asList(meanExamKey)), yearKey).get(
                    meanExamKey);
            grades.put(studentKey, gradeObject);
        }

        Map<String, Map<String, BaseObject>> means = new HashMap<>();
        means.put(meanExamKey, grades);
        return means;
    }
}
