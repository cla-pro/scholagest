package net.scholagest.service;

import java.util.List;

import net.scholagest.object.StudentResult;

/**
 * Provides the methods to handle the student results. This level is responsible to
 * filter the results (elements and fields) and to return only what the
 * requesting user is allowed to see.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface StudentResultServiceLocal {
    /**
     * Get the list of the student results identified by ids.
     * 
     * @param ids The ids to filter the list.
     * @return The student results list
     */
    public List<StudentResult> getStudentResults(List<String> ids);

    /**
     * Get the student result with the given id.
     * 
     * @param id Used to find the student result
     * @return The student result
     */
    public StudentResult getStudentResult(final String id);
}
