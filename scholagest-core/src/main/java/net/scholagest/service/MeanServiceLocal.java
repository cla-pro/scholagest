package net.scholagest.service;

import java.util.List;

import net.scholagest.object.Mean;

/**
 * Provides the methods to handle the means. This level is responsible to
 * filter the means (elements and fields) and to return only what the
 * requesting user is allowed to see.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface MeanServiceLocal {
    /**
     * Get the list of the means identified by ids.
     * 
     * @param ids The ids to filter the list.
     * @return The means list
     */
    public List<Mean> getMeans(List<String> ids);

    /**
     * Get the mean with the given id.
     * 
     * @param id Used to find the mean
     * @return The mean
     */
    public Mean getMean(final String id);

    /**
     * Update a mean.
     * 
     * @param mean The mean's information to store
     * @return The updated mean
     */
    public Mean saveMean(final Mean mean);
}
