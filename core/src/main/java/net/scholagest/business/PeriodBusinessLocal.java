package net.scholagest.business;

import net.scholagest.object.Period;

/**
 * Provides the methods to handle the periods. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface PeriodBusinessLocal {
    /**
     * Get the period with the given id.
     * 
     * @param id Used to find the period
     * @return The period
     */
    public Period getPeriod(final String id);

    /**
     * Create a new period.
     * 
     * @param period The information to store with the new period
     * @return The newly created period
     */
    public Period createPeriod(final Period period);

    /**
     * Update a period.
     * 
     * @param period The period's information to store
     * @return The updated period
     */
    public Period savePeriod(final Period period);
}
