package net.scholagest.business;

import java.util.List;

import net.scholagest.object.Student;

/**
 * Provides the methods to handle the students. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.13.0
 */
public interface StudentBusinessLocal {
    /**
     * Get the list of all students.
     * 
     * @return The students list
     */
    public List<Student> getStudents();

    /**
     * Get the student with the given id.
     * 
     * @param id Used to find the student
     * @return The student
     */
    public Student getStudent(final String id);

    /**
     * Create a new student.
     * 
     * @param student The information to store with the new student
     * @return The newly created student
     */
    public Student createStudent(final Student student);

    /**
     * Update a student, the personal and medical parts are not updated. The id is also required to verify the permissions
     * 
     * @param studentId The Id of the student to update
     * @param student The student's information to store
     * @return The updated student
     */
    public Student saveStudent(final String studentId, final Student student);
}
