package net.scholagest.object;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent a mean.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class Mean extends Base {
    private String grade;
    private String studentResult;

    public Mean() {}

    public Mean(final Mean toCopy) {
        this(toCopy.getId(), toCopy.grade, toCopy.studentResult);
    }

    public Mean(final String id, final String grade, final String studentResult) {
        super(id);
        this.grade = grade;
        this.studentResult = studentResult;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(final String grade) {
        this.grade = grade;
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
        } else if (!(that instanceof Mean)) {
            return false;
        }

        final Mean other = (Mean) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(studentResult, other.studentResult).isEquals();
    }
}
