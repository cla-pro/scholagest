package net.scholagest.business;

import java.util.List;

import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

/**
 * Provides the methods to handle the teachers. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.13.0
 */
public interface TeacherBusinessLocal {

    /**
     * Get the list of all teachers.
     * 
     * @return The teachers list
     */
    public List<Teacher> getTeachers();

    /**
     * Get the list of teachers with the given ids.
     * 
     * @param ids Used to filter the teachers list
     * @return The teachers
     */
    public Teacher getTeacher(final String id);

    /**
     * Create a new teacher.
     * 
     * @param teacher The information to store with the new teacher
     * @return The newly created teacher
     */
    public Teacher createTeacher(final Teacher teacher);

    /**
     * Update a teacher, the detail part is not updated.
     * 
     * @param teacher The teacher's information to store
     * @return The updated teacher
     */
    public Teacher saveTeacher(final Teacher teacher);

    /**
     * Get the teacher detail identified by id.
     * 
     * @param id The teacher detail's id
     * @return The teacher detail
     */
    public TeacherDetail getTeacherDetail(final String id);

    /**
     * Update the teacher detail.
     * 
     * @param teacherId Id of the teacher whose detail must be updated
     * @param teacherDetail The new detail information
     * @return The updated detail
     */
    public TeacherDetail saveTeacherDetail(final String teacherId, final TeacherDetail teacherDetail);
}
