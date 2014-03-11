package net.scholagest.old.managers.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IClassManager;
import net.scholagest.old.managers.impl.ClassManager;
import net.scholagest.old.managers.ontology.OntologyManager;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.ClassObject;
import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.mockito.Mockito;

public class ClassManagerTest extends AbstractTestWithTransaction {
    private static final String CLASS_NAME = "1P A";
    private static final String YEAR_NAME = "2012-2013";
    private static final String CLASS_KEY = CoreNamespace.classNs + "/" + YEAR_NAME + "#" + CLASS_NAME;
    private static final String YEAR_KEY = CoreNamespace.yearNs + "#" + YEAR_NAME;

    private IClassManager classManager = new ClassManager(new OntologyManager());

    @Test
    public void testCheckWhetherClassExistsInYear() {
        fillTransactionWithDataSets(new String[] { "Class" });

        assertTrue(classManager.checkWhetherClassExistsInYear(CLASS_NAME, YEAR_NAME));
        assertFalse(classManager.checkWhetherClassExistsInYear("6P A", YEAR_NAME));
        assertFalse(classManager.checkWhetherClassExistsInYear(CLASS_NAME, "2011-2012"));
    }

    @Test
    public void testCreateNewClass() throws Exception {
        ClassObject classObject = classManager.createClass(CLASS_NAME, YEAR_NAME, new HashMap<String, Object>());

        assertEquals(CoreNamespace.tClass, classObject.getType());
        Mockito.verify(transaction).insert(Mockito.eq(CoreNamespace.classesBase), Mockito.eq(YEAR_NAME + "/" + CLASS_NAME),
                Mockito.eq(classObject.getKey()), Mockito.anyString());
    }

    @Test
    public void testGetClasses() throws Exception {
        fillTransactionWithDataSets(new String[] { "Class" });

        Set<BaseObject> classKeys = classManager.getClasses(YEAR_KEY);
        assertEquals(2, classKeys.size());
    }

    @Test
    public void testSetAndGetClassProperties() throws Exception {
        fillTransactionWithDataSets(new String[] { "Class" });

        Map<String, Object> properties = createClassProperties();
        classManager.setClassProperties(CLASS_KEY, properties);
        BaseObject classProperties = classManager.getClassProperties(CLASS_KEY, properties.keySet());

        assertMapEquals(properties, classProperties.getProperties());
    }

    private Map<String, Object> createClassProperties() {
        Map<String, Object> classProperties = new HashMap<String, Object>();

        classProperties.put(CoreNamespace.pClassName, "1P A");
        classProperties.put(CoreNamespace.pClassYear, "2011-2012");

        return classProperties;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Class");
        ScholagestThreadLocal.setTransaction(transaction);

        IClassManager classManager = new ClassManager(new OntologyManager());

        createClass(transaction, classManager, "1P A", YEAR_NAME, YEAR_KEY);
        createClass(transaction, classManager, "2P A", YEAR_NAME, YEAR_KEY);
        createClass(transaction, classManager, "2P B", "2011-2012", CoreNamespace.yearNs + "#2011-2012");

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }

    private static void createClass(ITransaction transaction, IClassManager classManager, String className, String yearName, String yearKey)
            throws Exception {
        BaseObject clazz = classManager.createClass(className, yearName, new HashMap<String, Object>());

        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pClassName, className);
        properties.put(CoreNamespace.pClassYear, yearKey);

        classManager.setClassProperties(clazz.getKey(), properties);
    }
}
