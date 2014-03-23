package net.scholagest.object;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent a result.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class Result extends Base {
    private Double grade;
    private String studentResult;
    private String exam;

    public Result() {}

    public Result(final Result toCopy) {
        this(toCopy.getId(), toCopy.grade, toCopy.exam, toCopy.studentResult);
    }

    public Result(final String id, final Double grade, final String exam, final String studentResult) {
        super(id);
        this.grade = grade;
        this.exam = exam;
        this.studentResult = studentResult;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(final Double grade) {
        this.grade = grade;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(final String exam) {
        this.exam = exam;
    }

    public String getStudentResult() {
        return studentResult;
    }

    public void setStudentResult(final String studentResult) {
        this.studentResult = studentResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof Result)) {
            return false;
        }

        final Result other = (Result) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(exam, other.exam).append(studentResult, other.studentResult).isEquals();
    }
}
