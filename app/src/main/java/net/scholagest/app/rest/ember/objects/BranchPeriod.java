package net.scholagest.app.rest.ember.objects;

import java.util.ArrayList;
import java.util.List;

public class BranchPeriod extends Base {
    private String branch;
    private String period;
    private List<String> exams;
    private List<String> studentResults;

    public BranchPeriod() {}

    public BranchPeriod(final String id, final String branch, final String period, final List<String> exams, final List<String> studentResults) {
        super(id);
        this.branch = branch;
        this.period = period;
        this.exams = new ArrayList<String>(exams);
        this.studentResults = new ArrayList<String>(studentResults);
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
