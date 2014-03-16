package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.TeacherDetailJson;
import net.scholagest.app.rest.ws.objects.TeacherJson;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

/**
 * Method to convert from transfer object {@see Teacher} to json {@see TeacherJson} as well as {@see TeacherDetail} 
 * to {@see TeacherDetailJson} and reverse.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class TeacherJsonConverter {
    /**
     * Convenient method to convert a list of {@see Teacher} to a list of {@see TeacherJson}
     *  
     * @param teachers The list to convert
     * @return The converted list
     */
    public List<TeacherJson> convertToTeacherJson(final List<Teacher> teachers) {
        final List<TeacherJson> teachersJson = new ArrayList<>();

        for (final Teacher teacher : teachers) {
            teachersJson.add(convertToTeacherJson(teacher));
        }

        return teachersJson;
    }

    /**
     * Convert a {@see Teacher} to its json version {@see TeacherJson}
     * 
     * @param teacher The teacher to convert
     * @return The converted teacher json
     */
    public TeacherJson convertToTeacherJson(final Teacher teacher) {
        final TeacherJson teacherJson = new TeacherJson();

        teacherJson.setId(teacher.getId());
        teacherJson.setFirstName(teacher.getFirstName());
        teacherJson.setLastName(teacher.getLastName());
        teacherJson.setDetail(teacher.getDetail().getId());

        return teacherJson;
    }

    /**
     * Convert a {@see TeacherDetail} to {@see TeacherDetailJson}.
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
     * Convert a {@see TeacherJson} to a {@see Teacher}
     * 
     * @param teacherJson The teacher json to convert
     * @return The converted teacher
     */
    public Teacher convertToTeacher(final TeacherJson teacherJson) {
        final Teacher teacher = new Teacher();

        teacher.setId(teacherJson.getId());
        teacher.setFirstName(teacherJson.getFirstName());
        teacher.setLastName(teacherJson.getLastName());

        return teacher;
    }

    /**
     * Convert a {@see TeacherDetailJson} to a {@see TeacherDetail}
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
