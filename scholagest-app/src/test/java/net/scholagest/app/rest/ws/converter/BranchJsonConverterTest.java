package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.BranchJson;
import net.scholagest.object.Branch;

import org.junit.Test;

/**
 * Test class for {@link BranchJsonConverter}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchJsonConverterTest {
    @Test
    public void testConvertToBranchJsonList() {
        final Branch branch1 = new Branch("branch1", "name1", true, "clazz1", Arrays.asList("branchPeriod1"));
        final Branch branch2 = new Branch("branch2", "name2", false, "clazz2", Arrays.asList("branchPeriod2"));
        final List<Branch> branchList = Arrays.asList(branch1, branch2);

        final BranchJsonConverter testee = spy(new BranchJsonConverter());
        final List<BranchJson> branchJsonList = testee.convertToBranchJsonList(branchList);

        assertEquals(branchList.size(), branchJsonList.size());
        for (final Branch branch : branchList) {
            verify(testee).convertToBranchJson(eq(branch));
        }
    }

    @Test
    public void testConvertToBranchJson() {
        final Branch branch = new Branch("branch1", "name1", true, "clazz", Arrays.asList("branchPeriod1"));
        final BranchJson branchJson = new BranchJsonConverter().convertToBranchJson(branch);

        assertEquals(branch.getId(), branchJson.getId());
        assertEquals(branch.getName(), branchJson.getName());
        assertEquals(branch.getClazz(), branchJson.getClazz());
        assertEquals(branch.isNumerical(), branchJson.isNumerical());
        assertEquals(branch.getBranchPeriods(), branchJson.getBranchPeriods());
    }

    @Test
    public void testConvertToBranch() {
        final BranchJson branchJson = new BranchJson("branch1", "name1", true, "clazz1", Arrays.asList("branchPeriod1"));
        final Branch branch = new BranchJsonConverter().convertToBranch(branchJson);

        assertEquals(branchJson.getId(), branch.getId());
        assertEquals(branchJson.getName(), branch.getName());
        assertEquals(branchJson.getClazz(), branch.getClazz());
        assertEquals(branchJson.isNumerical(), branch.isNumerical());
        assertEquals(branchJson.getBranchPeriods(), branch.getBranchPeriods());
    }
}
