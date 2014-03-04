package net.scholagest.app.rest.ember.objects;

import java.util.ArrayList;
import java.util.List;

public class StudentResult extends Base {
    private String student;
    private String branchPeriod;
    private List<String> results;
    private String mean;

    public StudentResult(final String id, final String student, final String branchPeriod, final List<String> results, final String mean) {
        super(id);
        this.student = student;
        this.branchPeriod = branchPeriod;
        this.results = new ArrayList<>(results);
        this.mean = mean;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(final String student) {
        this.student = student;
    }

    public String getBranchPeriod() {
        return branchPeriod;
    }

    public void setBranchPeriod(final String branchPeriod) {
        this.branchPeriod = branchPeriod;
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
