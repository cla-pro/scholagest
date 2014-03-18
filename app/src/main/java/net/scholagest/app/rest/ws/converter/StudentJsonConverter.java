package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.StudentJson;
import net.scholagest.app.rest.ws.objects.StudentMedicalJson;
import net.scholagest.app.rest.ws.objects.StudentPersonalJson;
import net.scholagest.object.Student;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;

/**
 * Method to convert from transfer object {@link Student} to json {@link StudentJson} and reverse.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentJsonConverter {
    public List<StudentJson> convertToStudentJson(final List<Student> studentList) {
        final List<StudentJson> studentJsonList = new ArrayList<>();

        for (final Student student : studentList) {
            studentJsonList.add(convertToStudentJson(student));
        }

        return studentJsonList;
    }

    public StudentJson convertToStudentJson(final Student student) {
        final StudentJson studentJson = new StudentJson();

        studentJson.setId(student.getId());
        studentJson.setFirstName(student.getFirstName());
        studentJson.setLastName(student.getLastName());
        studentJson.setClazz(null);
        studentJson.setPersonal(student.getStudentPersonal().getId());
        studentJson.setMedical(student.getStudentMedical().getId());

        return studentJson;
    }

    public Student convertToStudent(final StudentJson studentJson) {
        final Student student = new Student();

        student.setId(studentJson.getId());
        student.setFirstName(studentJson.getFirstName());
        student.setLastName(studentJson.getLastName());
        student.setStudentPersonal(new StudentPersonal(studentJson.getPersonal(), null, null, null, null));
        student.setStudentMedical(new StudentMedical(studentJson.getMedical(), null));

        return student;
    }

    public StudentPersonalJson convertToStudentPersonalJson(final StudentPersonal studentPersonal) {
        final StudentPersonalJson studentPersonalJson = new StudentPersonalJson();

        studentPersonalJson.setId(studentPersonal.getId());
        studentPersonalJson.setStreet(studentPersonal.getStreet());
        studentPersonalJson.setCity(studentPersonal.getCity());
        studentPersonalJson.setPostcode(studentPersonal.getPostcode());
        studentPersonalJson.setReligion(studentPersonal.getReligion());

        return studentPersonalJson;
    }

    public StudentPersonal convertToStudentPersonal(final StudentPersonalJson studentPersonalJson) {
        final StudentPersonal studentPersonal = new StudentPersonal();

        studentPersonal.setId(studentPersonalJson.getId());
        studentPersonal.setStreet(studentPersonalJson.getStreet());
        studentPersonal.setCity(studentPersonalJson.getCity());
        studentPersonal.setPostcode(studentPersonalJson.getPostcode());
        studentPersonal.setReligion(studentPersonalJson.getReligion());

        return studentPersonal;
    }

    public StudentMedicalJson convertToStudentMedicalJson(final StudentMedical studentMedical) {
        final StudentMedicalJson studentMedicalJson = new StudentMedicalJson();

        studentMedicalJson.setId(studentMedical.getId());
        studentMedicalJson.setDoctor(studentMedical.getDoctor());

        return studentMedicalJson;
    }

    public StudentMedical convertToStudentMedical(final StudentMedicalJson studentMedicalJson) {
        final StudentMedical studentMedical = new StudentMedical();

        studentMedical.setId(studentMedicalJson.getId());
        studentMedical.setDoctor(studentMedicalJson.getDoctor());

        return studentMedical;
    }
}
