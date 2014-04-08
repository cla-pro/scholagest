package net.scholagest.service;

import java.util.List;

import net.scholagest.object.Exam;

/**
 * Provides the methods to handle the exams. This level is responsible to
 * filter the results (elements and fields) and to return only what the
 * requesting user is allowed to see.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface ExamServiceLocal {
    /**
     * Get the list of the exams identified by ids.
     * 
     * @param ids The ids to filter the list.
     * @return The exams list
     */
    public List<Exam> getExams(List<String> ids);

    /**
     * Get the exam with the given id.
     * 
     * @param id Used to find the exam
     * @return The exam
     */
    public Exam getExam(final String id);

    /**
     * Create a new exam.
     * 
     * @param exam The information to store with the new exam
     * @return The newly created exam
     */
    public Exam createExam(final Exam exam);

    /**
     * Update a exam. The id is also required to verify the permissions.
     * 
     * @param examId The Id of the exam to update
     * @param exam The exam's information to store
     * @return The updated exam
     */
    public Exam saveExam(final String id, final Exam exam);
}
