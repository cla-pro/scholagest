package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.ExamEntity;
import net.scholagest.object.Exam;

/**
 * Method to convert from the jpa entity {@link ExamEntity} to the transfer object {@link Exam} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ExamEntityConverter {
    /**
     * Convenient method to convert a list of {@link ExamEntity} to a list of {@link Exam}
     *  
     * @param examEntityList The list to convert
     * @return The converted list
     */
    public List<Exam> convertToExamList(final List<ExamEntity> examEntityList) {
        final List<Exam> examList = new ArrayList<>();

        for (final ExamEntity examEntity : examEntityList) {
            examList.add(convertToExam(examEntity));
        }

        return examList;
    }

    /**
     * Convert a {@link ExamEntity} to its transfer version {@link Exam}.
     * 
     * @param examEntity The exam entity to convert
     * @return The converted exam
     */
    public Exam convertToExam(final ExamEntity examEntity) {
        final Exam exam = new Exam();
        exam.setId("" + examEntity.getId());
        exam.setName(examEntity.getName());
        exam.setCoeff(examEntity.getCoeff());
        exam.setBranchPeriod("" + examEntity.getBranchPeriod().getId());

        return exam;
    }

    /**
     * Convert a {@link Exam} to the entity {@link ExamEntity}.
     * 
     * @param exam The exam to convert
     * @return The converted exam entity
     */
    public ExamEntity convertToExamEntity(final Exam exam) {
        final ExamEntity examEntity = new ExamEntity();
        examEntity.setName(exam.getName());
        examEntity.setCoeff(exam.getCoeff());
        examEntity.setBranchPeriod(null);

        return examEntity;
    }
}
