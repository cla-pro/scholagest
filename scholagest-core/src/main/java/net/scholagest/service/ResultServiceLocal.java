package net.scholagest.service;

import java.util.List;

import net.scholagest.object.Result;

/**
 * Provides the methods to handle the results. This level is responsible to
 * filter the results (elements and fields) and to return only what the
 * requesting user is allowed to see.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface ResultServiceLocal {
    /**
     * Get the list of the results identified by ids.
     * 
     * @param ids The ids to filter the list.
     * @return The results list
     */
    public List<Result> getResults(List<String> ids);

    /**
     * Get the result with the given id.
     * 
     * @param id Used to find the result
     * @return The result
     */
    public Result getResult(final String id);

    /**
     * Update a result.
     * 
     * @param result The result's information to store
     * @return The updated result
     */
    public Result saveResult(final Result result);
}
