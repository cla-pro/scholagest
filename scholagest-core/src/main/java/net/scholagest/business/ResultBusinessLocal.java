package net.scholagest.business;

import net.scholagest.object.Result;

/**
 * Provides the methods to handle the results. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface ResultBusinessLocal {
    /**
     * Get the result with the given id.
     * 
     * @param id Used to find the result
     * @return The result
     */
    public Result getResult(final Long id);

    /**
     * Update a result.
     * 
     * @param result The result's information to store
     * @return The updated result
     */
    public Result saveResult(final Result result);
}
