/**
 * 
 */
package net.scholagest.service;

import net.scholagest.object.User;
import net.scholagest.object.UserBlock;

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

    /**
     * Retrieve the user block information (user, teacher and classes) by user id.
     * 
     * @param id User's id
     * @return The user block information
     */
    public UserBlock getUserBlock(final String id);
}
