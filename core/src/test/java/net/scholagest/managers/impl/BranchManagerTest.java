package net.scholagest.managers.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.mockito.Mockito;

public class BranchManagerTest extends AbstractTestWithTransaction {
    private static final String BRANCH_NAME = "Math";
    private static final String YEAR_NAME = "2012-2013";
    private static final String CLASS_NAME = "1P A";
    private static final String BRANCH_KEY = CoreNamespace.branchNs + "/" + YEAR_NAME + "/" + CLASS_NAME + "#" + BRANCH_NAME;

    private IBranchManager branchManager = new BranchManager(new OntologyManager());

    @Test
    public void testCreateNewBranch() throws Exception {
        BaseObject branch = branchManager.createBranch(BRANCH_NAME, CLASS_NAME, YEAR_NAME);

        assertEquals(BRANCH_KEY, branch.getKey());
        assertEquals(CoreNamespace.tBranch, branch.getType());
        Mockito.verify(transaction).insert(Mockito.eq(branch.getKey()), Mockito.eq(RDF.type), Mockito.eq(CoreNamespace.tBranch), Mockito.anyString());
    }

    @Test
    public void testSetAndGetBranchProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Branch" });

        Map<String, Object> properties = createClassProperties();
        branchManager.setBranchProperties(BRANCH_KEY, properties);
        BaseObject branch = branchManager.getBranchProperties(BRANCH_KEY, properties.keySet());

        assertEquals(BRANCH_KEY, branch.getKey());
        assertEquals(CoreNamespace.tBranch, branch.getType());
        assertMapEquals(properties, branch.getProperties());
    }

    private Map<String, Object> createClassProperties() {
        Map<String, Object> classProperties = new HashMap<String, Object>();

        classProperties.put(CoreNamespace.pBranchName, BRANCH_NAME);

        return classProperties;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Branch");
        ScholagestThreadLocal.setTransaction(transaction);

        IBranchManager classManager = new BranchManager(new OntologyManager());

        createBranch(transaction, classManager, "Math", CLASS_NAME, YEAR_NAME);
        createBranch(transaction, classManager, "Geographie", CLASS_NAME, YEAR_NAME);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }

    private static void createBranch(ITransaction transaction, IBranchManager branchManager, String branchName, String className, String yearName)
            throws Exception {
        BaseObject clazz = branchManager.createBranch(branchName, className, yearName);

        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pBranchName, branchName);

        branchManager.setBranchProperties(clazz.getKey(), properties);
    }
}
