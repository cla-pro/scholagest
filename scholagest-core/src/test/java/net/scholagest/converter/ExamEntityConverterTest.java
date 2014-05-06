package net.scholagest.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.object.Exam;

import org.junit.Test;

/**
 * Test class for {@link ExamEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ExamEntityConverterTest {
    @Test
    public void testConvertToExamList() {
        final ExamEntity branchEntity1 = createExamEntity(1L, "name1", 3);
        final ExamEntity branchEntity2 = createExamEntity(2L, "name2", 4);
        final List<ExamEntity> toConvert = Arrays.asList(branchEntity1, branchEntity2);

        final ExamEntityConverter testee = spy(new ExamEntityConverter());
        final List<Exam> converted = testee.convertToExamList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final ExamEntity branchEntity : toConvert) {
            verify(testee).convertToExam(eq(branchEntity));
        }
    }

    @Test
    public void testConvertToExam() {
        final ExamEntity branchEntity = createExamEntity(1L, "name", 3);

        final ExamEntityConverter testee = spy(new ExamEntityConverter());
        final Exam converted = testee.convertToExam(branchEntity);

        assertEquals(branchEntity.getId().toString(), converted.getId());
        assertEquals(branchEntity.getName(), converted.getName());
        assertEquals(branchEntity.getCoeff(), converted.getCoeff());
        assertEquals("" + branchEntity.getBranchPeriod().getId(), converted.getBranchPeriod());
    }

    @Test
    public void testConvertToExamEntity() {
        final Exam exam = new Exam("3", "name", 3, "1");

        final ExamEntityConverter testee = spy(new ExamEntityConverter());
        final ExamEntity converted = testee.convertToExamEntity(exam);

        assertNull(converted.getId());
        assertEquals(exam.getName(), converted.getName());
        assertEquals(exam.getCoeff(), converted.getCoeff());
        assertNull(converted.getBranchPeriod());
    }

    private ExamEntity createExamEntity(final long id, final String name, final int coeff) {
        final ExamEntity examEntity = new ExamEntity();
        ReflectionUtils.setField(examEntity, "id", Long.valueOf(id));
        examEntity.setName(name);
        examEntity.setCoeff(coeff);
        examEntity.setBranchPeriod(createSimpleBranchPeriodEntity(5L));

        return examEntity;
    }

    private BranchPeriodEntity createSimpleBranchPeriodEntity(final long id) {
        final BranchPeriodEntity branchPeriodEntity = new BranchPeriodEntity();
        ReflectionUtils.setField(branchPeriodEntity, "id", Long.valueOf(id));

        return branchPeriodEntity;
    }
}
