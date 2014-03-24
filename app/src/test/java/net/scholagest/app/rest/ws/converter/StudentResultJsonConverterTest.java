package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.StudentResultJson;
import net.scholagest.object.StudentResult;

import org.junit.Test;

/**
 * Test class for {@link StudentResultJsonConverter}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class StudentResultJsonConverterTest {
    @Test
    public void testConvertToStudentResultJsonList() {
        final StudentResult studentResult1 = new StudentResult("1", "student1", "branchPeriod1", new ArrayList<String>(), "mean1", true);
        final StudentResult studentResult2 = new StudentResult("2", "student2", "branchPeriod2", new ArrayList<String>(), "mean2", false);
        final List<StudentResult> studentResultList = Arrays.asList(studentResult1, studentResult2);

        final StudentResultJsonConverter testee = spy(new StudentResultJsonConverter());
        final List<StudentResultJson> studentResultJsonList = testee.convertToStudentResultJsonList(studentResultList);

        assertEquals(studentResultList.size(), studentResultJsonList.size());
        for (final StudentResult studentResult : studentResultList) {
            verify(testee).convertToStudentResultJson(eq(studentResult));
        }
    }

    @Test
    public void testConvertToStudentResultJson() {
        final StudentResult branch = new StudentResult("1", "student1", "branchPeriod1", Arrays.asList("result1", "result2"), "mean1", true);
        final StudentResultJson branchJson = new StudentResultJsonConverter().convertToStudentResultJson(branch);

        assertEquals(branch.getId(), branchJson.getId());
        assertEquals(branch.getStudent(), branchJson.getStudent());
        assertEquals(branch.getBranchPeriod(), branchJson.getBranchPeriod());
        assertEquals(branch.isActive(), branchJson.isActive());
        assertEquals(branch.getResults(), branchJson.getResults());
        assertEquals(branch.getMean(), branchJson.getMean());
    }
}
