package net.scholagest.old.managers.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.IYearManager;
import net.scholagest.old.managers.ontology.RDF;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.ObjectHelper;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class YearManager extends ObjectManager implements IYearManager {

    @Inject
    public YearManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public boolean checkWhetherYearExists(String yearName) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String yearKey = (String) transaction.get(CoreNamespace.yearsBase, yearName, null);
        return yearKey != null;
    }

    @Override
    public BaseObject createNewYear(String yearName) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String yearKey = CoreNamespace.yearNs + "#" + UUID.randomUUID().toString();

        DBSet classes = DBSet.createDBSet(transaction, generateYearClassesKey(yearKey));

        BaseObject year = new BaseObject(yearKey, CoreNamespace.tYear);
        year.putProperty(CoreNamespace.pYearName, yearName);
        year.putProperty(CoreNamespace.pYearClasses, classes);

        persistObject(transaction, year);

        transaction.insert(CoreNamespace.yearsBase, yearName, yearKey, null);

        return year;
    }

    private String generateYearClassesKey(String yearKey) {
        return yearKey + "_classes";
    }

    @Override
    public void restoreYear(String yearKey) throws ScholagestException {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        if (getCurrentYearKey() != null) {
            throw new ScholagestException(ScholagestExceptionErrorCode.YEAR_ALREADY_RUNNING, "Year already running");
        }

        if (!CoreNamespace.tYear.equals(transaction.get(yearKey, RDF.type, null))) {
            throw new ScholagestException(ScholagestExceptionErrorCode.GENERAL, "Node being restored (" + yearKey + ") is not of type tYear");
        }

        transaction.insert(CoreNamespace.yearsBase, CoreNamespace.pYearCurrent, yearKey, null);
    }

    @Override
    public void backupYear() throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        if (getCurrentYearKey() == null) {
            throw new Exception("No year currently running");
        }

        transaction.delete(CoreNamespace.yearsBase, CoreNamespace.pYearCurrent, null);
    }

    @Override
    public BaseObject getCurrentYearKey() {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String currentYearKey = (String) transaction.get(CoreNamespace.yearsBase, CoreNamespace.pYearCurrent, null);

        if (currentYearKey == null) {
            return null;
        }
        return new BaseObject(currentYearKey, CoreNamespace.tYear);
    }

    @Override
    public Set<BaseObject> getYears() {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        Set<BaseObject> yearSet = new HashSet<>();

        for (String col : transaction.getColumns(CoreNamespace.yearsBase)) {
            yearSet.add(new BaseObject((String) transaction.get(CoreNamespace.yearsBase, col, null), CoreNamespace.tYear));
        }

        return yearSet;
    }

    @Override
    public BaseObject getYearProperties(String yearKey, Set<String> propertiesName) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        return new BaseObject(transaction, new ObjectHelper(getOntologyManager()), yearKey);
    }

    @Override
    public void addClassToYear(String yearKey, String classKey) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String setKey = generateYearClassesKey(yearKey);
        DBSet classesSet = new DBSet(transaction, setKey);
        classesSet.add(classKey);
    }

    @Override
    public void setYearProperties(String yearKey, Map<String, Object> yearProperties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        BaseObject yearObject = new BaseObject(transaction, new ObjectHelper(getOntologyManager()), yearKey);
        yearObject.putAllProperties(yearProperties);
    }
}
