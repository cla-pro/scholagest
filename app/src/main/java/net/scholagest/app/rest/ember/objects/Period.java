package net.scholagest.app.rest.ember.objects;

import java.util.List;

public class Period {
    private String id;
    private String name;
    private String clazz;
    private List<String> branches;

    public Period(final String id, final String name, final String clazz, final List<String> branches) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
        this.branches = branches;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }
}
