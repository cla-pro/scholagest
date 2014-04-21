package net.scholagest.business;

import net.scholagest.object.Mean;

/**
 * Provides the methods to handle the means. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface MeanBusinessLocal {
    /**
     * Get the mean with the given id.
     * 
     * @param id Used to find the mean
     * @return The mean
     */
    public Mean getMean(final Long id);

    /**
     * Update a mean.
     * 
     * @param mean The mean's information to store
     * @return The updated mean
     */
    public Mean saveMean(final Mean mean);
}
