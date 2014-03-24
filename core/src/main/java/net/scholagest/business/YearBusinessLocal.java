package net.scholagest.business;

import java.util.List;

import net.scholagest.object.Year;

/**
 * Provides the methods to handle the years. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface YearBusinessLocal {
    /**
     * Get the list of all years.
     * 
     * @return The years list
     */
    public List<Year> getYears();

    /**
     * Get the year with the given id.
     * 
     * @param id Used to find the year
     * @return The year
     */
    public Year getYear(final String id);

    /**
     * Create a new year.
     * 
     * @param year The information to store with the new year
     * @return The newly created year
     */
    public Year createYear(final Year year);

    /**
     * Update a year.
     * 
     * @param year The year's information to store
     * @return The updated year
     */
    public Year saveYear(final Year year);
}
