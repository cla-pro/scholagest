package net.scholagest.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.BranchPeriod;

import org.junit.Test;

/**
 * Test class for {@link BranchPeriodEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class BranchPeriodEntityConverterTest {
    @Test
    public void testConvertToBranchPeriodList() {
        final BranchPeriodEntity clazzEntity1 = createBranchPeriodEntity(1L);
        final BranchPeriodEntity clazzEntity2 = createBranchPeriodEntity(2L);
        final List<BranchPeriodEntity> toConvert = Arrays.asList(clazzEntity1, clazzEntity2);

        final BranchPeriodEntityConverter testee = spy(new BranchPeriodEntityConverter());
        final List<BranchPeriod> converted = testee.convertToBranchPeriodList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final BranchPeriodEntity clazzEntity : toConvert) {
            verify(testee).convertToBranchPeriod(eq(clazzEntity));
        }
    }

    @Test
    public void testConvertToBranchPeriod() {
        final BranchPeriodEntity branchPeriodEntity = createBranchPeriodEntity(1L);

        final BranchPeriodEntityConverter testee = spy(new BranchPeriodEntityConverter());
        final BranchPeriod converted = testee.convertToBranchPeriod(branchPeriodEntity);

        assertEquals(branchPeriodEntity.getId().toString(), converted.getId());
        assertEquals("" + branchPeriodEntity.getBranch().getId(), converted.getBranch());
        assertEquals("" + branchPeriodEntity.getPeriod().getId(), converted.getPeriod());

        for (final StudentResultEntity studentResultEntity : branchPeriodEntity.getStudentResults()) {
            assertTrue(converted.getStudentResults().contains("" + studentResultEntity.getId()));
        }

        for (final ExamEntity examEntity : branchPeriodEntity.getExams()) {
            assertTrue(converted.getExams().contains("" + examEntity.getId()));
        }
    }

    @Test
    public void testConvertToBranchPeriodEntity() {
        final BranchPeriod clazz = new BranchPeriod("3", "branch", "period", Arrays.asList("1", "2"), "meanExam", Arrays.asList("3", "4"));

        final BranchPeriodEntityConverter testee = spy(new BranchPeriodEntityConverter());
        final BranchPeriodEntity converted = testee.convertToBranchPeriodEntity(clazz);

        assertNull(converted.getId());
        assertNull(converted.getBranch());
        assertNull(converted.getPeriod());
        assertTrue(converted.getStudentResults().isEmpty());
        assertTrue(converted.getExams().isEmpty());
    }

    private BranchPeriodEntity createBranchPeriodEntity(final long id) {
        final BranchPeriodEntity clazzEntity = new BranchPeriodEntity();
        ReflectionUtils.setField(clazzEntity, "id", Long.valueOf(id));
        clazzEntity.setBranch(createSimpleBranchEntity(2L));
        clazzEntity.setPeriod(createSimplePeriodEntity(3L));
        clazzEntity.setStudentResults(Arrays.asList(createSimpleStudentResultEntity(4L), createSimpleStudentResultEntity(5L)));
        clazzEntity.setExams(Arrays.asList(createSimpleExamEntity(4L), createSimpleExamEntity(5L)));

        return clazzEntity;
    }

    private PeriodEntity createSimplePeriodEntity(final long id) {
        final PeriodEntity periodEntity = new PeriodEntity();
        ReflectionUtils.setField(periodEntity, "id", Long.valueOf(id));

        return periodEntity;
    }

    private BranchEntity createSimpleBranchEntity(final long id) {
        final BranchEntity branchEntity = new BranchEntity();
        ReflectionUtils.setField(branchEntity, "id", Long.valueOf(id));

        return branchEntity;
    }

    private StudentResultEntity createSimpleStudentResultEntity(final long id) {
        final StudentResultEntity studentResultEntity = new StudentResultEntity();
        ReflectionUtils.setField(studentResultEntity, "id", Long.valueOf(id));

        return studentResultEntity;
    }

    private ExamEntity createSimpleExamEntity(final long id) {
        final ExamEntity examEntity = new ExamEntity();
        ReflectionUtils.setField(examEntity, "id", Long.valueOf(id));

        return examEntity;
    }
}
