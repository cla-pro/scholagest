package net.scholagest.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.exception.ScholagestException;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class YearManager extends ObjectManager implements IYearManager {

    @Inject
    public YearManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createNewYear(String requestId, ITransaction transaction, String yearName) throws Exception {
        String yearKey = generateYearKey(yearName);

        BaseObject year = super.createObject(requestId, transaction, yearKey, CoreNamespace.tYear);

        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pYearName, yearName);
        super.setObjectProperties(requestId, transaction, yearKey, properties);

        transaction.insert(CoreNamespace.yearsBase, yearName, yearKey, null);

        String setKey = generateYearClassesKey(yearKey);
        DBSet.createDBSet(transaction, setKey);
        transaction.insert(yearKey, CoreNamespace.pYearClasses, setKey, null);

        return year;
    }

    private String generateYearClassesKey(String yearKey) {
        return yearKey + "_classes";
    }

    private String generateYearKey(String yearName) {
        return CoreNamespace.yearNs + "#" + yearName;
    }

    @Override
    public void restoreYear(String requestId, ITransaction transaction, String yearKey) throws Exception {
        if (getCurrentYearKey(requestId, transaction) != null) {
            throw new ScholagestException(-1, -1, "Year already running");
        }

        if (!CoreNamespace.tYear.equals(transaction.get(yearKey, RDF.type, null))) {
            throw new ScholagestException(-1, -1, "Node being restored (" + yearKey + ") is not of type tYear");
        }

        transaction.insert(CoreNamespace.yearsBase, CoreNamespace.pYearCurrent, yearKey, null);
    }

    @Override
    public void backupYear(String requestId, ITransaction transaction) throws Exception {
        if (getCurrentYearKey(requestId, transaction) == null) {
            throw new Exception("No year currently running");
        }

        transaction.delete(CoreNamespace.yearsBase, CoreNamespace.pYearCurrent, null);
    }

    @Override
    public BaseObject getCurrentYearKey(String requestId, ITransaction transaction) throws Exception {
        String currentYearKey = (String) transaction.get(CoreNamespace.yearsBase, CoreNamespace.pYearCurrent, null);

        if (currentYearKey == null) {
            return null;
        }
        return new BaseObject(currentYearKey, CoreNamespace.tYear);
    }

    @Override
    public Set<BaseObject> getYears(String requestId, ITransaction transaction) throws Exception {
        Set<BaseObject> yearSet = new HashSet<>();

        for (String col : transaction.getColumns(CoreNamespace.yearsBase)) {
            yearSet.add(new BaseObject((String) transaction.get(CoreNamespace.yearsBase, col, null), CoreNamespace.tYear));
        }

        return yearSet;
    }

    @Override
    public BaseObject getYearProperties(String requestId, ITransaction transaction, String yearKey, Set<String> propertiesName) throws Exception {
        BaseObject year = new BaseObject(yearKey, CoreNamespace.tYear);
        year.setProperties(super.getObjectProperties(requestId, transaction, yearKey, propertiesName));

        return year;
    }

    @Override
    public void addClassToYear(String requestId, ITransaction transaction, String yearKey, String classKey) throws Exception {
        String setKey = generateYearClassesKey(yearKey);
        DBSet classesSet = new DBSet(transaction, setKey);
        classesSet.add(classKey);
    }
}
