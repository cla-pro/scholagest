package net.scholagest.service;

import java.util.List;

import net.scholagest.object.Period;

/**
 * Provides the methods to handle the periods. This level is responsible to
 * filter the results (elements and fields) and to return only what the
 * requesting user is allowed to see.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface PeriodServiceLocal {
    /**
     * Get the list of the periods identified by ids.
     * 
     * @param ids The ids to filter the list.
     * @return The period list
     */
    public List<Period> getPeriods(List<String> ids);

    /**
     * Get the period with the given id.
     * 
     * @param id Used to find the period
     * @return The period
     */
    public Period getPeriod(final String id);
}
