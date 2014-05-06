package net.scholagest.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.StudentDaoLocal;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentMedicalEntity;
import net.scholagest.db.entity.StudentPersonalEntity;
import net.scholagest.object.Student;
import net.scholagest.object.StudentClass;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link StudentBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentBusinessBeanTest {
    @Mock
    private StudentDaoLocal studentDao;

    @InjectMocks
    private final StudentBusinessLocal testee = new StudentBusinessBean();

    @Test
    public void testGetStudents() {
        final StudentEntity studentEntity1 = createStudentEntity(1L, "firstname1", "lastname1");
        final StudentEntity studentEntity2 = createStudentEntity(2L, "firstname2", "lastname2");
        final List<StudentEntity> studentEntityList = Arrays.asList(studentEntity1, studentEntity2);

        when(studentDao.getAllStudentEntity()).thenReturn(studentEntityList);

        final List<Student> students = testee.getStudents();
        assertEquals(studentEntityList.size(), students.size());
    }

    @Test
    public void getStudent() {
        final StudentEntity studentEntity = createStudentEntity(1L, "firstname", "lastname");

        when(studentDao.getStudentEntityById(eq(1L))).thenReturn(studentEntity);

        assertNull(testee.getStudent(2L));
        verify(studentDao).getStudentEntityById(eq(2L));

        final Student student = testee.getStudent(1L);
        assertEquals("" + studentEntity.getId(), student.getId());
        verify(studentDao).getStudentEntityById(eq(1L));
    }

    @Test
    public void testCreateStudent() {
        final Student student = new Student("2", "firstname", "lastname", new StudentPersonal(), new StudentMedical(), new StudentClass());
        final StudentEntity studentEntity = createStudentEntity(1L, "firstname", "lastname");

        when(studentDao.persistStudentEntity(any(StudentEntity.class))).thenReturn(studentEntity);
        final Student created = testee.createStudent(student);

        assertEquals("The id must be the one from the db and not from the transfer object", "" + studentEntity.getId(), created.getId());
        verify(studentDao).persistStudentEntity(any(StudentEntity.class));
    }

    @Test
    public void testSaveStudent() {
        final Student student = new Student("1", "newFirstname", "newLastname", new StudentPersonal("1", "street", "city", "postcode", "religion"),
                new StudentMedical("1", "doctor"), new StudentClass());
        final StudentEntity studentEntity = createStudentEntity(1L, "firstname", "lastname");

        when(studentDao.getStudentEntityById(eq(1L))).thenReturn(studentEntity);

        assertNull(testee.saveStudent(new Student("2", "", "", new StudentPersonal(), new StudentMedical(), new StudentClass())));
        verify(studentDao).getStudentEntityById(eq(2L));

        final Student saved = testee.saveStudent(student);
        assertEquals(student.getFirstname(), studentEntity.getFirstname());
        assertEquals(student.getLastname(), studentEntity.getLastname());
        assertNull(studentEntity.getStudentPersonal().getStreet());
        assertNull(studentEntity.getStudentPersonal().getPostcode());
        assertNull(studentEntity.getStudentPersonal().getCity());
        assertNull(studentEntity.getStudentPersonal().getReligion());
        assertNull(studentEntity.getStudentMedical().getDoctor());

        assertEquals(student.getFirstname(), saved.getFirstname());
        assertEquals(student.getLastname(), saved.getLastname());

        verify(studentDao).getStudentEntityById(eq(1L));
    }

    @Test
    public void testSaveStudentPersonal() {
        final StudentPersonal studentPersonal = new StudentPersonal("2", "newStreet", "newCity", "newPostcode", "newReligion");
        final StudentPersonalEntity studentPersonalEntity = createStudentEntity(2L, null, null,
                createStudentPersonalEntity(3L, "street", "city", "postcode", "religion"), createStudentMedicalEntity(4L, null)).getStudentPersonal();

        when(studentDao.getStudentPersonalEntityById(eq(2L))).thenReturn(studentPersonalEntity);

        assertNull(testee.saveStudentPersonal(new StudentPersonal("3", null, null, null, null)));
        verify(studentDao).getStudentPersonalEntityById(eq(3L));

        final StudentPersonal saved = testee.saveStudentPersonal(studentPersonal);
        assertEquals(studentPersonal.getStreet(), saved.getStreet());
        assertEquals(studentPersonal.getPostcode(), saved.getPostcode());
        assertEquals(studentPersonal.getCity(), saved.getCity());
        assertEquals(studentPersonal.getReligion(), saved.getReligion());

        verify(studentDao).getStudentPersonalEntityById(eq(2L));
    }

    @Test
    public void testSaveStudentMedical() {
        final StudentMedical studentMedical = new StudentMedical("2", "newDoctor");
        final StudentMedicalEntity studentMedicalEntity = createStudentEntity(2L, null, null,
                createStudentPersonalEntity(3L, null, null, null, null), createStudentMedicalEntity(2L, "doctor")).getStudentMedical();

        when(studentDao.getStudentMedicalEntityById(eq(2L))).thenReturn(studentMedicalEntity);

        assertNull(testee.saveStudentMedical(new StudentMedical("3", null)));
        verify(studentDao).getStudentMedicalEntityById(eq(3L));

        final StudentMedical saved = testee.saveStudentMedical(studentMedical);
        assertEquals(studentMedical.getDoctor(), saved.getDoctor());

        verify(studentDao).getStudentMedicalEntityById(eq(2L));
    }

    private StudentEntity createStudentEntity(final long id, final String firstname, final String lastname) {
        return createStudentEntity(id, firstname, lastname, createStudentPersonalEntity(3L, null, null, null, null),
                createStudentMedicalEntity(3L, null));
    }

    private StudentEntity createStudentEntity(final long id, final String firstname, final String lastname,
            final StudentPersonalEntity studentPersonalEntity, final StudentMedicalEntity studentMedicalEntity) {
        final StudentEntity studentEntity = new StudentEntity();
        ReflectionUtils.setField(studentEntity, "id", Long.valueOf(3L));
        studentEntity.setFirstname(firstname);
        studentEntity.setLastname(lastname);
        studentEntity.setStudentPersonal(studentPersonalEntity);
        studentEntity.setStudentMedical(studentMedicalEntity);

        return studentEntity;
    }

    private StudentPersonalEntity createStudentPersonalEntity(final long id, final String street, final String postcode, final String city,
            final String religion) {
        final StudentPersonalEntity studentPersonalEntity = new StudentPersonalEntity();
        ReflectionUtils.setField(studentPersonalEntity, "id", Long.valueOf(3L));
        studentPersonalEntity.setStreet(street);
        studentPersonalEntity.setPostcode(postcode);
        studentPersonalEntity.setCity(city);
        studentPersonalEntity.setReligion(religion);

        return studentPersonalEntity;
    }

    private StudentMedicalEntity createStudentMedicalEntity(final long id, final String doctor) {
        final StudentMedicalEntity studentMedicalEntity = new StudentMedicalEntity();
        ReflectionUtils.setField(studentMedicalEntity, "id", Long.valueOf(3L));
        studentMedicalEntity.setDoctor(doctor);

        return studentMedicalEntity;
    }
}
