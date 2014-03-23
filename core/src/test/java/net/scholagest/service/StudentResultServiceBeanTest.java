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

import net.scholagest.business.StudentResultBusinessLocal;
import net.scholagest.object.StudentResult;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link StudentResultServiceBean}
 * 
 * @author CLA
 * @since 0.14.0
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentResultServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private StudentResultBusinessLocal studentResultBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(StudentResultBusinessLocal.class).toInstance(studentResultBusiness);
        module.bind(StudentResultServiceLocal.class).to(StudentResultServiceBean.class);
    }

    @Test
    public void testGetStudentResults() {
        setAdminSubject();
        final StudentResultServiceLocal testee = getInstance(StudentResultServiceLocal.class);

        final StudentResult expected = new StudentResult("1", "student", "branchPeriod", new ArrayList<String>(), null, true);
        when(studentResultBusiness.getStudentResult("1")).thenReturn(expected);

        assertTrue(testee.getStudentResults(new ArrayList<String>()).isEmpty());
        verify(studentResultBusiness, never()).getStudentResult(anyString());

        assertEquals(Arrays.asList(expected), testee.getStudentResults(Arrays.asList("1", "2")));
        verify(studentResultBusiness).getStudentResult(eq("1"));
        verify(studentResultBusiness).getStudentResult(eq("2"));
    }

    @Test
    public void testGetStudentResult() {
        setAdminSubject();
        final StudentResultServiceLocal testee = getInstance(StudentResultServiceLocal.class);

        final StudentResult expected = new StudentResult("1", "student", "branchPeriod", new ArrayList<String>(), null, true);
        when(studentResultBusiness.getStudentResult("1")).thenReturn(expected);

        assertNull(testee.getStudentResult(null));
        verify(studentResultBusiness, never()).getStudentResult(anyString());

        assertNull(testee.getStudentResult("2"));
        verify(studentResultBusiness).getStudentResult(eq("2"));

        assertEquals(expected, testee.getStudentResult("1"));
        verify(studentResultBusiness).getStudentResult(eq("1"));
    }
}
