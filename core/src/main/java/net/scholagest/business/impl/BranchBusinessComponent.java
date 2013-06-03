package net.scholagest.business.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.kdom.DBToKdomConverter;

import com.google.inject.Inject;

public class BranchBusinessComponent implements IBranchBusinessComponent {
    private IPeriodManager periodManager;
    private IBranchManager branchManager;
    private IClassManager classManager;
    private IYearManager yearManager;

    @Inject
    public BranchBusinessComponent(IPeriodManager periodManager, IBranchManager branchManager, IClassManager classManager, IYearManager yearManager) {
        this.periodManager = periodManager;
        this.branchManager = branchManager;
        this.classManager = classManager;
        this.yearManager = yearManager;
    }

    @Override
    public BaseObject createBranch(String classKey, Map<String, Object> branchProperties) throws Exception {
        String className = getClassName(classKey);
        String yearName = getYearName(classKey);

        String branchName = (String) branchProperties.get(CoreNamespace.pBranchName);
        BaseObject branch = branchManager.createBranch(branchName, className, yearName);

        branchProperties.put(CoreNamespace.pBranchClass, classKey);
        branchManager.setBranchProperties(branch.getKey(), branchProperties);

        addBranchToClass(classKey, branch.getKey());

        createPeriods(branch.getKey(), className, yearName, branchName);

        return branch;
    }

    private void createPeriods(String branchKey, String className, String yearName, String branchName) throws Exception {
        BaseObject branchProperties = branchManager.getBranchProperties(branchKey, new HashSet<>(Arrays.asList(CoreNamespace.pBranchPeriods)));
        DBSet periodsSet = (DBSet) branchProperties.getProperty(CoreNamespace.pBranchPeriods);
        for (int i = 1; i < 4; i++) {
            String periodName = "Trimestre " + i;
            BaseObject period = periodManager.createPeriod(periodName, branchName, className, yearName);

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

    private void addBranchToClass(String classKey, String branchKey) throws Exception {
        Set<String> properties = new HashSet<>(Arrays.asList(CoreNamespace.pClassBranches));
        DBSet branchesSet = (DBSet) classManager.getClassProperties(classKey, properties).getProperty(CoreNamespace.pClassBranches);

        branchesSet.add(branchKey);
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
    public BaseObject getBranchProperties(String branchKey, Set<String> propertiesName) throws Exception {
        return new DBToKdomConverter().convertDbToKdom(branchManager.getBranchProperties(branchKey, propertiesName));
    }

    @Override
    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties) throws Exception {
        branchManager.setBranchProperties(branchKey, branchProperties);
    }
}
