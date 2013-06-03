package net.scholagest.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IBranchService;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BranchServiceTest extends AbstractTest {
    private static final String CLASS_KEY = "classKey";
    private InMemoryDatabase database;

    private IBranchBusinessComponent branchBusinessComponent;
    private IBranchService testee;

    @Before
    public void setUpTest() {
        database = Mockito.spy(new InMemoryDatabase());
        defineAdminSubject();

        branchBusinessComponent = Mockito.mock(IBranchBusinessComponent.class);

        testee = new BranchService(database, branchBusinessComponent);
    }

    @Test
    public void testCreateBranch() throws Exception {
        testee.createBranch(CLASS_KEY, new HashMap<String, Object>());

        Mockito.verify(branchBusinessComponent).createBranch(Mockito.eq(CLASS_KEY), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(database).getTransaction(getKeyspace());
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
        testee.getBranchProperties(branchKey, new HashSet<String>());

        Mockito.verify(branchBusinessComponent).getBranchProperties(Mockito.eq(branchKey), Mockito.anySetOf(String.class));
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

    private BaseObject createClassKeyObject() {
        BaseObject classKeyObject = new BaseObject(null, null);

        classKeyObject.putProperty(CoreNamespace.pBranchClass, CLASS_KEY);

        return classKeyObject;
    }
}
