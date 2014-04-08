package net.scholagest.business;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.scholagest.object.StudentResult;

/**
 * Implementation of {@link StudentResultBusinessLocal}.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class StudentResultBusinessBean implements StudentResultBusinessLocal {
    public static Map<String, StudentResult> studentResultsMap = new HashMap<>();

    static {
        studentResultsMap.put("studentResult1", new StudentResult("studentResult1", "student1", "branchPeriod1", Arrays.asList("result1", "result2"),
                "result3", true));
        studentResultsMap.put("studentResult2",
                new StudentResult("studentResult2", "student1", "branchPeriod2", Arrays.asList("result4", "result5", "result6"), "result7", true));
    }

    StudentResultBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentResult getStudentResult(final String id) {
        if (studentResultsMap.containsKey(id)) {
            return studentResultsMap.get(id);
        } else {
            return null;
        }
    }
}
