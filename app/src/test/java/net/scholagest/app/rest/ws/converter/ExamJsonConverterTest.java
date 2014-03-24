package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.ExamJson;
import net.scholagest.object.Exam;

import org.junit.Test;

/**
 * Test class for {@link ExamJsonConverter}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ExamJsonConverterTest {
    @Test
    public void testConvertToExamJsonList() {
        final Exam exam1 = new Exam("exam1", "name1", 2, "branchPeriod1");
        final Exam exam2 = new Exam("exam2", "name2", 3, "branchPeriod2");
        final List<Exam> examList = Arrays.asList(exam1, exam2);

        final ExamJsonConverter testee = spy(new ExamJsonConverter());
        final List<ExamJson> examJsonList = testee.convertToExamJsonList(examList);

        assertEquals(examList.size(), examJsonList.size());
        for (final Exam exam : examList) {
            verify(testee).convertToExamJson(eq(exam));
        }
    }

    @Test
    public void testConvertToExamJson() {
        final Exam exam = new Exam("exam1", "name1", 2, "branchPeriod1");
        final ExamJson examJson = new ExamJsonConverter().convertToExamJson(exam);

        assertEquals(exam.getId(), examJson.getId());
        assertEquals(exam.getName(), examJson.getName());
        assertEquals(exam.getCoeff(), examJson.getCoeff());
        assertEquals(exam.getBranchPeriod(), examJson.getBranchPeriod());
    }

    @Test
    public void testConvertToExam() {
        final ExamJson examJson = new ExamJson("exam1", "name1", 2, "branchPeriod1");
        final Exam exam = new ExamJsonConverter().convertToExam(examJson);

        assertEquals(examJson.getId(), exam.getId());
        assertEquals(examJson.getName(), exam.getName());
        assertEquals(examJson.getCoeff(), exam.getCoeff());
        assertEquals(examJson.getBranchPeriod(), exam.getBranchPeriod());
    }
}
