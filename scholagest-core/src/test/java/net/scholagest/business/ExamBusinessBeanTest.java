package net.scholagest.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.scholagest.ReflectionUtils;
import net.scholagest.dao.ExamDaoLocal;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.object.Exam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link ExamBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class ExamBusinessBeanTest {
    @Mock
    private ExamDaoLocal examDao;

    @InjectMocks
    private final ExamBusinessLocal testee = new ExamBusinessBean();

    @Test
    public void testGetExam() {
        final long id = 1;
        final ExamEntity examEntity = createExamEntity(1L, "name", 3);
        when(examDao.getExamEntityById(eq(id))).thenReturn(examEntity);

        assertNull(testee.getExam(2L));
        verify(examDao).getExamEntityById(eq(2L));

        assertNotNull(testee.getExam(id));
        verify(examDao).getExamEntityById(eq(id));
    }

    @Test
    public void testCreateExam() {
        final Exam exam = new Exam("2", "name", 3, null);
        final ExamEntity examEntityMock = createExamEntity(2L, "name", 3);

        when(examDao.persistExamEntity(any(ExamEntity.class))).thenReturn(examEntityMock);

        final Exam result = testee.createExam(exam);

        assertEquals(exam, result);
        final ArgumentCaptor<ExamEntity> teacherCaptor = ArgumentCaptor.forClass(ExamEntity.class);
        verify(examDao).persistExamEntity(teacherCaptor.capture());
        assertNull(teacherCaptor.getValue().getId());
    }

    @Test
    public void testSaveExam() {
        final Exam exam = new Exam("2", "name2", 4, "branchPeriod");
        final ExamEntity examEntityMock = createExamEntity(2L, "name", 3);

        when(examDao.getExamEntityById(eq(2L))).thenReturn(examEntityMock);

        final Exam result = testee.saveExam(exam);

        assertEquals(exam.getName(), result.getName());
        assertEquals(exam.getCoeff(), result.getCoeff());
    }

    private ExamEntity createExamEntity(final Long id, final String name, final int coeff) {
        final ExamEntity examEntity = new ExamEntity();
        ReflectionUtils.setField(examEntity, "id", id);
        examEntity.setName(name);
        examEntity.setCoeff(coeff);
        examEntity.setBranchPeriod(new BranchPeriodEntity());

        return examEntity;
    }
}
