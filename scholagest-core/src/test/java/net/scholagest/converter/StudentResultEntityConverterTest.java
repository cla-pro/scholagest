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
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.MeanEntity;
import net.scholagest.db.entity.ResultEntity;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.StudentResult;

import org.junit.Test;

/**
 * Test class for {@link StudentResultEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class StudentResultEntityConverterTest {
    @Test
    public void testConvertToStudentResultList() {
        final StudentResultEntity studentResultEntity1 = createStudentResultEntity(1L, true);
        final StudentResultEntity studentResultEntity2 = createStudentResultEntity(2L, false);
        final List<StudentResultEntity> toConvert = Arrays.asList(studentResultEntity1, studentResultEntity2);

        final StudentResultEntityConverter testee = spy(new StudentResultEntityConverter());
        final List<StudentResult> converted = testee.convertToStudentResultList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final StudentResultEntity studentResultEntity : toConvert) {
            verify(testee).convertToStudentResult(eq(studentResultEntity));
        }
    }

    @Test
    public void testConvertToStudentResult() {
        final StudentResultEntity studentResultEntity = createStudentResultEntity(1L, true);

        final StudentResultEntityConverter testee = spy(new StudentResultEntityConverter());
        final StudentResult converted = testee.convertToStudentResult(studentResultEntity);

        assertEquals(studentResultEntity.getId().toString(), converted.getId());
        assertEquals(studentResultEntity.isActive(), converted.isActive());
        assertEquals("" + studentResultEntity.getBranchPeriod().getId(), converted.getBranchPeriod());
        assertEquals("" + studentResultEntity.getMean().getId(), converted.getMean());
        assertEquals("" + studentResultEntity.getStudent().getId(), converted.getStudent());

        for (final ResultEntity resultEntity : studentResultEntity.getResults()) {
            assertTrue(converted.getResults().contains("" + resultEntity.getId()));
        }
    }

    @Test
    public void testConvertToStudentResultEntity() {
        final StudentResult studentResult = new StudentResult("3", "student", "branchPeriod", Arrays.asList("1", "2"), "mean", true);

        final StudentResultEntityConverter testee = spy(new StudentResultEntityConverter());
        final StudentResultEntity converted = testee.convertToStudentResultEntity(studentResult);

        assertNull(converted.getId());
        assertNull(converted.getBranchPeriod());
        assertNull(converted.getMean());
        assertTrue(converted.getResults().isEmpty());
        assertNull(converted.getStudent());
    }

    private StudentResultEntity createStudentResultEntity(final long id, final boolean active) {
        final StudentResultEntity studentResultEntity = new StudentResultEntity();
        ReflectionUtils.setField(studentResultEntity, "id", Long.valueOf(id));
        studentResultEntity.setActive(active);
        studentResultEntity.setBranchPeriod(createSimpleBranchPeriodEntity(2L));
        studentResultEntity.setStudent(createSimpleStudentEntity(3L));
        studentResultEntity.setResults(Arrays.asList(createSimpleResultEntity(4L), createSimpleResultEntity(5L)));
        studentResultEntity.setMean(createSimpleMeanEntity(5L));

        return studentResultEntity;
    }

    private MeanEntity createSimpleMeanEntity(final long id) {
        final MeanEntity meanEntity = new MeanEntity();
        ReflectionUtils.setField(meanEntity, "id", Long.valueOf(id));

        return meanEntity;
    }

    private BranchPeriodEntity createSimpleBranchPeriodEntity(final long id) {
        final BranchPeriodEntity branchPeriodEntity = new BranchPeriodEntity();
        ReflectionUtils.setField(branchPeriodEntity, "id", Long.valueOf(id));

        return branchPeriodEntity;
    }

    private StudentEntity createSimpleStudentEntity(final long id) {
        final StudentEntity studentEntity = new StudentEntity();
        ReflectionUtils.setField(studentEntity, "id", Long.valueOf(id));

        return studentEntity;
    }

    private ResultEntity createSimpleResultEntity(final long id) {
        final ResultEntity resultEntity = new ResultEntity();
        ReflectionUtils.setField(resultEntity, "id", Long.valueOf(id));

        return resultEntity;
    }
}
