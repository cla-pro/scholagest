package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.BranchPeriodJson;
import net.scholagest.object.BranchPeriod;

import org.junit.Test;

/**
 * Test class for {@link BranchPeriodJsonConverter}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchPeriodJsonConverterTest {
    @Test
    public void testConvertToBranchPeriodJsonList() {
        final BranchPeriod branchPeriod1 = new BranchPeriod("branch1", "branch1", "period1", Arrays.asList("exam1"), "exam3",
                Arrays.asList("studentResult1"));
        final BranchPeriod branchPeriod2 = new BranchPeriod("branch2", "branch2", "period2", Arrays.asList("exam2"), "exam4",
                Arrays.asList("studentResult2"));
        final List<BranchPeriod> branchList = Arrays.asList(branchPeriod1, branchPeriod2);

        final BranchPeriodJsonConverter testee = spy(new BranchPeriodJsonConverter());
        final List<BranchPeriodJson> branchJsonList = testee.convertToBranchPeriodJsonList(branchList);

        assertEquals(branchList.size(), branchJsonList.size());
        for (final BranchPeriod branch : branchList) {
            verify(testee).convertToBranchPeriodJson(eq(branch));
        }
    }

    @Test
    public void testConvertToBranchPeriodJson() {
        final BranchPeriod branchPeriod = new BranchPeriod("branch1", "branch1", "period1", Arrays.asList("exam1"), "exam3",
                Arrays.asList("studentResult1"));
        final BranchPeriodJson branchPeriodJson = new BranchPeriodJsonConverter().convertToBranchPeriodJson(branchPeriod);

        assertEquals(branchPeriod.getId(), branchPeriodJson.getId());
        assertEquals(branchPeriod.getBranch(), branchPeriodJson.getBranch());
        assertEquals(branchPeriod.getPeriod(), branchPeriodJson.getPeriod());
        assertEquals(branchPeriod.getExams(), branchPeriodJson.getExams());
        assertEquals(branchPeriod.getStudentResults(), branchPeriodJson.getStudentResults());
    }
}
