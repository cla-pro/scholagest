package net.scholagest.business;

import net.scholagest.object.Branch;

/**
 * Provides the methods to handle the branches. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface BranchBusinessLocal {
    /**
     * Get the branch with the given id.
     * 
     * @param id Used to find the branch
     * @return The branch
     */
    public Branch getBranch(final Long id);

    /**
     * Create a new branch.
     * 
     * @param branch The information to store with the new branch
     * @return The newly created branch
     */
    public Branch createBranch(final Branch branch);

    /**
     * Update a branch.
     * 
     * @param branch The branch's information to store
     * @return The updated branch
     */
    public Branch saveBranch(final Branch branch);
}
