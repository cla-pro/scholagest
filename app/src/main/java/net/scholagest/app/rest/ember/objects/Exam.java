package net.scholagest.app.rest.ember.objects;

import java.util.List;

public class Exam {
    private String id;
    private String name;
    private int coeff;
    private List<String> results;
    private final String branch;

    public Exam(final String id, final String name, final int coeff, final String branch, final List<String> results) {
        this.id = id;
        this.name = name;
        this.coeff = coeff;
        this.branch = branch;
        this.results = results;
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

    public int getCoeff() {
        return coeff;
    }

    public void setCoeff(int coeff) {
        this.coeff = coeff;
    }

    public String getBranch() {
        return branch;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }
}
