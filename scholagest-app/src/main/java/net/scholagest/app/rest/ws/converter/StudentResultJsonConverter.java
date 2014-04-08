package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.StudentResultJson;
import net.scholagest.object.StudentResult;

/**
 * Method to convert from transfer object {@link StudentResult} to json {@link StudentResultJson} and reverse
 * 
 * @author CLA
 * @since 0.14.0
 */
public class StudentResultJsonConverter {
    /**
     * Convenient method to convert a list of {@link StudentResult} to a list of {@link StudentResultJson}
     *  
     * @param studentResultList The list to convert
     * @return The converted list
     */
    public List<StudentResultJson> convertToStudentResultJsonList(final List<StudentResult> studentResultList) {
        final List<StudentResultJson> studentResultJsonList = new ArrayList<>();

        for (final StudentResult studentResult : studentResultList) {
            studentResultJsonList.add(convertToStudentResultJson(studentResult));
        }

        return studentResultJsonList;
    }

    /**
     * Convert a {@link StudentResult} to its json version {@link StudentResultJson}
     * 
     * @param studentResult The student result to convert
     * @return The converted student result json
     */
    public StudentResultJson convertToStudentResultJson(final StudentResult studentResult) {
        final StudentResultJson studentResultJson = new StudentResultJson();

        studentResultJson.setId(studentResult.getId());
        studentResultJson.setStudent(studentResult.getStudent());
        studentResultJson.setBranchPeriod(studentResult.getBranchPeriod());
        studentResultJson.setActive(studentResult.isActive());
        studentResultJson.setResults(new ArrayList<>(studentResult.getResults()));
        studentResultJson.setMean(studentResult.getMean());

        return studentResultJson;
    }
}
