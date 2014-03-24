package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.ExamJson;
import net.scholagest.object.Exam;

/**
 * Method to convert from transfer object {@link Exam} to json {@link ExamJson} and reverse
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ExamJsonConverter {
    /**
     * Convenient method to convert a list of {@link Exam} to a list of {@link ExamJson}
     *  
     * @param examList The list to convert
     * @return The converted list
     */
    public List<ExamJson> convertToExamJsonList(final List<Exam> examList) {
        final List<ExamJson> examJsonList = new ArrayList<>();

        for (final Exam exam : examList) {
            examJsonList.add(convertToExamJson(exam));
        }

        return examJsonList;
    }

    /**
     * Convert a {@link Exam} to its json version {@link ExamJson}
     * 
     * @param exam The exam to convert
     * @return The converted exam json
     */
    public ExamJson convertToExamJson(final Exam exam) {
        final ExamJson examJson = new ExamJson();

        examJson.setId(exam.getId());
        examJson.setName(exam.getName());
        examJson.setCoeff(exam.getCoeff());
        examJson.setBranchPeriod(exam.getBranchPeriod());

        return examJson;
    }

    /**
     * Convert a {@link ExamJson} to its version {@link Exam}.
     * 
     * @param examJson The exam json to convert
     * @return The converted exam
     */
    public Exam convertToExam(final ExamJson examJson) {
        final Exam exam = new Exam();

        exam.setId(examJson.getId());
        exam.setName(examJson.getName());
        exam.setCoeff(examJson.getCoeff());
        exam.setBranchPeriod(examJson.getBranchPeriod());

        return exam;
    }
}
