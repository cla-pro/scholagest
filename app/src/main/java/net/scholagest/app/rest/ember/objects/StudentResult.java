package net.scholagest.app.rest.ember.objects;

import java.util.ArrayList;
import java.util.List;

public class StudentResult extends Base {
    private String student;
    private String branch;
    private List<String> results;
    private String mean;

    public StudentResult(final String id, final String student, final String branch, final List<String> results, final String mean) {
        super(id);
        this.student = student;
        this.branch = branch;
        this.results = new ArrayList<>(results);
        this.mean = mean;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(final String student) {
        this.student = student;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(final String branch) {
        this.branch = branch;
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
}
