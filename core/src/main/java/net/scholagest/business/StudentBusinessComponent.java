package net.scholagest.business;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IStudentManager;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.kdom.DBToKdomConverter;

import com.google.inject.Inject;

public class StudentBusinessComponent implements IStudentBusinessComponent {
    private IStudentManager studentManager;

    private DBToKdomConverter converter = new DBToKdomConverter();

    @Inject
    public StudentBusinessComponent(IStudentManager studentManager) {
        this.studentManager = studentManager;
    }

    @Override
    public BaseObject createStudent(String requestId, ITransaction transaction, Map<String, Object> personalProperties) throws Exception {
        BaseObject student = studentManager.createStudent(requestId, transaction);

        if (personalProperties != null) {
            studentManager.setPersonalProperties(requestId, transaction, student.getKey(), personalProperties);
        }

        return converter.convertDbToKdom(student);
    }

    @Override
    public void updateStudentProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> personalProperties,
            Map<String, Object> medicalProperties) throws Exception {
        studentManager.setPersonalProperties(requestId, transaction, studentKey, personalProperties);
        studentManager.setMedicalProperties(requestId, transaction, studentKey, medicalProperties);
    }

    @Override
    public BaseObject getStudentPersonalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception {
        BaseObject personalProperties = studentManager.getPersonalProperties(requestId, transaction, studentKey, properties);
        return converter.convertDbToKdom(personalProperties);
    }

    @Override
    public BaseObject getStudentMedicalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception {
        BaseObject medicalProperties = studentManager.getMedicalProperties(requestId, transaction, studentKey, properties);
        return converter.convertDbToKdom(medicalProperties);
    }

    @Override
    public Set<BaseObject> getStudentsWithProperties(String requestId, ITransaction transaction, Set<String> properties) throws Exception {
        Set<BaseObject> students = new HashSet<>();

        for (BaseObject student : studentManager.getStudents(requestId, transaction)) {
            BaseObject personalInfo = studentManager.getPersonalProperties(requestId, transaction, student.getKey(), properties);
            student.setProperties(personalInfo.getProperties());
            students.add(converter.convertDbToKdom(student));
        }

        return students;
    }
}
