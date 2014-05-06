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

import net.scholagest.business.ClazzBusinessLocal;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.Clazz;
import net.scholagest.utils.AbstractGuiceContextTest;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link ClazzServiceBean}
 * 
 * @author CLA
 * @since 0.14.0
 */
@RunWith(MockitoJUnitRunner.class)
public class ClazzServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private ClazzBusinessLocal clazzBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(ClazzBusinessLocal.class).toInstance(clazzBusiness);
        module.bind(ClazzServiceLocal.class).to(ClazzServiceBean.class);
    }

    @Test
    public void testGetClasses() {
        setAdminSubject();
        final ClazzServiceLocal testee = getInstance(ClazzServiceLocal.class);

        final Clazz clazz1 = new Clazz("1", "name1", "year1", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
        final Clazz clazz2 = new Clazz("2", "name2", "year2", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
        final List<Clazz> expected = Arrays.asList(clazz1, clazz2);
        when(clazzBusiness.getClazz(1L)).thenReturn(clazz1);
        when(clazzBusiness.getClazz(2L)).thenReturn(clazz2);

        assertTrue(testee.getClasses(new ArrayList<String>()).isEmpty());
        verify(clazzBusiness, never()).getClazz(anyLong());

        assertTrue(testee.getClasses(Arrays.asList("3")).isEmpty());
        verify(clazzBusiness).getClazz(eq(3L));

        final List<Clazz> result = testee.getClasses(Arrays.asList("1", "2"));

        assertEquals(expected, result);
        verify(clazzBusiness).getClazz(eq(1L));
        verify(clazzBusiness).getClazz(eq(2L));
    }

    @Test
    public void testGetClazz() {
        setAdminSubject();
        final ClazzServiceLocal testee = getInstance(ClazzServiceLocal.class);

        final Clazz expected = new Clazz("1", "name", "year", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
        when(clazzBusiness.getClazz(1L)).thenReturn(expected);

        assertNull(testee.getClazz(null));
        verify(clazzBusiness, never()).getClazz(anyLong());

        assertNull(testee.getClazz("2"));
        verify(clazzBusiness).getClazz(eq(2L));

        assertEquals(expected, testee.getClazz("1"));
        verify(clazzBusiness).getClazz(eq(1L));
    }

    @Test
    public void testCreateClazz() {
        setAdminSubject();
        final ClazzServiceLocal testee = getInstance(ClazzServiceLocal.class);

        final Clazz created = new Clazz("1", "name", "year", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
        when(clazzBusiness.createClazz(any(Clazz.class))).thenReturn(created);

        assertNull(testee.createClazz(null));
        verify(clazzBusiness, never()).createClazz(any(Clazz.class));

        final Clazz toCreate = new Clazz();
        assertEquals(created, testee.createClazz(toCreate));
        verify(clazzBusiness).createClazz(eq(toCreate));
    }

    @Test
    public void testSaveClazz() {
        setAdminSubject();
        final ClazzServiceLocal testee = getInstance(ClazzServiceLocal.class);

        final Clazz saved = new Clazz("1", "name", "year", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
        when(clazzBusiness.saveClazz(any(Clazz.class))).thenReturn(saved);

        final Clazz toSave = new Clazz();
        assertNull(testee.saveClazz(null));
        verify(clazzBusiness, never()).saveClazz(any(Clazz.class));

        assertEquals(saved, testee.saveClazz(toSave));
        verify(clazzBusiness).saveClazz(eq(toSave));
    }

    @Test
    public void testCreateClazzAuthorization() {
        setNoRightSubject();
        final ClazzServiceLocal testee = getInstance(ClazzServiceLocal.class);

        try {
            testee.createClazz(new Clazz());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
        }
    }

    @Test
    public void testSaveClazzAuthorization() {
        setNoRightSubject();
        final ClazzServiceLocal testee = getInstance(ClazzServiceLocal.class);

        try {
            testee.saveClazz(new Clazz());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
        }
    }
}
