package net.scholagest.app.rest.ember.objects;

import java.util.List;

public class Branch {
    private String id;
    private String name;
    private boolean numerical;
    private List<String> branchPeriods;
    private String clazz;

    public Branch() {}

    public Branch(final String id, final String name, final boolean numerical, final String clazz, final List<String> branchPeriods) {
        this.id = id;
        this.name = name;
        this.numerical = numerical;
        this.clazz = clazz;
        this.branchPeriods = branchPeriods;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
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
