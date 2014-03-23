package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.BranchJson;
import net.scholagest.object.Branch;

/**
 * Method to convert from transfer object {@link Branch} to json {@link BranchJson} and reverse
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchJsonConverter {
    /**
     * Convenient method to convert a list of {@link Branch} to a list of {@link BranchJson}
     *  
     * @param branchList The list to convert
     * @return The converted list
     */
    public List<BranchJson> convertToBranchJsonList(final List<Branch> branchList) {
        final List<BranchJson> branchJsonList = new ArrayList<>();

        for (final Branch branch : branchList) {
            branchJsonList.add(convertToBranchJson(branch));
        }

        return branchJsonList;
    }

    /**
     * Convert a {@link Branch} to its json version {@link BranchJson}
     * 
     * @param branch The branch to convert
     * @return The converted branch json
     */
    public BranchJson convertToBranchJson(final Branch branch) {
        final BranchJson branchJson = new BranchJson();

        branchJson.setId(branch.getId());
        branchJson.setName(branch.getName());
        branchJson.setClazz(branch.getClazz());
        branchJson.setNumerical(branch.isNumerical());
        branchJson.setBranchPeriods(new ArrayList<>(branch.getBranchPeriods()));

        return branchJson;
    }

    /**
     * Convert a {@link BranchJson} to its version {@link Branch}.
     * 
     * @param branchJson The branch json to convert
     * @return The converted branch
     */
    public Branch convertToBranch(final BranchJson branchJson) {
        final Branch branch = new Branch();

        branch.setId(branchJson.getId());
        branch.setName(branchJson.getName());
        branch.setClazz(branchJson.getClazz());
        branch.setNumerical(branchJson.isNumerical());
        branch.setBranchPeriods(new ArrayList<>(branchJson.getBranchPeriods()));

        return branch;
    }
}
