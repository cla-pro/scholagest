package net.scholagest.service;

import java.util.List;

import net.scholagest.object.BranchPeriod;

/**
 * Provides the methods to handle the branch periods. This level is responsible to
 * filter the results (elements and fields) and to return only what the
 * requesting user is allowed to see.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface BranchPeriodServiceLocal {
    /**
     * Get the list of the branch periods identified by ids.
     * 
     * @param ids The ids to filter the list.
     * @return The branch periods list
     */
    public List<BranchPeriod> getBranchPeriods(List<String> ids);

    /**
     * Get the branch period with the given id.
     * 
     * @param id Used to find the branch period
     * @return The branch period
     */
    public BranchPeriod getBranchPeriod(final String id);
}
