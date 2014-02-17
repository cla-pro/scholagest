package net.scholagest.app.rest.ember.objects;

import java.util.List;

public class StudentResult {
    private String id;
    private String student;
    private String branch;
    private List<String> results;
    private String mean;

    public StudentResult(final String id, final String student, final String branch, final List<String> results, final String mean) {
        this.id = id;
        this.student = student;
        this.branch = branch;
        this.results = results;
        this.mean = mean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
}
