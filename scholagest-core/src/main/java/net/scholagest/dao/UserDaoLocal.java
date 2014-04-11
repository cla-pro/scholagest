package net.scholagest.dao;

import net.scholagest.db.entity.UserEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link UserEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface UserDaoLocal {

    /**
     * Get a single user entity identified by its id
     * 
     * @param id The id of the user to retrieve
     * @return The user if it exists, else null
     */
    public UserEntity getUserEntityById(final long id);

    /**
     * Get a single user entity identified by its username
     * 
     * @param username The username of the user to retrieve
     * @return The user if it exists, else null
     */
    public UserEntity getUserEntityByUsername(final String username);

    /**
     * Persist a new user entity into the DB
     * 
     * @param userEntity The user entity to save
     * @return The persisted user entity (with its id)
     */
    public UserEntity persistUserEntity(final UserEntity userEntity);
}
