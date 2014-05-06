package net.scholagest.dao;

import net.scholagest.db.entity.BranchEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link BranchEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface BranchDaoLocal {
    /**
     * Get a single branch entity identified by id
     * 
     * @param id The id of the branch to retrieve
     * @return The branch if it exists, else null
     */
    public BranchEntity getBranchEntityById(long id);

    /**
     * Persist a new branch entity into the DB
     * 
     * @param branchEntity The branch entity to save
     * @return The persisted branch entity (with its id)
     */
    public BranchEntity persistBranchEntity(BranchEntity branchEntity);

}
