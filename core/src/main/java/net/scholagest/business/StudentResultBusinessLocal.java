package net.scholagest.business;

import net.scholagest.object.StudentResult;

/**
 * Provides the methods to handle the student results. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface StudentResultBusinessLocal {
    /**
     * Get the student result with the given id.
     * 
     * @param id Used to find the student result
     * @return The student
     */
    public StudentResult getStudentResult(final String id);
}
