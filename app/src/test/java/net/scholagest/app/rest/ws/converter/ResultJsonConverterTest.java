package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.ResultJson;
import net.scholagest.object.Result;

import org.junit.Test;

/**
 * Test class for {@link ResultJsonConverter}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ResultJsonConverterTest {
    @Test
    public void testConvertToResultJsonList() {
        final Result result1 = new Result("result1", 2.5, "exam1", "studentResult1");
        final Result result2 = new Result("result2", 4.5, "exam2", "studentResult2");
        final List<Result> resultList = Arrays.asList(result1, result2);

        final ResultJsonConverter testee = spy(new ResultJsonConverter());
        final List<ResultJson> resultJsonList = testee.convertToResultJsonList(resultList);

        assertEquals(resultList.size(), resultJsonList.size());
        for (final Result result : resultList) {
            verify(testee).convertToResultJson(eq(result));
        }
    }

    @Test
    public void testConvertToResultJson() {
        final Result result = new Result("result1", 2.5, "exam1", "studentResult1");
        final ResultJson resultJson = new ResultJsonConverter().convertToResultJson(result);

        assertEquals(result.getId(), resultJson.getId());
        assertEquals(result.getGrade(), resultJson.getGrade());
        assertEquals(result.getExam(), resultJson.getExam());
        assertEquals(result.getStudentResult(), resultJson.getStudentResult());
    }

    @Test
    public void testConvertToResult() {
        final ResultJson resultJson = new ResultJson("result1", 2.5, "exam1", "studentResult1");
        final Result result = new ResultJsonConverter().convertToResult(resultJson);

        assertEquals(resultJson.getId(), result.getId());
        assertEquals(resultJson.getGrade(), result.getGrade());
        assertEquals(resultJson.getExam(), result.getExam());
        assertEquals(resultJson.getStudentResult(), result.getStudentResult());
    }
}
