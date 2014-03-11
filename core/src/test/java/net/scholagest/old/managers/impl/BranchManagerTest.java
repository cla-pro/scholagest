package net.scholagest.old.managers.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IBranchManager;
import net.scholagest.old.managers.impl.BranchManager;
import net.scholagest.old.managers.ontology.OntologyManager;
import net.scholagest.old.managers.ontology.RDF;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.BranchObject;
import net.scholagest.old.objects.BranchType;
import net.scholagest.utils.AbstractTestWithTransaction;
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;

public class BranchManagerTest extends AbstractTestWithTransaction {
    private static final String BRANCH_NAME = "Math";
    private static final String YEAR_NAME = "2012-2013";
    private static final String CLASS_NAME = "1P A";
    private static final String BRANCH_KEY = CoreNamespace.branchNs + "#cbe012bd-e40b-4524-95bb-084bda2f570a";
    private static final String BRANCHES_BASE_PROPERTY_NAME = YEAR_NAME + "/" + CLASS_NAME + "/" + BRANCH_NAME;
    private static final String CLASS_KEY = UUID.randomUUID().toString();

    private IBranchManager branchManager = new BranchManager(new OntologyManager());

    @Test
    public void testCheckWhetherBranchExistsInClass() {
        super.fillTransactionWithDataSets(new String[] { "Branch" });

        assertTrue(branchManager.checkWhetherBranchExistsInClass(BRANCH_NAME, CLASS_NAME, YEAR_NAME));
        assertFalse(branchManager.checkWhetherBranchExistsInClass("Histoire", CLASS_NAME, YEAR_NAME));
        assertFalse(branchManager.checkWhetherBranchExistsInClass(BRANCH_NAME, "2P B", YEAR_NAME));
        assertFalse(branchManager.checkWhetherBranchExistsInClass(BRANCH_NAME, CLASS_NAME, "2011-2012"));
    }

    @Test
    public void testCreateNewBranch() throws Exception {
        BranchObject branch = branchManager.createBranch(BRANCH_NAME, CLASS_KEY, CLASS_NAME, YEAR_NAME, createBranchProperties());

        assertEquals(CoreNamespace.tBranch, branch.getType());
        verify(transaction).insert(eq(branch.getKey()), eq(RDF.type), eq(CoreNamespace.tBranch), anyString());
        // Check default branch type
        verify(transaction).insert(eq(branch.getKey()), eq(CoreNamespace.pBranchType), eq(BranchType.NUMERICAL.name()), anyString());
        verify(transaction).insert(eq(branch.getKey()), eq(CoreNamespace.pBranchClass), eq(CLASS_KEY), anyString());
        verify(transaction).insert(eq(branch.getKey()), eq(CoreNamespace.pBranchPeriods), anyString(), anyString());

        verify(transaction).insert(eq(CoreNamespace.branchesBase), eq(BRANCHES_BASE_PROPERTY_NAME), eq(branch.getKey()), anyString());
    }

    private HashMap<String, Object> createBranchProperties() {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put(CoreNamespace.pBranchClass, CLASS_KEY);
        return properties;
    }

    @Test
    public void testSetAndGetBranchProperties() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Branch" });

        Map<String, Object> properties = createClassProperties();
        branchManager.setBranchProperties(BRANCH_KEY, properties);
        BaseObject branch = branchManager.getBranchProperties(BRANCH_KEY, properties.keySet());

        assertEquals(BRANCH_KEY, branch.getKey());
        assertEquals(CoreNamespace.tBranch, branch.getType());
        assertEquals(CLASS_KEY, branch.getProperty(CoreNamespace.pBranchClass));
    }

    private Map<String, Object> createClassProperties() {
        Map<String, Object> classProperties = createBranchProperties();

        classProperties.put(CoreNamespace.pBranchName, BRANCH_NAME);

        return classProperties;
    }

    public static void main(String[] args) throws Exception {
        InMemoryTransaction transaction = new InMemoryDatabase().getTransaction("Branch");
        ScholagestThreadLocal.setTransaction(transaction);

        IBranchManager classManager = new BranchManager(new OntologyManager());

        createBranch(transaction, classManager, "Math", CLASS_KEY, CLASS_NAME, YEAR_NAME);
        createBranch(transaction, classManager, "Geographie", CLASS_KEY, CLASS_NAME, YEAR_NAME);

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);
    }

    private static void createBranch(ITransaction transaction, IBranchManager branchManager, String branchName, String classKey, String className,
            String yearName) throws Exception {
        BaseObject clazz = branchManager.createBranch(branchName, classKey, className, yearName, new HashMap<String, Object>());

        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pBranchName, branchName);

        branchManager.setBranchProperties(clazz.getKey(), properties);
    }
}
