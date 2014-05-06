/**
 * 
 */
package net.scholagest.business;

import java.util.List;

import net.scholagest.converter.StudentEntityConverter;
import net.scholagest.dao.StudentDaoLocal;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentMedicalEntity;
import net.scholagest.db.entity.StudentPersonalEntity;
import net.scholagest.object.Student;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;

import com.google.inject.Inject;

/**
 * Implementation of {@link StudentBusinessLocal}.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentBusinessBean implements StudentBusinessLocal {
    // public static Map<String, Student> studentsMap = new HashMap<>();
    //
    // static {
    // studentsMap.put(
    // "student1",
    // new Student("student1", "Elodie", "Lavanchy", new
    // StudentPersonal("student1", "Route final du Verney 8", "Perly", "1242",
    // "Protestant"), new StudentMedical("student1", null), new
    // StudentClass("student1", new ArrayList<String>(), Arrays
    // .asList("clazz1"))));
    // studentsMap.put("student2",
    // new Student("student2", "Thibaud", "Hottelier", new
    // StudentPersonal("student2", "Post Street 711", "San Francisco", "1242",
    // null),
    // new StudentMedical("student2", null), new StudentClass("student2", new
    // ArrayList<String>(), Arrays.asList("clazz1"))));
    // }

    @Inject
    private StudentDaoLocal studentDao;

    StudentBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getStudents() {
        final StudentEntityConverter converter = new StudentEntityConverter();
        final List<StudentEntity> studentEntityList = studentDao.getAllStudentEntity();

        return converter.convertToStudentList(studentEntityList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student getStudent(final Long id) {
        final StudentEntity studentEntity = studentDao.getStudentEntityById(id);

        if (studentEntity == null) {
            return null;
        } else {
            final StudentEntityConverter converter = new StudentEntityConverter();
            return converter.convertToStudent(studentEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student createStudent(final Student student) {
        // TODO check existence first?
        final StudentEntityConverter converter = new StudentEntityConverter();

        final StudentEntity studentEntity = converter.convertToStudentEntity(student);
        final StudentEntity persisted = studentDao.persistStudentEntity(studentEntity);

        return converter.convertToStudent(persisted);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student saveStudent(final Student student) {
        final StudentEntity studentEntity = studentDao.getStudentEntityById(Long.valueOf(student.getId()));

        if (studentEntity == null) {
            return null;
        } else {
            studentEntity.setFirstname(student.getFirstname());
            studentEntity.setLastname(student.getLastname());

            final StudentEntityConverter converter = new StudentEntityConverter();
            return converter.convertToStudent(studentEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentPersonal saveStudentPersonal(final StudentPersonal studentPersonal) {
        final StudentPersonalEntity studentPersonalEntity = studentDao.getStudentPersonalEntityById(Long.valueOf(studentPersonal.getId()));

        if (studentPersonalEntity == null) {
            return null;
        } else {
            studentPersonalEntity.setStreet(studentPersonal.getStreet());
            studentPersonalEntity.setPostcode(studentPersonal.getPostcode());
            studentPersonalEntity.setCity(studentPersonal.getCity());
            studentPersonalEntity.setReligion(studentPersonal.getReligion());

            final StudentEntityConverter converter = new StudentEntityConverter();
            final StudentEntity studentEntity = studentPersonalEntity.getStudent();
            final Student student = converter.convertToStudent(studentEntity);
            return student.getStudentPersonal();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentMedical saveStudentMedical(final StudentMedical studentMedical) {
        final StudentMedicalEntity studentMedicalEntity = studentDao.getStudentMedicalEntityById(Long.valueOf(studentMedical.getId()));

        if (studentMedicalEntity == null) {
            return null;
        } else {
            studentMedicalEntity.setDoctor(studentMedical.getDoctor());

            final StudentEntityConverter converter = new StudentEntityConverter();
            final StudentEntity studentEntity = studentMedicalEntity.getStudent();
            final Student student = converter.convertToStudent(studentEntity);
            return student.getStudentMedical();
        }
    }
}
