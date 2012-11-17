package net.scholagest.business;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IStudentManager;
import net.scholagest.services.kdom.DBToKdomConverter;

import com.google.inject.Inject;

public class StudentBusinessComponent implements IStudentBusinessComponent {
    private IStudentManager studentManager;

    @Inject
    public StudentBusinessComponent(IStudentManager studentManager) {
        this.studentManager = studentManager;
    }

    @Override
    public String createStudent(String requestId, ITransaction transaction, Map<String, Object> personalProperties) throws Exception {
        String studentKey = studentManager.createStudent(requestId, transaction);

        if (personalProperties != null) {
            studentManager.setPersonalProperties(requestId, transaction, studentKey, personalProperties);
        }

        return studentKey;
    }

    @Override
    public void updateStudentProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> personalProperties,
            Map<String, Object> medicalProperties) throws Exception {
        studentManager.setPersonalProperties(requestId, transaction, studentKey, personalProperties);
        studentManager.setMedicalProperties(requestId, transaction, studentKey, medicalProperties);
    }

    @Override
    public Map<String, Object> getStudentPersonalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception {
        Map<String, Object> personalProperties = studentManager.getPersonalProperties(requestId, transaction, studentKey, properties);
        return new DBToKdomConverter().convertDBToKdom(personalProperties);
    }

    @Override
    public Map<String, Object> getStudentMedicalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception {
        Map<String, Object> medicalProperties = studentManager.getMedicalProperties(requestId, transaction, studentKey, properties);
        return new DBToKdomConverter().convertDBToKdom(medicalProperties);
    }

    @Override
    public Map<String, Map<String, Object>> getStudentsWithProperties(String requestId, ITransaction transaction, Set<String> properties)
            throws Exception {
        Map<String, Map<String, Object>> students = new HashMap<String, Map<String, Object>>();

        for (String studentKey : studentManager.getStudents(requestId, transaction)) {
            Map<String, Object> info = this.studentManager.getPersonalProperties(requestId, transaction, studentKey, properties);
            students.put(studentKey, info);
        }

        return students;
    }
}
