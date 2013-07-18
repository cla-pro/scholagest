package net.scholagest.managers.impl;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.ObjectHelper;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class YearManager extends ObjectManager implements IYearManager {

    @Inject
    public YearManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createNewYear(String yearName) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String yearKey = generateYearKey(yearName);

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

    private String generateYearKey(String yearName) {
        return CoreNamespace.yearNs + "#" + yearName;
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
}
