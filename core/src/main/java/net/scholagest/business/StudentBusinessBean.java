/**
 * 
 */
package net.scholagest.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.scholagest.object.Student;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;
import net.scholagest.utils.IdHelper;

/**
 * Implementation of {@link StudentBusinessLocal}.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentBusinessBean implements StudentBusinessLocal {
    public static Map<String, Student> studentsMap = new HashMap<>();

    static {
        studentsMap.put("student1", new Student("student1", "Elodie", "Lavanchy", new StudentPersonal("student1", "Route final du Verney 8", "Perly",
                "1242", "Protestant"), new StudentMedical("student1", null)));
        studentsMap.put("student2", new Student("student2", "Thibaud", "Hottelier", new StudentPersonal("student2", "Post Street 711",
                "San Francisco", "1242", null), new StudentMedical("student2", null)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getStudents() {
        return copyStudents();
    }

    private List<Student> copyStudents() {
        final List<Student> students = new ArrayList<>();

        for (final Student student : studentsMap.values()) {
            students.add(new Student(student));
        }
        return students;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student getStudent(final String id) {
        if (studentsMap.containsKey(id)) {
            return new Student(studentsMap.get(id));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student createStudent(final Student student) {
        // TODO check existence first?
        final String id = IdHelper.getNextId(studentsMap.keySet(), "student");

        final Student toStore = new Student(student);
        toStore.setId(id);
        toStore.getStudentPersonal().setId(id);
        toStore.getStudentMedical().setId(id);

        studentsMap.put(id, toStore);

        return toStore;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student saveStudent(final String studentId, final Student student) {
        // TODO check existence first?
        final Student stored = studentsMap.get(studentId);

        stored.setFirstName(student.getFirstName());
        stored.setLastName(student.getLastName());

        return new Student(stored);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentPersonal saveStudentPersonal(final String studentId, final StudentPersonal studentPersonal) {
        // TODO check existence first?
        final Student stored = studentsMap.get(studentId);
        final StudentPersonal storedPersonal = stored.getStudentPersonal();

        storedPersonal.setStreet(studentPersonal.getStreet());
        storedPersonal.setCity(studentPersonal.getCity());
        storedPersonal.setPostcode(studentPersonal.getPostcode());
        storedPersonal.setReligion(studentPersonal.getReligion());

        return new StudentPersonal(storedPersonal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentMedical saveStudentMedical(final String studentId, final StudentMedical studentMedical) {
        // TODO check existence first?
        final Student stored = studentsMap.get(studentId);
        final StudentMedical storedMedical = stored.getStudentMedical();

        storedMedical.setDoctor(studentMedical.getDoctor());

        return new StudentMedical(storedMedical);
    }
}
