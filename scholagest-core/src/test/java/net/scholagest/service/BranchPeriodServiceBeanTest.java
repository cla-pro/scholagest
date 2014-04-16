package net.scholagest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import net.scholagest.business.BranchPeriodBusinessLocal;
import net.scholagest.object.BranchPeriod;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link BranchPeriodServiceBean}
 * 
 * @author CLA
 * @since 0.14.0
 */
@RunWith(MockitoJUnitRunner.class)
public class BranchPeriodServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private BranchPeriodBusinessLocal branchPeriodBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(BranchPeriodBusinessLocal.class).toInstance(branchPeriodBusiness);
        module.bind(BranchPeriodServiceLocal.class).to(BranchPeriodServiceBean.class);
    }

    @Test
    public void testGetBranchPeriods() {
        setAdminSubject();
        final BranchPeriodServiceLocal testee = getInstance(BranchPeriodServiceLocal.class);

        final BranchPeriod expected = new BranchPeriod("1", "branch", "period", new ArrayList<String>(), null, new ArrayList<String>());
        when(branchPeriodBusiness.getBranchPeriod(1L)).thenReturn(expected);

        assertTrue(testee.getBranchPeriods(new ArrayList<String>()).isEmpty());
        verify(branchPeriodBusiness, never()).getBranchPeriod(anyLong());

        assertEquals(Arrays.asList(expected), testee.getBranchPeriods(Arrays.asList("1", "2")));
        verify(branchPeriodBusiness).getBranchPeriod(eq(1L));
        verify(branchPeriodBusiness).getBranchPeriod(eq(2L));
    }

    @Test
    public void testGetBranchPeriod() {
        setAdminSubject();
        final BranchPeriodServiceLocal testee = getInstance(BranchPeriodServiceLocal.class);

        final BranchPeriod expected = new BranchPeriod("1", "branch", "period", new ArrayList<String>(), null, new ArrayList<String>());
        when(branchPeriodBusiness.getBranchPeriod(1L)).thenReturn(expected);

        assertNull(testee.getBranchPeriod(null));
        verify(branchPeriodBusiness, never()).getBranchPeriod(anyLong());

        assertNull(testee.getBranchPeriod("2"));
        verify(branchPeriodBusiness).getBranchPeriod(eq(2L));

        assertEquals(expected, testee.getBranchPeriod("1"));
        verify(branchPeriodBusiness).getBranchPeriod(eq(1L));
    }
}
