package net.scholagest.app.rest.ember.objects;

import java.util.List;

public class Period {
    private String id;
    private String name;
    private String clazz;
    private List<String> branchPeriods;

    public Period() {}

    public Period(final String id, final String name, final String clazz, final List<String> branchPeriods) {
        this.id = id;
        this.name = name;
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
