package net.scholagest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.scholagest.business.ResultBusinessLocal;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.Result;
import net.scholagest.utils.AbstractGuiceContextTest;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link ResultServiceBean}
 * 
 * @author CLA
 * @since 0.14.0
 */
@RunWith(MockitoJUnitRunner.class)
public class ResultServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private ResultBusinessLocal resultBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(ResultBusinessLocal.class).toInstance(resultBusiness);
        module.bind(ResultServiceLocal.class).to(ResultServiceBean.class);
    }

    @Test
    public void testGetResults() {
        setAdminSubject();
        final ResultServiceLocal testee = getInstance(ResultServiceLocal.class);

        final Result result1 = new Result("result1", 2.5, "exam1", "studentResult1");
        final Result result2 = new Result("result2", 4.5, "exam2", "studentResult2");
        final List<Result> expected = Arrays.asList(result1, result2);
        when(resultBusiness.getResult("result1")).thenReturn(result1);
        when(resultBusiness.getResult("result2")).thenReturn(result2);

        assertTrue(testee.getResults(new ArrayList<String>()).isEmpty());
        verify(resultBusiness, never()).getResult(anyString());

        assertTrue(testee.getResults(Arrays.asList("result3")).isEmpty());
        verify(resultBusiness).getResult(eq("result3"));

        final List<Result> result = testee.getResults(Arrays.asList("result1", "result2"));

        assertEquals(expected, result);
        verify(resultBusiness).getResult(eq("result1"));
        verify(resultBusiness).getResult(eq("result2"));
    }

    @Test
    public void testGetResult() {
        setAdminSubject();
        final ResultServiceLocal testee = getInstance(ResultServiceLocal.class);

        final Result expected = new Result("result1", 2.5, "exam1", "studentResult1");
        when(resultBusiness.getResult("result1")).thenReturn(expected);

        assertNull(testee.getResult(null));
        verify(resultBusiness, never()).getResult(anyString());

        assertNull(testee.getResult("result2"));
        verify(resultBusiness).getResult(eq("result2"));

        assertEquals(expected, testee.getResult("result1"));
        verify(resultBusiness).getResult(eq("result1"));
    }

    @Test
    public void testSaveResult() {
        setAdminSubject();
        final ResultServiceLocal testee = getInstance(ResultServiceLocal.class);

        final Result saved = new Result("result1", 2.5, "exam1", "studentResult1");
        when(resultBusiness.saveResult(any(Result.class))).thenReturn(saved);

        final Result toSave = new Result();
        assertNull(testee.saveResult(null));
        verify(resultBusiness, never()).saveResult(any(Result.class));

        assertEquals(saved, testee.saveResult(toSave));
        verify(resultBusiness).saveResult(eq(toSave));
    }

    @Test
    public void testSaveResultAuthorization() {
        setNoRightSubject();
        final ResultServiceLocal testee = getInstance(ResultServiceLocal.class);

        try {
            testee.saveResult(new Result());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
        }
    }
}
