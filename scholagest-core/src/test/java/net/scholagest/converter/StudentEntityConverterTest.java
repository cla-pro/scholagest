package net.scholagest.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentMedicalEntity;
import net.scholagest.db.entity.StudentPersonalEntity;
import net.scholagest.object.Student;
import net.scholagest.object.StudentClass;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;

import org.junit.Test;

/**
 * Test class for {@link StudentEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class StudentEntityConverterTest {
    @Test
    public void testConvertToStudentList() {
        final StudentEntity teacherEntity1 = createStudentEntity(1L, "firstname1", "lastname1",
                createStudentPersonalEntity(11L, null, null, null, null), createStudentMedicalEntity(21L, null));
        final StudentEntity teacherEntity2 = createStudentEntity(2L, "firstname2", "lastname2",
                createStudentPersonalEntity(12L, null, null, null, null), createStudentMedicalEntity(22L, null));
        final List<StudentEntity> toConvert = Arrays.asList(teacherEntity1, teacherEntity2);

        final StudentEntityConverter testee = spy(new StudentEntityConverter());
        final List<Student> converted = testee.convertToStudentList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final StudentEntity studentEntity : toConvert) {
            verify(testee).convertToStudent(eq(studentEntity));
        }
    }

    @Test
    public void testConvertToStudent() {
        final StudentPersonalEntity studentPersonalEntity = createStudentPersonalEntity(11L, "street", "postcode", "city", "religion");
        final StudentMedicalEntity studentMedicalEntity = createStudentMedicalEntity(21L, "doctor");
        final StudentEntity studentEntity = createStudentEntity(1L, "firstname", "lastname", studentPersonalEntity, studentMedicalEntity);

        final StudentEntityConverter testee = spy(new StudentEntityConverter());
        final Student converted = testee.convertToStudent(studentEntity);

        assertEquals(studentEntity.getId().toString(), converted.getId());
        assertEquals(studentEntity.getFirstname(), converted.getFirstname());
        assertEquals(studentEntity.getLastname(), converted.getLastname());

        final StudentPersonal convertedStudentPersonal = converted.getStudentPersonal();
        assertEquals(studentPersonalEntity.getId().toString(), convertedStudentPersonal.getId());
        assertEquals(studentPersonalEntity.getStreet(), convertedStudentPersonal.getStreet());
        assertEquals(studentPersonalEntity.getPostcode(), convertedStudentPersonal.getPostcode());
        assertEquals(studentPersonalEntity.getCity(), convertedStudentPersonal.getCity());
        assertEquals(studentPersonalEntity.getReligion(), convertedStudentPersonal.getReligion());

        final StudentMedical convertedStudentMedical = converted.getStudentMedical();
        assertEquals(studentMedicalEntity.getId().toString(), convertedStudentMedical.getId());
        assertEquals(studentMedicalEntity.getDoctor(), convertedStudentMedical.getDoctor());

        final StudentClass convertedStudentClasses = converted.getStudentClasses();
        assertNull(convertedStudentClasses.getId());
        assertTrue(convertedStudentClasses.getCurrentClasses().isEmpty());
        assertTrue(convertedStudentClasses.getOldClasses().isEmpty());
    }

    @Test
    public void testConvertToStudentEntity() {
        final StudentPersonal studentPersonal = new StudentPersonal("3", "street", "postcode", "city", "religion");
        final StudentMedical studentMedical = new StudentMedical("4", "doctor");
        final StudentClass studentClass = new StudentClass("5", Arrays.asList("6", "7"), Arrays.asList("8", "9"));
        final Student student = new Student("4", "firstname", "lastname", studentPersonal, studentMedical, studentClass);

        final StudentEntityConverter testee = spy(new StudentEntityConverter());
        final StudentEntity converted = testee.convertToStudentEntity(student);

        assertNull(converted.getId());
        assertEquals(student.getFirstname(), converted.getFirstname());
        assertEquals(student.getLastname(), converted.getLastname());

        final StudentPersonalEntity convertedStudentPersonalEntity = converted.getStudentPersonal();
        assertNull(convertedStudentPersonalEntity.getId());
        assertEquals(studentPersonal.getStreet(), convertedStudentPersonalEntity.getStreet());
        assertEquals(studentPersonal.getPostcode(), convertedStudentPersonalEntity.getPostcode());
        assertEquals(studentPersonal.getCity(), convertedStudentPersonalEntity.getCity());
        assertEquals(studentPersonal.getReligion(), convertedStudentPersonalEntity.getReligion());
        assertEquals(converted, convertedStudentPersonalEntity.getStudent());

        final StudentMedicalEntity convertedStudentMedicalEntity = converted.getStudentMedical();
        assertNull(convertedStudentMedicalEntity.getId());
        assertEquals(studentMedical.getDoctor(), convertedStudentMedicalEntity.getDoctor());
        assertEquals(converted, convertedStudentMedicalEntity.getStudent());
    }

    private StudentEntity createStudentEntity(final Long id, final String firstname, final String lastname,
            final StudentPersonalEntity studentPersonalEntity, final StudentMedicalEntity studentMedicalEntity) {
        final StudentEntity studentEntity = new StudentEntity();
        ReflectionUtils.setField(studentEntity, "id", id);
        studentEntity.setFirstname(firstname);
        studentEntity.setLastname(lastname);
        studentEntity.setStudentPersonal(studentPersonalEntity);
        studentEntity.setStudentMedical(studentMedicalEntity);

        return studentEntity;
    }

    private StudentPersonalEntity createStudentPersonalEntity(final Long id, final String street, final String postcode, final String city,
            final String religion) {
        final StudentPersonalEntity studentPersonalEntity = new StudentPersonalEntity();
        ReflectionUtils.setField(studentPersonalEntity, "id", id);
        studentPersonalEntity.setStreet(street);
        studentPersonalEntity.setPostcode(postcode);
        studentPersonalEntity.setCity(city);
        studentPersonalEntity.setReligion(religion);

        return studentPersonalEntity;
    }

    private StudentMedicalEntity createStudentMedicalEntity(final Long id, final String doctor) {
        final StudentMedicalEntity studentMedicalEntity = new StudentMedicalEntity();
        ReflectionUtils.setField(studentMedicalEntity, "id", id);
        studentMedicalEntity.setDoctor(doctor);

        return studentMedicalEntity;
    }
}
