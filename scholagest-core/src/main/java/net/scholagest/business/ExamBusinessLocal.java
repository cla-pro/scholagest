package net.scholagest.business;

import net.scholagest.object.Exam;

/**
 * Provides the methods to handle the exams. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface ExamBusinessLocal {
    /**
     * Get the exam with the given id.
     * 
     * @param id Used to find the exam
     * @return The exam
     */
    public Exam getExam(final Long id);

    /**
     * Create a new exam.
     * 
     * @param exam The information to store with the new exam
     * @return The newly created exam
     */
    public Exam createExam(final Exam exam);

    /**
     * Update a exam.
     * 
     * @param exam The exam's information to store
     * @return The updated exam
     */
    public Exam saveExam(final Exam exam);
}
