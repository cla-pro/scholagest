package net.scholagest.app.rest.ws.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Json object representing a student result
 * 
 * @author CLA
 * @since 0.14.0
 */
public class StudentResultJson extends BaseJson {
    private String student;
    private String branchPeriod;
    private List<String> results;
    private String mean;
    private boolean active;

    public StudentResultJson() {
        this.results = new ArrayList<>();
    }

    public StudentResultJson(final String id, final String student, final String branchPeriod, final List<String> results, final String mean,
            final boolean active) {
        super(id);
        this.student = student;
        this.branchPeriod = branchPeriod;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }
}
