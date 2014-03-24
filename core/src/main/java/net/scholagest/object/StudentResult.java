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
public class StudentResult extends Base {
    private String student;
    private String branchPeriod;
    private List<String> results;
    private String mean;
    private boolean active;

    public StudentResult() {
        this.results = new ArrayList<String>();
    }

    public StudentResult(final StudentResult toCopy) {
        this(toCopy.getId(), toCopy.student, toCopy.branchPeriod, toCopy.results, toCopy.mean, toCopy.active);
    }

    public StudentResult(final String id, final String student, final String branchPeriod, final List<String> results, final String mean,
            final boolean active) {
        super(id);
        this.student = student;
        this.branchPeriod = branchPeriod;
        this.active = active;
        this.results = new ArrayList<>(results);
        this.mean = mean;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(final String student) {
        this.student = student;
    }

    public String getBranchPeriod() {
        return branchPeriod;
    }

    public void setBranchPeriod(final String branchPeriod) {
        this.branchPeriod = branchPeriod;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(final List<String> results) {
        this.results = results;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(final String mean) {
        this.mean = mean;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof StudentResult)) {
            return false;
        }

        final StudentResult other = (StudentResult) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(student, other.student).append(branchPeriod, other.branchPeriod).isEquals();
    }
}
