package net.scholagest.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BranchObject;
import net.scholagest.objects.BranchObjectMock;
import net.scholagest.services.IBranchService;
import net.scholagest.services.kdom.KSet;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BranchServiceTest extends AbstractTest {
    private static final String DBSET_PROP = "DBSET_PROP";

    private static final String CLASS_KEY = "classKey";

    private InMemoryDatabase database;
    private IBranchBusinessComponent branchBusinessComponent;
    private IOntologyBusinessComponent ontologyBusinessComponent;

    private IBranchService testee;

    @Before
    public void setUpTest() throws Exception {
        database = Mockito.spy(new InMemoryDatabase());
        defineAdminSubject();

        branchBusinessComponent = Mockito.mock(IBranchBusinessComponent.class);
        ontologyBusinessComponent = Mockito.mock(IOntologyBusinessComponent.class);

        Mockito.when(branchBusinessComponent.createBranch(Mockito.anyString(), Mockito.anyMapOf(String.class, Object.class))).thenReturn(
                BranchObjectMock.createBranchObject(UUID.randomUUID().toString(), createBranchProperties()));
        Mockito.when(branchBusinessComponent.getBranchProperties(Mockito.anyString(), Mockito.anySetOf(String.class))).thenReturn(
                BranchObjectMock.createBranchObject(UUID.randomUUID().toString(), createBranchProperties()));

        testee = new BranchService(database, branchBusinessComponent, ontologyBusinessComponent);
    }

    @Test
    public void testCreateBranch() throws Exception {
        BaseObject branch = testee.createBranch(CLASS_KEY, new HashMap<String, Object>());

        // Ensure the conversion from DB to Kdom
        assertTrue(branch.getProperty(DBSET_PROP) instanceof KSet);

        Mockito.verify(branchBusinessComponent).createBranch(Mockito.eq(CLASS_KEY), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    private Map<String, Object> createBranchProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(DBSET_PROP, new DBSet(Mockito.mock(ITransaction.class), UUID.randomUUID().toString()));
        // For the right check
        properties.put(CoreNamespace.pBranchClass, UUID.randomUUID().toString());

        return properties;
    }

    @Test
    public void testCreateBranchInsufficientPrivileges() throws Exception {
        defineClassTeacherSubject();
        testee.createBranch(CLASS_KEY, new HashMap<String, Object>());
        Mockito.verify(branchBusinessComponent).createBranch(Mockito.eq(CLASS_KEY), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());

        try {
            defineClassHelpTeacherSubject();
            testee.createBranch(CLASS_KEY, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.createBranch(CLASS_KEY, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testGetBranchProperties() throws Exception {
        String branchKey = "branchKey";
        BaseObject branch = testee.getBranchProperties(branchKey, new HashSet<String>(Arrays.asList(DBSET_PROP)));

        // Ensure the conversion from DB to Kdom
        assertTrue(branch.getProperty(DBSET_PROP) instanceof KSet);

        Mockito.verify(branchBusinessComponent).getBranchProperties(branchKey, new HashSet<String>(Arrays.asList(CoreNamespace.pBranchClass)));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetBranchPropertiesOtherUsers() throws Exception {
        String branchKey = "branchKey";
        Mockito.when(branchBusinessComponent.getBranchProperties(branchKey, new HashSet<String>(Arrays.asList(CoreNamespace.pBranchClass))))
                .thenReturn(createClassKeyObject());

        defineClassTeacherSubject();
        testee.getBranchProperties(branchKey, new HashSet<String>());
        Mockito.verify(branchBusinessComponent, Mockito.times(2)).getBranchProperties(Mockito.eq(branchKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
        Mockito.reset(branchBusinessComponent);
        Mockito.reset(database);

        Mockito.when(branchBusinessComponent.getBranchProperties(branchKey, new HashSet<String>(Arrays.asList(CoreNamespace.pBranchClass))))
                .thenReturn(createClassKeyObject());

        try {
            defineClassHelpTeacherSubject();
            testee.getBranchProperties(branchKey, new HashSet<String>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.getBranchProperties(branchKey, new HashSet<String>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testSetBranchProperties() throws Exception {
        String branchKey = "branchKey";
        Mockito.when(branchBusinessComponent.getBranchProperties(branchKey, new HashSet<String>(Arrays.asList(CoreNamespace.pBranchClass))))
                .thenReturn(createClassKeyObject());

        testee.setBranchProperties(branchKey, new HashMap<String, Object>());

        Mockito.verify(branchBusinessComponent).setBranchProperties(Mockito.eq(branchKey), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testSetBranchPropertiesInsufficientPrivileges() throws Exception {
        String branchKey = "branchKey";
        Mockito.when(branchBusinessComponent.getBranchProperties(branchKey, new HashSet<String>(Arrays.asList(CoreNamespace.pBranchClass))))
                .thenReturn(createClassKeyObject());

        defineClassTeacherSubject();
        testee.setBranchProperties(branchKey, new HashMap<String, Object>());
        Mockito.verify(branchBusinessComponent).setBranchProperties(Mockito.eq(branchKey), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());

        try {
            defineClassHelpTeacherSubject();
            testee.setBranchProperties(branchKey, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.setBranchProperties(branchKey, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    private BranchObject createClassKeyObject() {
        BranchObject classKeyObject = new BranchObject(null);

        classKeyObject.putProperty(CoreNamespace.pBranchClass, CLASS_KEY);

        return classKeyObject;
    }
}
