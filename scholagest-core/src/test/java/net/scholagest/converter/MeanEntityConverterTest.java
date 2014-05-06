package net.scholagest.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.db.entity.MeanEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.Mean;

import org.junit.Test;

/**
 * Test class for {@link MeanEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class MeanEntityConverterTest {
    @Test
    public void testConvertToMeanList() {
        final MeanEntity branchEntity1 = createMeanEntity(1L, "grade1");
        final MeanEntity branchEntity2 = createMeanEntity(2L, "grade2");
        final List<MeanEntity> toConvert = Arrays.asList(branchEntity1, branchEntity2);

        final MeanEntityConverter testee = spy(new MeanEntityConverter());
        final List<Mean> converted = testee.convertToMeanList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final MeanEntity branchEntity : toConvert) {
            verify(testee).convertToMean(eq(branchEntity));
        }
    }

    @Test
    public void testConvertToMean() {
        final MeanEntity meanEntity = createMeanEntity(1L, "grade");

        final MeanEntityConverter testee = spy(new MeanEntityConverter());
        final Mean converted = testee.convertToMean(meanEntity);

        assertEquals(meanEntity.getId().toString(), converted.getId());
        assertEquals(meanEntity.getGrade(), converted.getGrade());
        assertEquals("" + meanEntity.getStudentResult().getId(), converted.getStudentResult());
    }

    @Test
    public void testConvertToMeanEntity() {
        final Mean result = new Mean("3", "grade", "studentResult");

        final MeanEntityConverter testee = spy(new MeanEntityConverter());
        final MeanEntity converted = testee.convertToMeanEntity(result);

        assertNull(converted.getId());
        assertEquals(result.getGrade(), converted.getGrade());
        assertNull(converted.getStudentResult());
    }

    private MeanEntity createMeanEntity(final long id, final String grade) {
        final MeanEntity studentResultEntity = new MeanEntity();
        ReflectionUtils.setField(studentResultEntity, "id", Long.valueOf(id));
        studentResultEntity.setGrade(grade);
        studentResultEntity.setStudentResult(createSimpleStudentResultEntity(3L));

        return studentResultEntity;
    }

    private StudentResultEntity createSimpleStudentResultEntity(final long id) {
        final StudentResultEntity studentResultEntity = new StudentResultEntity();
        ReflectionUtils.setField(studentResultEntity, "id", Long.valueOf(id));

        return studentResultEntity;
    }
}
