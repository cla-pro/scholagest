package net.scholagest.business;

import java.util.List;

import net.scholagest.object.Student;
import net.scholagest.object.StudentClasses;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;

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
     * Update a student, the personal and medical parts are not updated.
     * 
     * @param studentId The Id of the student to update
     * @param student The student's information to store
     * @return The updated student
     */
    public Student saveStudent(final String studentId, final Student student);

    /**
     * Update the student personal information.
     * 
     * @param studentId The Id of the student to update
     * @param student The student's personal information to store
     * @return The updated student personal information
     */
    public StudentPersonal saveStudentPersonal(String studentId, StudentPersonal studentPersonal);

    /**
     * Update the student medical information.
     * 
     * @param studentId The Id of the student to update
     * @param student The student's medical information to store
     * @return The updated student medical information
     */
    public StudentMedical saveStudentMedical(String studentId, StudentMedical studentMedical);

    /**
     * Retrieve the class information ({@link StudentClasses}) for a student.
     * 
     * @param studentId The student for which the studentClasses information must be retrieved.
     * @return
     */
    public StudentClasses getStudentClasses(String studentId);
}
