package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.TeacherDetailJson;
import net.scholagest.app.rest.ws.objects.TeacherJson;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

/**
 * Method to convert from transfer object {@link Teacher} to json {@link TeacherJson} as well as {@link TeacherDetail} 
 * to {@link TeacherDetailJson} and reverse.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class TeacherJsonConverter {
    /**
     * Convenient method to convert a list of {@link Teacher} to a list of {@link TeacherJson}
     *  
     * @param teacherList The list to convert
     * @return The converted list
     */
    public List<TeacherJson> convertToTeacherJsonList(final List<Teacher> teacherList) {
        final List<TeacherJson> teachersJson = new ArrayList<>();

        for (final Teacher teacher : teacherList) {
            teachersJson.add(convertToTeacherJson(teacher));
        }

        return teachersJson;
    }

    /**
     * Convert a {@link Teacher} to its json version {@link TeacherJson}. The {@link TeacherDetail} is copied only as reference (id).
     * 
     * @param teacher The teacher to convert
     * @return The converted teacher json
     */
    public TeacherJson convertToTeacherJson(final Teacher teacher) {
        final TeacherJson teacherJson = new TeacherJson();

        teacherJson.setId(teacher.getId());
        teacherJson.setFirstname(teacher.getFirstname());
        teacherJson.setLastname(teacher.getLastname());
        teacherJson.setDetail(teacher.getDetail().getId());

        return teacherJson;
    }

    /**
     * Convert a {@link TeacherDetail} to {@link TeacherDetailJson}.
     * 
     * @param teacherDetail The teacher detail to convert
     * @return The converted json teacher detail
     */
    public TeacherDetailJson convertToTeacherDetailJson(final TeacherDetail teacherDetail) {
        final TeacherDetailJson teacherDetailJson = new TeacherDetailJson();

        teacherDetailJson.setId(teacherDetail.getId());
        teacherDetailJson.setAddress(teacherDetail.getAddress());
        teacherDetailJson.setEmail(teacherDetail.getEmail());
        teacherDetailJson.setPhone(teacherDetail.getPhone());

        return teacherDetailJson;
    }

    /**
     * Convert a {@link TeacherJson} to a {@link Teacher}
     * 
     * @param teacherJson The teacher json to convert
     * @return The converted teacher
     */
    public Teacher convertToTeacher(final TeacherJson teacherJson) {
        final Teacher teacher = new Teacher();

        teacher.setId(teacherJson.getId());
        teacher.setFirstname(teacherJson.getFirstname());
        teacher.setLastname(teacherJson.getLastname());

        return teacher;
    }

    /**
     * Convert a {@link TeacherDetailJson} to a {@link TeacherDetail}
     * 
     * @param teacherJson The teacher detail json to convert
     * @return The converted teacher detail
     */
    public TeacherDetail convertToTeacherDetail(final TeacherDetailJson teacherDetailJson) {
        final TeacherDetail teacherDetail = new TeacherDetail();

        teacherDetail.setId(teacherDetailJson.getId());
        teacherDetail.setAddress(teacherDetailJson.getAddress());
        teacherDetail.setEmail(teacherDetailJson.getEmail());
        teacherDetail.setPhone(teacherDetailJson.getPhone());

        return teacherDetail;
    }
}
