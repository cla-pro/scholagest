/**
 * 
 */
package net.scholagest.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;
import net.scholagest.utils.IdHelper;

/**
 * Implementation of {@link TeacherBusinessLocal}.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class TeacherBusinessBean implements TeacherBusinessLocal {
    public static Map<String, Teacher> teachersMap = new HashMap<>();

    static {
        teachersMap.put("teacher1", new Teacher("teacher1", "Cédric", "Lavanchy", new TeacherDetail("teacher1", "Kleefeldstrasse 1, 3018 Bern",
                "cedric.lavanchy@gmail.com", "+41791234567")));
        teachersMap.put("teacher2", new Teacher("teacher2", "Valérie", "Parvex", new TeacherDetail("teacher2",
                "Chemin des Merisiers 25, 1870 Monthey", "valerie.parvex@gmail.com", "+41797654321")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getTeachers() {
        return copyTeachers();
    }

    private List<Teacher> copyTeachers() {
        final List<Teacher> teachers = new ArrayList<>();

        for (final Teacher teacher : teachersMap.values()) {
            teachers.add(new Teacher(teacher));
        }
        return teachers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher getTeacher(final String id) {
        if (teachersMap.containsKey(id)) {
            return new Teacher(teachersMap.get(id));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher createTeacher(final Teacher teacher) {
        // TODO check for existence first?
        final String id = IdHelper.getNextId(teachersMap.keySet(), "teacher");

        final Teacher toStore = new Teacher(teacher);
        toStore.setId(id);
        toStore.getDetail().setId(id);

        teachersMap.put(id, toStore);

        return new Teacher(toStore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher saveTeacher(final Teacher teacher) {
        // TODO check for existence first?
        final Teacher stored = teachersMap.get(teacher.getId());

        stored.setFirstName(teacher.getFirstName());
        stored.setLastName(teacher.getLastName());

        return new Teacher(stored);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherDetail getTeacherDetail(final String id) {
        if (teachersMap.containsKey(id)) {
            final Teacher teacher = teachersMap.get(id);
            return new TeacherDetail(teacher.getDetail());
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherDetail saveTeacherDetail(final String teacherId, final TeacherDetail teacherDetail) {
        // TODO check for existence first?
        final Teacher stored = teachersMap.get(teacherId);
        final TeacherDetail storedDetail = stored.getDetail();

        storedDetail.setAddress(teacherDetail.getAddress());
        storedDetail.setEmail(teacherDetail.getEmail());
        storedDetail.setPhone(teacherDetail.getPhone());

        return new TeacherDetail(storedDetail);
    }

}
