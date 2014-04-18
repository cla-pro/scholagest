package net.scholagest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import net.scholagest.business.BranchBusinessLocal;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.Branch;
import net.scholagest.utils.AbstractGuiceContextTest;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link BranchServiceBean}
 * 
 * @author CLA
 * @since 0.14.0
 */
@RunWith(MockitoJUnitRunner.class)
public class BranchServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private BranchBusinessLocal branchBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(BranchBusinessLocal.class).toInstance(branchBusiness);
        module.bind(BranchServiceLocal.class).to(BranchServiceBean.class);
    }

    @Test
    public void testGetBranches() {
        setAdminSubject();
        final BranchServiceLocal testee = getInstance(BranchServiceLocal.class);

        final Branch expected = new Branch("1", "name", true, "clazz", new ArrayList<String>());
        when(branchBusiness.getBranch(1L)).thenReturn(expected);

        assertTrue(testee.getBranches(new ArrayList<String>()).isEmpty());
        verify(branchBusiness, never()).getBranch(anyLong());

        assertEquals(Arrays.asList(expected), testee.getBranches(Arrays.asList("1", "2")));
        verify(branchBusiness).getBranch(eq(1L));
        verify(branchBusiness).getBranch(eq(2L));
    }

    @Test
    public void testGetBranch() {
        setAdminSubject();
        final BranchServiceLocal testee = getInstance(BranchServiceLocal.class);

        final Branch expected = new Branch("1", "name", true, "clazz", new ArrayList<String>());
        when(branchBusiness.getBranch(1L)).thenReturn(expected);

        assertNull(testee.getBranch(null));
        verify(branchBusiness, never()).getBranch(anyLong());

        assertNull(testee.getBranch("2"));
        verify(branchBusiness).getBranch(eq(2L));

        assertEquals(expected, testee.getBranch("1"));
        verify(branchBusiness).getBranch(eq(1L));
    }

    @Test
    public void testCreateBranch() {
        setAdminSubject();
        final BranchServiceLocal testee = getInstance(BranchServiceLocal.class);

        final Branch created = new Branch("1", "name", true, "clazz", new ArrayList<String>());
        when(branchBusiness.createBranch(any(Branch.class))).thenReturn(created);

        assertNull(testee.createBranch(null));
        verify(branchBusiness, never()).createBranch(any(Branch.class));

        final Branch toCreate = new Branch();
        assertEquals(created, testee.createBranch(toCreate));
        verify(branchBusiness).createBranch(eq(toCreate));
    }

    @Test
    public void testSaveBranch() {
        setAdminSubject();
        final BranchServiceLocal testee = getInstance(BranchServiceLocal.class);

        final Branch saved = new Branch("1", "name", true, "clazz", new ArrayList<String>());
        when(branchBusiness.saveBranch(any(Branch.class))).thenReturn(saved);

        final Branch toSave = new Branch();
        assertNull(testee.saveBranch(null, toSave));
        assertNull(testee.saveBranch("1", null));
        verify(branchBusiness, never()).saveBranch(any(Branch.class));

        assertEquals(saved, testee.saveBranch("1", toSave));
        verify(branchBusiness).saveBranch(eq(toSave));
    }

    @Test
    public void testCreateBranchAuthorization() {
        setNoRightSubject();
        final BranchServiceLocal testee = getInstance(BranchServiceLocal.class);

        try {
            testee.createBranch(new Branch());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
        }
    }

    @Test
    public void testSaveBranchAuthorization() {
        setNoRightSubject();
        final BranchServiceLocal testee = getInstance(BranchServiceLocal.class);

        final String id = "id";
        try {
            testee.saveBranch(id, new Branch());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
            verify(ScholagestThreadLocal.getSubject()).isPermitted(id);
        }
    }
}
