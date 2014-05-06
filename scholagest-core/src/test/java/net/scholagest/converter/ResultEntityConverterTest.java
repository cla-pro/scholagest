package net.scholagest.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.db.entity.ResultEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.Result;

import org.junit.Test;

/**
 * Test class for {@link ResultEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ResultEntityConverterTest {
    @Test
    public void testConvertToResultList() {
        final ResultEntity branchEntity1 = createResultEntity(1L, "grade1");
        final ResultEntity branchEntity2 = createResultEntity(2L, "grade2");
        final List<ResultEntity> toConvert = Arrays.asList(branchEntity1, branchEntity2);

        final ResultEntityConverter testee = spy(new ResultEntityConverter());
        final List<Result> converted = testee.convertToResultList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final ResultEntity branchEntity : toConvert) {
            verify(testee).convertToResult(eq(branchEntity));
        }
    }

    @Test
    public void testConvertToResult() {
        final ResultEntity examEntity = createResultEntity(1L, "grade");

        final ResultEntityConverter testee = spy(new ResultEntityConverter());
        final Result converted = testee.convertToResult(examEntity);

        assertEquals(examEntity.getId().toString(), converted.getId());
        assertEquals(examEntity.getGrade(), converted.getGrade());
        assertEquals("" + examEntity.getStudentResult().getId(), converted.getStudentResult());
        assertEquals("" + examEntity.getExam().getId(), converted.getExam());
    }

    @Test
    public void testConvertToResultEntity() {
        final Result result = new Result("3", "grade", "exam", "studentResult");

        final ResultEntityConverter testee = spy(new ResultEntityConverter());
        final ResultEntity converted = testee.convertToResultEntity(result);

        assertNull(converted.getId());
        assertEquals(result.getGrade(), converted.getGrade());
        assertNull(converted.getStudentResult());
    }

    private ResultEntity createResultEntity(final long id, final String grade) {
        final ResultEntity studentResultEntity = new ResultEntity();
        ReflectionUtils.setField(studentResultEntity, "id", Long.valueOf(id));
        studentResultEntity.setGrade(grade);
        studentResultEntity.setStudentResult(createSimpleStudentResultEntity(3L));
        studentResultEntity.setExam(createSimpleExamEntity(3L));

        return studentResultEntity;
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
