package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.object.Branch;

/**
 * Method to convert from the jpa entity {@link BranchEntity} to the transfer object {@link Branch} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class BranchEntityConverter {
    /**
     * Convenient method to convert a list of {@link BranchEntity} to a list of {@link Branch}
     *  
     * @param branchEntityList The list to convert
     * @return The converted list
     */
    public List<Branch> convertToBranchList(final List<BranchEntity> branchEntityList) {
        final List<Branch> branchList = new ArrayList<>();

        for (final BranchEntity branchEntity : branchEntityList) {
            branchList.add(convertToBranch(branchEntity));
        }

        return branchList;
    }

    /**
     * Convert a {@link BranchEntity} to its transfer version {@link Branch}.
     * 
     * @param branchEntity The branch entity to convert
     * @return The converted branch
     */
    public Branch convertToBranch(final BranchEntity branchEntity) {
        final Branch branch = new Branch();
        branch.setId("" + branchEntity.getId());
        branch.setName(branchEntity.getName());
        branch.setClazz("" + branchEntity.getClazz().getId());

        final List<String> branchPeriods = new ArrayList<>();
        for (final BranchPeriodEntity branchPeriodEntity : branchEntity.getBranchPeriods()) {
            branchPeriods.add("" + branchPeriodEntity.getId());
        }
        branch.setBranchPeriods(branchPeriods);

        return branch;
    }

    /**
     * Convert a {@link Branch} to the entity {@link BranchEntity}.
     * 
     * @param branch The branch to convert
     * @return The converted branch entity
     */
    public BranchEntity convertToBranchEntity(final Branch branch) {
        final BranchEntity branchEntity = new BranchEntity();
        branchEntity.setName(branch.getName());
        branchEntity.setNumerical(branch.isNumerical());
        branchEntity.setBranchPeriods(new ArrayList<BranchPeriodEntity>());

        return branchEntity;
    }
}
