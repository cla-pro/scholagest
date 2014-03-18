/**
 * 
 */
package net.scholagest.service;

import net.scholagest.object.User;

/**
 * Provides the methods to handle the users. This level is responsible to
 * filter the results (elements and fields) and to return only what the
 * requesting user is allowed to see.
 * 
 * @author CLA
 * @since 0.13.0
 */
public interface UserServiceLocal {
    /**
     * Retrieve the user by id.
     * 
     * @param id User's id
     * @return The user
     */
    public User getUser(final String id);
}
