package net.scholagest.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent a branch period.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchPeriod extends Base {
    private String branch;
    private String period;
    private List<String> exams;
    private String meanExam;
    private List<String> studentResults;

    public BranchPeriod() {
        this.exams = new ArrayList<>();
        this.studentResults = new ArrayList<>();
    }

    public BranchPeriod(final BranchPeriod toCopy) {
        this(toCopy.getId(), toCopy.branch, toCopy.period, toCopy.exams, toCopy.meanExam, toCopy.studentResults);
    }

    public BranchPeriod(final String id, final String branch, final String period, final List<String> exams, final String meanExam,
            final List<String> studentResults) {
        super(id);
        this.branch = branch;
        this.period = period;
        this.exams = new ArrayList<String>(exams);
        this.meanExam = meanExam;
        this.studentResults = new ArrayList<String>(studentResults);
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(final String branch) {
        this.branch = branch;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(final String period) {
        this.period = period;
    }

    public List<String> getExams() {
        return exams;
    }

    public void setExams(final List<String> exams) {
        this.exams = exams;
    }

    public String getMeanExam() {
        return meanExam;
    }

    public void setMeanExam(final String meanExam) {
        this.meanExam = meanExam;
    }

    public List<String> getStudentResults() {
        return studentResults;
    }

    public void setStudentResults(final List<String> studentResults) {
        this.studentResults = studentResults;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof BranchPeriod)) {
            return false;
        }

        final BranchPeriod other = (BranchPeriod) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(branch, other.branch).append(period, other.period).isEquals();
    }
}
