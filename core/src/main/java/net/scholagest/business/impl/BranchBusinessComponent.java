package net.scholagest.business.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.impl.CoreNamespace;
import net.scholagest.managers.ontology.types.DBSet;
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
    public BaseObject createBranch(String requestId, ITransaction transaction, String classKey, Map<String, Object> branchProperties)
            throws Exception {
        String className = getClassName(requestId, transaction, classKey);
        String yearName = getYearName(requestId, transaction, classKey);

        String branchName = (String) branchProperties.get(CoreNamespace.pBranchName);
        BaseObject branch = branchManager.createBranch(requestId, transaction, branchName, className, yearName);
        branchManager.setBranchProperties(requestId, transaction, branch.getKey(), branchProperties);

        addBranchToClass(requestId, transaction, classKey, branch.getKey());

        createPeriods(requestId, transaction, branch.getKey(), className, yearName, branchName);

        return branch;
    }

    private void createPeriods(String requestId, ITransaction transaction, String branchKey, String className, String yearName, String branchName)
            throws Exception {
        BaseObject branchProperties = branchManager.getBranchProperties(requestId, transaction, branchKey,
                new HashSet<>(Arrays.asList(CoreNamespace.pBranchPeriods)));
        DBSet periodsSet = (DBSet) branchProperties.getProperty(CoreNamespace.pBranchPeriods);
        for (int i = 1; i < 4; i++) {
            String periodName = "Trimestre " + i;
            BaseObject period = periodManager.createPeriod(requestId, transaction, periodName, branchName, className, yearName);

            Map<String, Object> periodProperties = createPeriodProperties(periodName);
            periodManager.setPeriodProperties(requestId, transaction, period.getKey(), periodProperties);

            periodsSet.add(period.getKey());
        }
    }

    private Map<String, Object> createPeriodProperties(String periodName) {
        Map<String, Object> periodProperties = new HashMap<String, Object>();
        periodProperties.put(CoreNamespace.pPeriodName, periodName);
        return periodProperties;
    }

    private void addBranchToClass(String requestId, ITransaction transaction, String classKey, String branchKey) throws Exception {
        Set<String> properties = new HashSet<>(Arrays.asList(CoreNamespace.pClassBranches));
        DBSet branchesSet = (DBSet) classManager.getClassProperties(requestId, transaction, classKey, properties).getProperty(
                CoreNamespace.pClassBranches);

        branchesSet.add(branchKey);
    }

    private String getClassName(String requestId, ITransaction transaction, String classKey) throws Exception {
        Set<String> properties = new HashSet<>(Arrays.asList(CoreNamespace.pClassName));
        String className = (String) classManager.getClassProperties(requestId, transaction, classKey, properties).getProperty(
                CoreNamespace.pClassName);
        return className;
    }

    private String getYearName(String requestId, ITransaction transaction, String classKey) throws Exception {
        Set<String> yearKeyProperties = new HashSet<>(Arrays.asList(CoreNamespace.pClassYear));
        String yearKey = (String) classManager.getClassProperties(requestId, transaction, classKey, yearKeyProperties).getProperty(
                CoreNamespace.pClassYear);

        Set<String> yearNameProperties = new HashSet<>(Arrays.asList(CoreNamespace.pYearName));
        String yearName = (String) yearManager.getYearProperties(requestId, transaction, yearKey, yearNameProperties).getProperty(
                CoreNamespace.pYearName);

        return yearName;
    }

    @Override
    public BaseObject getBranchProperties(String requestId, ITransaction transaction, String branchKey, Set<String> propertiesName) throws Exception {
        return new DBToKdomConverter().convertDbToKdom(branchManager.getBranchProperties(requestId, transaction, branchKey, propertiesName));
    }

    @Override
    public void setBranchProperties(String requestId, ITransaction transaction, String branchKey, Map<String, Object> branchProperties)
            throws Exception {
        branchManager.setBranchProperties(requestId, transaction, branchKey, branchProperties);
    }
}
