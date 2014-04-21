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
import java.util.List;

import net.scholagest.business.MeanBusinessLocal;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.Mean;
import net.scholagest.utils.AbstractGuiceContextTest;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link MeanServiceBean}
 * 
 * @author CLA
 * @since 0.14.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MeanServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private MeanBusinessLocal meanBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(MeanBusinessLocal.class).toInstance(meanBusiness);
        module.bind(MeanServiceLocal.class).to(MeanServiceBean.class);
    }

    @Test
    public void testGetMeans() {
        setAdminSubject();
        final MeanServiceLocal testee = getInstance(MeanServiceLocal.class);

        final Mean mean1 = new Mean("1", "2.5", "studentResult1");
        final Mean mean2 = new Mean("2", "4.5", "studentResult2");
        final List<Mean> expected = Arrays.asList(mean1, mean2);
        when(meanBusiness.getMean(1L)).thenReturn(mean1);
        when(meanBusiness.getMean(2L)).thenReturn(mean2);

        assertTrue(testee.getMeans(new ArrayList<String>()).isEmpty());
        verify(meanBusiness, never()).getMean(anyLong());

        assertTrue(testee.getMeans(Arrays.asList("3")).isEmpty());
        verify(meanBusiness).getMean(eq(3L));

        final List<Mean> mean = testee.getMeans(Arrays.asList("1", "2"));

        assertEquals(expected, mean);
        verify(meanBusiness).getMean(eq(1L));
        verify(meanBusiness).getMean(eq(2L));
    }

    @Test
    public void testGetMean() {
        setAdminSubject();
        final MeanServiceLocal testee = getInstance(MeanServiceLocal.class);

        final Mean expected = new Mean("1", "2.5", "studentResult1");
        when(meanBusiness.getMean(1L)).thenReturn(expected);

        assertNull(testee.getMean(null));
        verify(meanBusiness, never()).getMean(anyLong());

        assertNull(testee.getMean("2"));
        verify(meanBusiness).getMean(eq(2L));

        assertEquals(expected, testee.getMean("1"));
        verify(meanBusiness).getMean(eq(1L));
    }

    @Test
    public void testSaveMean() {
        setAdminSubject();
        final MeanServiceLocal testee = getInstance(MeanServiceLocal.class);

        final Mean saved = new Mean("1", "2.5", "studentResult1");
        when(meanBusiness.saveMean(any(Mean.class))).thenReturn(saved);

        final Mean toSave = new Mean();
        assertNull(testee.saveMean(null));
        verify(meanBusiness, never()).saveMean(any(Mean.class));

        assertEquals(saved, testee.saveMean(toSave));
        verify(meanBusiness).saveMean(eq(toSave));
    }

    @Test
    public void testSaveMeanAuthorization() {
        setNoRightSubject();
        final MeanServiceLocal testee = getInstance(MeanServiceLocal.class);

        try {
            testee.saveMean(new Mean());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
        }
    }
}
