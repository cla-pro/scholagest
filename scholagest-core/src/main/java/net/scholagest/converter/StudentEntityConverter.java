package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentMedicalEntity;
import net.scholagest.db.entity.StudentPersonalEntity;
import net.scholagest.object.Student;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;

/**
 * Method to convert from the jpa entity {@link StudentEntity} to the transfer object {@link Student} as well as the {@link StudentPersonalEntity} 
 * and the {@link StudentMedicalEntity} to {@link StudentPersonal} and {@link StudentMedical} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class StudentEntityConverter {
    /**
     * Convenient method to convert a list of {@link StudentEntity} to a list of {@link Student}
     *  
     * @param studentEntityList The list to convert
     * @return The converted list
     */
    public List<Student> convertToStudentList(final List<StudentEntity> studentEntityList) {
        final List<Student> studentList = new ArrayList<>();

        for (final StudentEntity StudentEntity : studentEntityList) {
            studentList.add(convertToStudent(StudentEntity));
        }

        return studentList;
    }

    /**
     * Convert a {@link StudentEntity} to its transfer version {@link Student}. The {@link StudentPersonalEntity} as well as the
     * {@link StudentMedicalEntity} are converted as well.
     * 
     * @param studentEntity The student entity to convert
     * @return The converted student
     */
    public Student convertToStudent(final StudentEntity studentEntity) {
        final Student student = new Student();
        student.setId("" + studentEntity.getId());
        student.setFirstname(studentEntity.getFirstname());
        student.setLastname(studentEntity.getLastname());

        final StudentPersonalEntity studentPersonalEntity = studentEntity.getStudentPersonal();
        final StudentPersonal studentPersonal = student.getStudentPersonal();
        studentPersonal.setId("" + studentPersonalEntity.getId());
        studentPersonal.setStreet(studentPersonalEntity.getStreet());
        studentPersonal.setPostcode(studentPersonalEntity.getPostcode());
        studentPersonal.setCity(studentPersonalEntity.getCity());
        studentPersonal.setReligion(studentPersonalEntity.getReligion());

        final StudentMedicalEntity studentMedicalEntity = studentEntity.getStudentMedical();
        final StudentMedical studentMedical = student.getStudentMedical();
        studentMedical.setId("" + studentMedicalEntity.getId());
        studentMedical.setDoctor(studentMedicalEntity.getDoctor());

        return student;
    }

    /**
     * Convert a {@link Student} to the entity {@link StudentEntity}. The {@link StudentPersonal} as well as the
     * {@link StudentMedical} are converted as well.
     * 
     * @param student The student to convert
     * @return The converted student entity
     */
    public StudentEntity convertToStudentEntity(final Student student) {
        final StudentEntity studentEntity = new StudentEntity();
        studentEntity.setFirstname(student.getFirstname());
        studentEntity.setLastname(student.getLastname());

        final StudentPersonalEntity studentPersonalEntity = new StudentPersonalEntity();
        final StudentPersonal studentPersonal = student.getStudentPersonal();
        studentPersonalEntity.setStreet(studentPersonal.getStreet());
        studentPersonalEntity.setPostcode(studentPersonal.getPostcode());
        studentPersonalEntity.setCity(studentPersonal.getCity());
        studentPersonalEntity.setReligion(studentPersonal.getReligion());

        studentEntity.setStudentPersonal(studentPersonalEntity);

        final StudentMedicalEntity studentMedicalEntity = new StudentMedicalEntity();
        final StudentMedical studentMedical = student.getStudentMedical();
        studentMedicalEntity.setDoctor(studentMedical.getDoctor());

        studentEntity.setStudentMedical(studentMedicalEntity);

        return studentEntity;
    }
}
