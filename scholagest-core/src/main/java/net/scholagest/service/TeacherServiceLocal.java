/**
 * 
 */
package net.scholagest.service;

import java.util.List;

import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

/**
 * Provides the methods to handle the teachers. This level is responsible to
 * filter the results (elements and fields) and to return only what the
 * requesting user is allowed to see.
 * 
 * @author CLA
 * @since 0.13.0
 */
public interface TeacherServiceLocal {
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
    public List<Teacher> getTeacher(final List<String> ids);

    /**
     * Create a new teacher.
     * 
     * @param teacher The information to store with the new teacher
     * @return The newly created teacher
     */
    public Teacher createTeacher(final Teacher teacher);

    /**
     * Update a teacher, the detail part is not updated. The id is also required to verify the permissions
     * 
     * @param teacherId The Id of the teacher to update
     * @param teacher The teacher's information to store
     * @return The updated teacher
     */
    public Teacher saveTeacher(final String teacherId, final Teacher teacher);

    /**
     * Get the teacher detail identified by id.
     * 
     * @param id The teacher detail's id
     * @return The teacher detail
     */
    public TeacherDetail getTeacherDetail(final String id);

    /**
     * Update the teacher detail. The teacher id is required to verify the permission.
     * 
     * @param teacherId The teacher's id which has the detail
     * @param teacherDetail The new detail information
     * @return The updated detail
     */
    public TeacherDetail saveTeacherDetail(final String teacherId, final TeacherDetail teacherDetail);
}
