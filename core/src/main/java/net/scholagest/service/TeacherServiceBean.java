/**
 * 
 */
package net.scholagest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.scholagest.authorization.Permission;
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;
import net.scholagest.utils.IdHelper;

/**
 * Implementation of {@see TeacherServiceLocal}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class TeacherServiceBean implements TeacherServiceLocal {
    public static Map<String, Teacher> teachersMap = new HashMap<>();

    static {
        teachersMap.put("1", new Teacher("1", "Cédric", "Lavanchy", new TeacherDetail("1", "Kleefeldstrasse 1, 3018 Bern",
                "cedric.lavanchy@gmail.com", "+41791234567")));
        teachersMap.put("2", new Teacher("2", "Valérie", "Parvex", new TeacherDetail("2", "Chemin des Merisiers 25, 1870 Monthey",
                "valerie.parvex@gmail.com", "+41797654321")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getTeachers() {
        return new ArrayList<>(teachersMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getTeacher(final List<String> ids) {
        final List<Teacher> teachers = new ArrayList<>();

        for (final String id : ids) {
            if (teachersMap.containsKey(id)) {
                teachers.add(teachersMap.get(id));
            }
        }

        return teachers;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Teacher createTeacher(final Teacher teacher) {
        final String id = IdHelper.getNextId(teachersMap.keySet());

        final Teacher toStore = new Teacher(teacher);
        toStore.setId(id);
        toStore.getDetail().setId(id);

        teachersMap.put(id, toStore);

        return toStore;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Teacher saveTeacher(@Permission final String teacherId, final Teacher teacher) {
        final Teacher stored = teachersMap.get(teacherId);

        stored.setFirstName(teacher.getFirstName());
        stored.setLastName(teacher.getLastName());

        return stored;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherDetail getTeacherDetail(final String id) {
        final Teacher teacher = teachersMap.get(id);
        return teacher.getDetail();
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public TeacherDetail saveTeacherDetail(@Permission final String teacherId, final TeacherDetail teacherDetail) {
        final Teacher teacher = teachersMap.get(teacherId);
        final TeacherDetail stored = teacher.getDetail();

        stored.setAddress(teacherDetail.getAddress());
        stored.setEmail(teacherDetail.getEmail());
        stored.setPhone(teacherDetail.getPhone());

        return stored;
    }
}
