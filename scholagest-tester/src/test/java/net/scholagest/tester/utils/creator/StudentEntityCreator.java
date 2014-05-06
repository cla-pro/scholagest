package net.scholagest.tester.utils.creator;

import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentMedicalEntity;
import net.scholagest.db.entity.StudentPersonalEntity;

/**
 * Utility class to create {@link StudentEntity}, {@link StudentPersonalEntity}, {@link StudentMedicalEntity} and {@link StudentClassEntity}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class StudentEntityCreator {
    public static StudentEntity createStudentEntity(final String firstname, final String lastname) {
        final StudentEntity studentEntity = new StudentEntity();
        studentEntity.setFirstname(firstname);
        studentEntity.setLastname(lastname);

        return studentEntity;
    }

    public static StudentPersonalEntity createStudentPersonalEntity(final String city, final String postcode, final String religion,
            final String street, final StudentEntity studentEntity) {
        final StudentPersonalEntity studentPersonalEntity = new StudentPersonalEntity();
        studentPersonalEntity.setCity(city);
        studentPersonalEntity.setPostcode(postcode);
        studentPersonalEntity.setReligion(religion);
        studentPersonalEntity.setStreet(street);
        studentPersonalEntity.setStudent(studentEntity);

        return studentPersonalEntity;
    }

    public static StudentMedicalEntity createStudentMedicalEntity(final String doctor, final StudentEntity studentEntity) {
        final StudentMedicalEntity studentMedicalEntity = new StudentMedicalEntity();
        studentMedicalEntity.setDoctor(doctor);
        studentMedicalEntity.setStudent(studentEntity);

        return studentMedicalEntity;
    }
}
