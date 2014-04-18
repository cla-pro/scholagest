package net.scholagest.dao;

import net.scholagest.db.entity.BranchPeriodEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link BranchPeriodEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface BranchPeriodDaoLocal {
    /**
     * Get a single branch period entity identified by id
     * 
     * @param id The id of the branch period to retrieve
     * @return The branch period if it exists, else null
     */
    public BranchPeriodEntity getBranchPeriodEntityById(long id);

    /**
     * Persist a new branch period entity into the DB
     * 
     * @param branchPeriodEntity The branch period entity to save
     * @return The persisted branch period entity (with its id)
     */
    public BranchPeriodEntity persistBranchPeriodEntity(BranchPeriodEntity branchPeriodEntity);
}
