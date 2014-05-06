package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.BranchPeriodJson;
import net.scholagest.object.BranchPeriod;

/**
 * Method to convert from transfer object {@link BranchPeriod} to json {@link BranchPeriodJson} and reverse
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchPeriodJsonConverter {
    /**
     * Convenient method to convert a list of {@link BranchPeriod} to a list of {@link BranchPeriodJson}
     *  
     * @param branchPeriodList The list to convert
     * @return The converted list
     */
    public List<BranchPeriodJson> convertToBranchPeriodJsonList(final List<BranchPeriod> branchPeriodList) {
        final List<BranchPeriodJson> branchJsonList = new ArrayList<>();

        for (final BranchPeriod branch : branchPeriodList) {
            branchJsonList.add(convertToBranchPeriodJson(branch));
        }

        return branchJsonList;
    }

    /**
     * Convert a {@link BranchPeriod} to its json version {@link BranchPeriodJson}
     * 
     * @param branch The branchPeriod to convert
     * @return The converted branchPeriod json
     */
    public BranchPeriodJson convertToBranchPeriodJson(final BranchPeriod branchPeriod) {
        final BranchPeriodJson branchPeriodJson = new BranchPeriodJson();

        branchPeriodJson.setId(branchPeriod.getId());
        branchPeriodJson.setBranch(branchPeriod.getBranch());
        branchPeriodJson.setPeriod(branchPeriod.getPeriod());
        branchPeriodJson.setExams(new ArrayList<>(branchPeriod.getExams()));
        branchPeriodJson.setStudentResults(new ArrayList<>(branchPeriod.getStudentResults()));

        return branchPeriodJson;
    }
}
