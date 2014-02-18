package net.scholagest.app.rest.ember.objects;

import java.util.List;

public class BranchPeriod {
    private String id;
    private String branch;
    private String period;
    private List<String> exams;
    private List<String> studentResults;

    public BranchPeriod() {}

    public BranchPeriod(final String id, final String branch, final String period, final List<String> exams, final List<String> studentResults) {
        this.id = id;
        this.branch = branch;
        this.period = period;
        this.exams = exams;
        this.studentResults = studentResults;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(final String branch) {
        this.branch = branch;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(final String period) {
        this.period = period;
    }

    public List<String> getExams() {
        return exams;
    }

    public void setExams(final List<String> exams) {
        this.exams = exams;
    }

    public List<String> getStudentResults() {
        return studentResults;
    }

    public void setStudentResults(final List<String> studentResults) {
        this.studentResults = studentResults;
    }
}
