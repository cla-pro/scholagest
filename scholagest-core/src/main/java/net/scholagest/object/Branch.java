package net.scholagest.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent a branch.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class Branch extends Base {
    private String name;
    private boolean numerical;
    private List<String> branchPeriods;
    private String clazz;

    public Branch() {
        this.branchPeriods = new ArrayList<>();
    }

    public Branch(final Branch toCopy) {
        this(toCopy.getId(), toCopy.name, toCopy.numerical, toCopy.clazz, toCopy.branchPeriods);
    }

    public Branch(final String id, final String name, final boolean numerical, final String clazz, final List<String> branchPeriods) {
        super(id);
        this.name = name;
        this.numerical = numerical;
        this.clazz = clazz;
        this.branchPeriods = new ArrayList<String>(branchPeriods);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isNumerical() {
        return numerical;
    }

    public void setNumerical(final boolean numerical) {
        this.numerical = numerical;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(final String clazz) {
        this.clazz = clazz;
    }

    public List<String> getBranchPeriods() {
        return branchPeriods;
    }

    public void setBranchPeriods(final List<String> branchPeriods) {
        this.branchPeriods = branchPeriods;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof Branch)) {
            return false;
        }

        final Branch other = (Branch) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(name, other.name).append(numerical, other.numerical).isEquals();
    }
}
