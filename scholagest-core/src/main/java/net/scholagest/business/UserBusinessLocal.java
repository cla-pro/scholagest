package net.scholagest.business;

import net.scholagest.object.User;

/**
 * Provides the methods to handle the users. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.13.0
 */
public interface UserBusinessLocal {
    /**
     * Get the user identified by id.
     * 
     * @param id The id of the user to get
     * @return The user
     */
    public User getUser(final String id);

    /**
     * Update the user identified by id based on the received information.
     * 
     * @param id Id of the user to update
     * @param user The information to store
     * @return The updated user
     */
    public User saveUser(final String id, final User user);
}
