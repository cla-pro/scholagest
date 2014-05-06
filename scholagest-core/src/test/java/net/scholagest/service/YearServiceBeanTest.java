package net.scholagest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import net.scholagest.business.YearBusinessLocal;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.Year;
import net.scholagest.utils.AbstractGuiceContextTest;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link YearServiceBean}
 * 
 * @author CLA
 * @since 0.14.0
 */
@RunWith(MockitoJUnitRunner.class)
public class YearServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private YearBusinessLocal yearBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(YearBusinessLocal.class).toInstance(yearBusiness);
        module.bind(YearServiceLocal.class).to(YearServiceBean.class);
    }

    @Test
    public void testGetYears() {
        setAdminSubject();
        final YearServiceLocal testee = getInstance(YearServiceLocal.class);

        final Year year1 = new Year("year1", "name1", false, Arrays.asList("class1", "class2"));
        final Year year2 = new Year("year2", "name2", true, Arrays.asList("class3", "class4"));
        final List<Year> expected = Arrays.asList(year1, year2);
        when(yearBusiness.getYears()).thenReturn(expected);

        final List<Year> result = testee.getYears();

        assertEquals(expected, result);

        verify(yearBusiness).getYears();
    }

    @Test
    public void testGetYear() {
        setAdminSubject();
        final YearServiceLocal testee = getInstance(YearServiceLocal.class);

        final Year expected = new Year("1", "name", true, Arrays.asList("class1", "class2"));
        when(yearBusiness.getYear(1L)).thenReturn(expected);

        assertNull(testee.getYear(null));
        verify(yearBusiness, never()).getYear(anyLong());

        assertNull(testee.getYear("2"));
        verify(yearBusiness).getYear(eq(2L));

        assertEquals(expected, testee.getYear("1"));
        verify(yearBusiness).getYear(eq(1L));
    }

    @Test
    public void testCreateYear() {
        setAdminSubject();
        final YearServiceLocal testee = getInstance(YearServiceLocal.class);

        final Year created = new Year("1", "name", false, Arrays.asList("class1", "class2"));
        when(yearBusiness.createYear(any(Year.class))).thenReturn(created);

        assertNull(testee.createYear(null));
        verify(yearBusiness, never()).createYear(any(Year.class));

        final Year toCreate = new Year();
        assertEquals(created, testee.createYear(toCreate));
        verify(yearBusiness).createYear(eq(toCreate));
    }

    @Test
    public void testSaveYear() {
        setAdminSubject();
        final YearServiceLocal testee = getInstance(YearServiceLocal.class);

        final Year saved = new Year("year1", "name", true, Arrays.asList("class1", "class2"));
        when(yearBusiness.saveYear(any(Year.class))).thenReturn(saved);

        final Year toSave = new Year();
        assertNull(testee.saveYear(null));
        verify(yearBusiness, never()).saveYear(any(Year.class));

        assertEquals(saved, testee.saveYear(toSave));
        verify(yearBusiness).saveYear(eq(toSave));
    }

    @Test
    public void testCreateYearAuthorization() {
        setNoRightSubject();
        final YearServiceLocal testee = getInstance(YearServiceLocal.class);

        try {
            testee.createYear(new Year());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
        }
    }

    @Test
    public void testSaveYearAuthorization() {
        setNoRightSubject();
        final YearServiceLocal testee = getInstance(YearServiceLocal.class);

        try {
            testee.saveYear(new Year());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
        }
    }
}
