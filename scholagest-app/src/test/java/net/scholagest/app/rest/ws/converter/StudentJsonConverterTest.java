package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.StudentClassJson;
import net.scholagest.app.rest.ws.objects.StudentJson;
import net.scholagest.app.rest.ws.objects.StudentMedicalJson;
import net.scholagest.app.rest.ws.objects.StudentPersonalJson;
import net.scholagest.object.Student;
import net.scholagest.object.StudentClass;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;

import org.junit.Test;

/**
 * Test class for {@link StudentJsonConverter}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentJsonConverterTest {
    @Test
    public void testConvertToStudentJsonList() {
        final Student student1 = new Student("student1", "firstName1", "lastName1", new StudentPersonal(), new StudentMedical(), new StudentClass());
        final Student student2 = new Student("student2", "firstName2", "lastName2", new StudentPersonal(), new StudentMedical(), new StudentClass());

        final List<Student> toConvert = Arrays.asList(student1, student2);
        final StudentJsonConverter testee = spy(new StudentJsonConverter());

        final List<StudentJson> result = testee.convertToStudentJsonList(toConvert);

        assertEquals(toConvert.size(), result.size());

        for (final Student student : toConvert) {
            verify(testee).convertToStudentJson(eq(student));
        }
    }

    @Test
    public void testConvertToStudentJson() {
        final Student student = new Student("student", "firstName", "lastName", new StudentPersonal("personal", null, null, null, null),
                new StudentMedical("medical", null), new StudentClass("classes", new ArrayList<String>(), new ArrayList<String>()));
        final StudentJson converted = new StudentJsonConverter().convertToStudentJson(student);

        assertEquals(student.getId(), converted.getId());
        assertEquals(student.getFirstname(), converted.getFirstName());
        assertEquals(student.getLastname(), converted.getLastName());
        assertEquals(student.getStudentClasses().getId(), converted.getClasses());
        assertEquals(student.getStudentPersonal().getId(), converted.getPersonal());
        assertEquals(student.getStudentMedical().getId(), converted.getMedical());
    }

    @Test
    public void testConvertToStudent() {
        final StudentJson studentJson = new StudentJson("student", "firstName", "lastName", "1", "1", "1");
        final Student converted = new StudentJsonConverter().convertToStudent(studentJson);

        assertEquals(studentJson.getId(), converted.getId());
        assertEquals(studentJson.getFirstName(), converted.getFirstname());
        assertEquals(studentJson.getLastName(), converted.getLastname());
        assertEquals(studentJson.getPersonal(), converted.getStudentPersonal().getId());
        assertEquals(studentJson.getMedical(), converted.getStudentMedical().getId());
    }

    @Test
    public void testConvertToStudentPersonalJson() {
        final StudentPersonal studentPersonal = new StudentPersonal("id", "street", "city", "postcode", "religion");
        final StudentPersonalJson converted = new StudentJsonConverter().convertToStudentPersonalJson(studentPersonal);

        assertEquals(studentPersonal.getId(), converted.getId());
        assertEquals(studentPersonal.getStreet(), converted.getStreet());
        assertEquals(studentPersonal.getCity(), converted.getCity());
        assertEquals(studentPersonal.getPostcode(), converted.getPostcode());
        assertEquals(studentPersonal.getReligion(), converted.getReligion());
    }

    @Test
    public void testConvertToStudentPersonal() {
        final StudentPersonalJson studentPersonalJson = new StudentPersonalJson("id", "street", "city", "postcode", "religion");
        final StudentPersonal converted = new StudentJsonConverter().convertToStudentPersonal(studentPersonalJson);

        assertEquals(studentPersonalJson.getId(), converted.getId());
        assertEquals(studentPersonalJson.getStreet(), converted.getStreet());
        assertEquals(studentPersonalJson.getCity(), converted.getCity());
        assertEquals(studentPersonalJson.getPostcode(), converted.getPostcode());
        assertEquals(studentPersonalJson.getReligion(), converted.getReligion());
    }

    @Test
    public void testConvertToStudentMedicalJson() {
        final StudentMedical studentMedical = new StudentMedical("id", "doctor");
        final StudentMedicalJson converted = new StudentJsonConverter().convertToStudentMedicalJson(studentMedical);

        assertEquals(studentMedical.getId(), converted.getId());
        assertEquals(studentMedical.getDoctor(), converted.getDoctor());
    }

    @Test
    public void testConvertToStudentMedical() {
        final StudentMedicalJson studentMedicalJson = new StudentMedicalJson("id", "doctor");
        final StudentMedical converted = new StudentJsonConverter().convertToStudentMedical(studentMedicalJson);

        assertEquals(studentMedicalJson.getId(), converted.getId());
        assertEquals(studentMedicalJson.getDoctor(), converted.getDoctor());
    }

    @Test
        public void testConvertToStudentClassJson() {
            final StudentClass studentClasses = new StudentClass("id", Arrays.asList("clazz1", "clazz2"), Arrays.asList("clazz3", "clazz4", "clazz5"));
            final StudentClassJson converted = new StudentJsonConverter().convertToStudentClassJson(studentClasses);
    
            assertEquals(studentClasses.getId(), converted.getId());
            assertEquals(studentClasses.getCurrentClasses(), converted.getCurrentClasses());
            assertEquals(studentClasses.getOldClasses(), converted.getOldClasses());
        }
}
