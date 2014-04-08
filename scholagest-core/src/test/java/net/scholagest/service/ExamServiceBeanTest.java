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

import net.scholagest.business.ExamBusinessLocal;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.Exam;
import net.scholagest.utils.AbstractGuiceContextTest;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link ExamServiceBean}
 * 
 * @author CLA
 * @since 0.14.0
 */
@RunWith(MockitoJUnitRunner.class)
public class ExamServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private ExamBusinessLocal examBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(ExamBusinessLocal.class).toInstance(examBusiness);
        module.bind(ExamServiceLocal.class).to(ExamServiceBean.class);
    }

    @Test
    public void testGetExams() {
        setAdminSubject();
        final ExamServiceLocal testee = getInstance(ExamServiceLocal.class);

        final Exam expected = new Exam("1", "name", 2, "branchPeriod");
        when(examBusiness.getExam("1")).thenReturn(expected);

        assertTrue(testee.getExams(new ArrayList<String>()).isEmpty());
        verify(examBusiness, never()).getExam(anyString());

        assertEquals(Arrays.asList(expected), testee.getExams(Arrays.asList("1", "2")));
        verify(examBusiness).getExam(eq("1"));
        verify(examBusiness).getExam(eq("2"));
    }

    @Test
    public void testGetExam() {
        setAdminSubject();
        final ExamServiceLocal testee = getInstance(ExamServiceLocal.class);

        final Exam expected = new Exam("1", "name", 2, "branchPeriod");
        when(examBusiness.getExam("1")).thenReturn(expected);

        assertNull(testee.getExam(null));
        verify(examBusiness, never()).getExam(anyString());

        assertNull(testee.getExam("2"));
        verify(examBusiness).getExam(eq("2"));

        assertEquals(expected, testee.getExam("1"));
        verify(examBusiness).getExam(eq("1"));
    }

    @Test
    public void testCreateExam() {
        setAdminSubject();
        final ExamServiceLocal testee = getInstance(ExamServiceLocal.class);

        final Exam created = new Exam("1", "name", 2, "branchPeriod");
        when(examBusiness.createExam(any(Exam.class))).thenReturn(created);

        assertNull(testee.createExam(null));
        verify(examBusiness, never()).createExam(any(Exam.class));

        final Exam toCreate = new Exam();
        assertEquals(created, testee.createExam(toCreate));
        verify(examBusiness).createExam(eq(toCreate));
    }

    @Test
    public void testSaveExam() {
        setAdminSubject();
        final ExamServiceLocal testee = getInstance(ExamServiceLocal.class);

        final Exam saved = new Exam("1", "name", 2, "branchPeriod");
        when(examBusiness.saveExam(any(Exam.class))).thenReturn(saved);

        final Exam toSave = new Exam();
        assertNull(testee.saveExam(null, toSave));
        assertNull(testee.saveExam("1", null));
        verify(examBusiness, never()).saveExam(any(Exam.class));

        assertEquals(saved, testee.saveExam("1", toSave));
        verify(examBusiness).saveExam(eq(toSave));
    }

    @Test
    public void testCreateExamAuthorization() {
        setNoRightSubject();
        final ExamServiceLocal testee = getInstance(ExamServiceLocal.class);

        try {
            testee.createExam(new Exam());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
        }
    }

    @Test
    public void testSaveExamAuthorization() {
        setNoRightSubject();
        final ExamServiceLocal testee = getInstance(ExamServiceLocal.class);

        final String id = "id";
        try {
            testee.saveExam(id, new Exam());
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(eq("ADMIN"));
            verify(ScholagestThreadLocal.getSubject()).isPermitted(id);
        }
    }
}
