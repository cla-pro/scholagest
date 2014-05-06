package net.scholagest.business;

import net.scholagest.object.BranchPeriod;

/**
 * Provides the methods to handle the branch periods. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface BranchPeriodBusinessLocal {
    /**
     * Get the branch period with the given id.
     * 
     * @param id Used to find the branch period
     * @return The branch period
     */
    public BranchPeriod getBranchPeriod(final Long id);
}
