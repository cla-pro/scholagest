package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.TeacherDetailJson;
import net.scholagest.app.rest.ws.objects.TeacherJson;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

public class TeacherJsonConverter {
    public List<TeacherJson> convertToTeacherJson(final List<Teacher> teachers) {
        final List<TeacherJson> teachersJson = new ArrayList<>();

        for (final Teacher teacher : teachers) {
            teachersJson.add(convertToTeacherJson(teacher));
        }

        return teachersJson;
    }

    public TeacherJson convertToTeacherJson(final Teacher teacher) {
        final TeacherJson teacherJson = new TeacherJson();

        teacherJson.setId(teacher.getId());
        teacherJson.setFirstName(teacher.getFirstName());
        teacherJson.setLastName(teacher.getLastName());
        teacherJson.setDetail(teacher.getDetail().getId());

        return teacherJson;
    }

    public TeacherDetailJson convertToTeacherDetailJson(final TeacherDetail teacherDetail) {
        final TeacherDetailJson teacherDetailJson = new TeacherDetailJson();

        teacherDetailJson.setId(teacherDetail.getId());
        teacherDetailJson.setAddress(teacherDetail.getAddress());
        teacherDetailJson.setEmail(teacherDetail.getEmail());
        teacherDetailJson.setPhone(teacherDetail.getPhone());

        return teacherDetailJson;
    }

    public Teacher convertToTeacher(final TeacherJson teacherJson) {
        final Teacher teacher = new Teacher();

        teacher.setId(teacherJson.getId());
        teacher.setFirstName(teacherJson.getFirstName());
        teacher.setLastName(teacherJson.getLastName());

        return teacher;
    }

    public TeacherDetail convertToTeacherDetail(final TeacherDetailJson teacherDetailJson) {
        final TeacherDetail teacherDetail = new TeacherDetail();

        teacherDetail.setId(teacherDetailJson.getId());
        teacherDetail.setAddress(teacherDetailJson.getAddress());
        teacherDetail.setEmail(teacherDetailJson.getEmail());
        teacherDetail.setPhone(teacherDetailJson.getPhone());

        return teacherDetail;
    }
}
