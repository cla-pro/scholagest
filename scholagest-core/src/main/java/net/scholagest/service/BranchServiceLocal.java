package net.scholagest.service;

import java.util.List;

import net.scholagest.object.Branch;

/**
 * Provides the methods to handle the branches. This level is responsible to
 * filter the results (elements and fields) and to return only what the
 * requesting user is allowed to see.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface BranchServiceLocal {
    /**
     * Get the list of the branches identified by ids.
     * 
     * @param ids The ids to filter the list.
     * @return The branches list
     */
    public List<Branch> getBranches(List<String> ids);

    /**
     * Get the branch with the given id.
     * 
     * @param id Used to find the branch
     * @return The branch
     */
    public Branch getBranch(final String id);

    /**
     * Create a new branch.
     * 
     * @param branch The information to store with the new branch
     * @return The newly created branch
     */
    public Branch createBranch(final Branch branch);

    /**
     * Update a branch. The id is also required to verify the permissions.
     * 
     * @param branchId The Id of the branch to update
     * @param branch The branch's information to store
     * @return The updated branch
     */
    public Branch saveBranch(final String id, final Branch branch);
}
