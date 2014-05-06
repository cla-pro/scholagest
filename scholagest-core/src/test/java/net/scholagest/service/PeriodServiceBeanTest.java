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

        final Period period1 = new Period("1", "name1", "clazz1", Arrays.asList("branchPeriod1"));
        final Period period2 = new Period("", "name2", "clazz2", Arrays.asList("branchPeriod2"));
        final List<Period> expected = Arrays.asList(period1, period2);
        when(periodBusiness.getPeriod(1L)).thenReturn(period1);
        when(periodBusiness.getPeriod(2L)).thenReturn(period2);

        assertTrue(testee.getPeriods(new ArrayList<String>()).isEmpty());
        verify(periodBusiness, never()).getPeriod(anyLong());

        assertTrue(testee.getPeriods(Arrays.asList("3")).isEmpty());
        verify(periodBusiness).getPeriod(eq(3L));

        final List<Period> result = testee.getPeriods(Arrays.asList("1", "2"));

        assertEquals(expected, result);
        verify(periodBusiness).getPeriod(eq(1L));
        verify(periodBusiness).getPeriod(eq(2L));
    }

    @Test
    public void testGetPeriod() {
        setAdminSubject();
        final PeriodServiceLocal testee = getInstance(PeriodServiceLocal.class);

        final Period expected = new Period("1", "name", "clazz", Arrays.asList("branchPeriod1", "periodPeriod2"));
        when(periodBusiness.getPeriod(1L)).thenReturn(expected);

        assertNull(testee.getPeriod(null));
        verify(periodBusiness, never()).getPeriod(anyLong());

        assertNull(testee.getPeriod("2"));
        verify(periodBusiness).getPeriod(eq(2L));

        assertEquals(expected, testee.getPeriod("1"));
        verify(periodBusiness).getPeriod(eq(1L));
    }
}
