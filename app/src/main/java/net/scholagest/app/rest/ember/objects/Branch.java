package net.scholagest.app.rest.ember.objects;

import java.util.ArrayList;
import java.util.List;

public class Branch extends Base {
    private String name;
    private boolean numerical;
    private List<String> branchPeriods;
    private String clazz;

    public Branch() {}

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
}
