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
    public User getUser(final Long id);

    /**
     * Get the user identified by username.
     * 
     * @param username The username of the user to get
     * @return The user
     */
    public User getUserByUsername(String username);

    /**
     * Update the user identified by id based on the received information.
     * 
     * @param user The information to store
     * @return The updated user
     */
    public User saveUser(final User user);
}
