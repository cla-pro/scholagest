package net.scholagest.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent a period.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class Period extends Base {
    private String name;
    private String clazz;
    private List<String> branchPeriods;

    public Period() {
        this.branchPeriods = new ArrayList<String>();
    }

    public Period(final Period toCopy) {
        this(toCopy.getId(), toCopy.name, toCopy.clazz, toCopy.branchPeriods);
    }

    public Period(final String id, final String name, final String clazz, final List<String> branchPeriods) {
        super(id);
        this.name = name;
        this.clazz = clazz;
        this.branchPeriods = new ArrayList<String>(branchPeriods);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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
        } else if (!(that instanceof Period)) {
            return false;
        }

        final Period other = (Period) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(name, other.name).append(clazz, other.clazz).isEquals();
    }
}
