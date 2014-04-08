package net.scholagest.object;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent an exam.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class Exam extends Base {
    private String name;
    private int coeff;
    private String branchPeriod;

    public Exam() {}

    public Exam(final Exam toCopy) {
        this(toCopy.getId(), toCopy.name, toCopy.coeff, toCopy.branchPeriod);
    }

    public Exam(final String id, final String name, final int coeff, final String branchPeriod) {
        super(id);
        this.name = name;
        this.coeff = coeff;
        this.branchPeriod = branchPeriod;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getCoeff() {
        return coeff;
    }

    public void setCoeff(final int coeff) {
        this.coeff = coeff;
    }

    public String getBranchPeriod() {
        return branchPeriod;
    }

    public void setBranchPeriod(final String branchPeriod) {
        this.branchPeriod = branchPeriod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof Exam)) {
            return false;
        }

        final Exam other = (Exam) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(name, other.name).append(coeff, other.coeff)
                .append(branchPeriod, other.branchPeriod).isEquals();
    }
}
