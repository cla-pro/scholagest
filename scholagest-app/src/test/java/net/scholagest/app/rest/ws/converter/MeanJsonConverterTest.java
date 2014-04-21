package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.ResultJson;
import net.scholagest.object.Mean;

import org.junit.Test;

/**
 * Test class for {@link MeanJsonConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class MeanJsonConverterTest {
    @Test
    public void testConvertToMeanJsonList() {
        final Mean mean1 = new Mean("result1", "2.5", "studentResult1");
        final Mean mean2 = new Mean("result2", "4.5", "studentResult2");
        final List<Mean> meanList = Arrays.asList(mean1, mean2);

        final MeanJsonConverter testee = spy(new MeanJsonConverter());
        final List<ResultJson> resultJsonList = testee.convertToMeanJsonList(meanList);

        assertEquals(meanList.size(), resultJsonList.size());
        for (final Mean mean : meanList) {
            verify(testee).convertToMeanJson(eq(mean));
        }
    }

    @Test
    public void testConvertToMeanJson() {
        final Mean mean = new Mean("result1", "2.5", "studentResult1");
        final ResultJson resultJson = new MeanJsonConverter().convertToMeanJson(mean);

        assertEquals(mean.getId(), resultJson.getId());
        assertEquals(mean.getGrade(), resultJson.getGrade());
        assertNull(resultJson.getExam());
        assertEquals(mean.getStudentResult(), resultJson.getStudentResult());
    }

    @Test
    public void testConvertToMean() {
        final ResultJson resultJson = new ResultJson("result1", "2.5", "exam1", "studentResult1");
        final Mean mean = new MeanJsonConverter().convertToMean(resultJson);

        assertEquals(resultJson.getId(), mean.getId());
        assertEquals(resultJson.getGrade(), mean.getGrade());
        assertEquals(resultJson.getStudentResult(), mean.getStudentResult());
    }
}
