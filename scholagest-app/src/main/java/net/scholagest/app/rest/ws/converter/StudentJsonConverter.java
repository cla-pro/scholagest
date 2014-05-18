package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.StudentClassJson;
import net.scholagest.app.rest.ws.objects.StudentJson;
import net.scholagest.app.rest.ws.objects.StudentMedicalJson;
import net.scholagest.app.rest.ws.objects.StudentPersonalJson;
import net.scholagest.object.Student;
import net.scholagest.object.StudentClass;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;

/**
 * Method to convert from transfer object {@link Student} to json {@link StudentJson} and reverse.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentJsonConverter {
    /**
     * Convenient method to convert a list of {@link Student} to a list of {@link StudentJson}
     *  
     * @param studentList The list to convert
     * @return The converted list
     */
    public List<StudentJson> convertToStudentJsonList(final List<Student> studentList) {
        final List<StudentJson> studentJsonList = new ArrayList<>();

        for (final Student student : studentList) {
            studentJsonList.add(convertToStudentJson(student));
        }

        return studentJsonList;
    }

    /**
     * Convert a {@link Student} to its json version {@link StudentJson}. The {@link StudentPersonal} and {@link StudentMedical}
     * are copied only as reference (id).
     * 
     * @param student The student to convert
     * @return The converted student json
     */
    public StudentJson convertToStudentJson(final Student student) {
        final StudentJson studentJson = new StudentJson();

        studentJson.setId(student.getId());
        studentJson.setFirstname(student.getFirstname());
        studentJson.setLastname(student.getLastname());
        studentJson.setClasses(student.getStudentClasses().getId());
        studentJson.setPersonal(student.getStudentPersonal().getId());
        studentJson.setMedical(student.getStudentMedical().getId());

        return studentJson;
    }

    /**
     * Convert a {@link StudentJson} to its version {@link Student}. Empty {@link StudentPersonal} and {@link StudentMedical}
     * are created with the ids from the json.
     * 
     * @param clazzJson The clazz json to convert
     * @return The converted clazz
     */
    public Student convertToStudent(final StudentJson studentJson) {
        final Student student = new Student();

        student.setId(studentJson.getId());
        student.setFirstname(studentJson.getFirstname());
        student.setLastname(studentJson.getLastname());
        student.setStudentPersonal(new StudentPersonal(studentJson.getPersonal(), null, null, null, null));
        student.setStudentMedical(new StudentMedical(studentJson.getMedical(), null));

        return student;
    }

    /**
     * Convert a {@link StudentPersonal} to its json version {@link StudentPersonalJson}
     * 
     * @param studentPersonal The student personal to convert
     * @return The converted student personal json
     */
    public StudentPersonalJson convertToStudentPersonalJson(final StudentPersonal studentPersonal) {
        final StudentPersonalJson studentPersonalJson = new StudentPersonalJson();

        studentPersonalJson.setId(studentPersonal.getId());
        studentPersonalJson.setStreet(studentPersonal.getStreet());
        studentPersonalJson.setCity(studentPersonal.getCity());
        studentPersonalJson.setPostcode(studentPersonal.getPostcode());
        studentPersonalJson.setReligion(studentPersonal.getReligion());

        return studentPersonalJson;
    }

    /**
     * Convert a {@link StudentPersonalJson} to its version {@link StudentPersonal}.
     * 
     * @param studentPersonalJson The student personal json to convert
     * @return The converted student personal
     */
    public StudentPersonal convertToStudentPersonal(final StudentPersonalJson studentPersonalJson) {
        final StudentPersonal studentPersonal = new StudentPersonal();

        studentPersonal.setId(studentPersonalJson.getId());
        studentPersonal.setStreet(studentPersonalJson.getStreet());
        studentPersonal.setCity(studentPersonalJson.getCity());
        studentPersonal.setPostcode(studentPersonalJson.getPostcode());
        studentPersonal.setReligion(studentPersonalJson.getReligion());

        return studentPersonal;
    }

    /**
     * Convert a {@link StudentMedical} to its json version {@link StudentMedicalJson}
     * 
     * @param studentMedical The student medical to convert
     * @return The converted student medical json
     */
    public StudentMedicalJson convertToStudentMedicalJson(final StudentMedical studentMedical) {
        final StudentMedicalJson studentMedicalJson = new StudentMedicalJson();

        studentMedicalJson.setId(studentMedical.getId());
        studentMedicalJson.setDoctor(studentMedical.getDoctor());

        return studentMedicalJson;
    }

    /**
     * Convert a {@link StudentMedicalJson} to its version {@link StudentMedical}.
     * 
     * @param studentMedicalJson The student medical json to convert
     * @return The converted student medical
     */
    public StudentMedical convertToStudentMedical(final StudentMedicalJson studentMedicalJson) {
        final StudentMedical studentMedical = new StudentMedical();

        studentMedical.setId(studentMedicalJson.getId());
        studentMedical.setDoctor(studentMedicalJson.getDoctor());

        return studentMedical;
    }

    /**
     * Convert a {@link StudentClass} to its json version {@link StudentClassJson}
     * 
     * @param studentClass The student classes to convert
     * @return The converted student classes json
     */
    public StudentClassJson convertToStudentClassJson(final StudentClass studentClass) {
        final StudentClassJson studentClassJson = new StudentClassJson();

        studentClassJson.setId(studentClass.getId());
        studentClassJson.setCurrentClasses(new ArrayList<String>(studentClass.getCurrentClasses()));
        studentClassJson.setOldClasses(new ArrayList<String>(studentClass.getOldClasses()));

        return studentClassJson;
    }
}
