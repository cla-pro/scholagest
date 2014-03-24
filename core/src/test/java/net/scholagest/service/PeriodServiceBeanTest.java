package net.scholagest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.scholagest.business.PeriodBusinessLocal;
import net.scholagest.object.Period;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link PeriodServiceBean}
 * 
 * @author CLA
 * @since 0.14.0
 */
@RunWith(MockitoJUnitRunner.class)
public class PeriodServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private PeriodBusinessLocal periodBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(PeriodBusinessLocal.class).toInstance(periodBusiness);
        module.bind(PeriodServiceLocal.class).to(PeriodServiceBean.class);
    }

    @Test
    public void testGetPeriods() {
        setAdminSubject();
        final PeriodServiceLocal testee = getInstance(PeriodServiceLocal.class);

        final Period period1 = new Period("period1", "name1", "clazz1", Arrays.asList("branchPeriod1"));
        final Period period2 = new Period("period2", "name2", "clazz2", Arrays.asList("branchPeriod2"));
        final List<Period> expected = Arrays.asList(period1, period2);
        when(periodBusiness.getPeriod("period1")).thenReturn(period1);
        when(periodBusiness.getPeriod("period2")).thenReturn(period2);

        assertTrue(testee.getPeriods(new ArrayList<String>()).isEmpty());
        verify(periodBusiness, never()).getPeriod(anyString());

        assertTrue(testee.getPeriods(Arrays.asList("period3")).isEmpty());
        verify(periodBusiness).getPeriod(eq("period3"));

        final List<Period> result = testee.getPeriods(Arrays.asList("period1", "period2"));

        assertEquals(expected, result);
        verify(periodBusiness).getPeriod(eq("period1"));
        verify(periodBusiness).getPeriod(eq("period2"));
    }

    @Test
    public void testGetPeriod() {
        setAdminSubject();
        final PeriodServiceLocal testee = getInstance(PeriodServiceLocal.class);

        final Period expected = new Period("1", "name", "clazz", Arrays.asList("branchPeriod1", "periodPeriod2"));
        when(periodBusiness.getPeriod("1")).thenReturn(expected);

        assertNull(testee.getPeriod(null));
        verify(periodBusiness, never()).getPeriod(anyString());

        assertNull(testee.getPeriod("2"));
        verify(periodBusiness).getPeriod(eq("2"));

        assertEquals(expected, testee.getPeriod("1"));
        verify(periodBusiness).getPeriod(eq("1"));
    }
}
