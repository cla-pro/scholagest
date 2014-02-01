package net.scholagest.app.rest.ember.objects;

import java.util.List;

public class Branch {
    private String id;
    private String name;
    private List<String> exams;
    private List<String> studentResults;
    private String period;

    public Branch(final String id, final String name, final String period, final List<String> exams, final List<String> studentResults) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.exams = exams;
        this.studentResults = studentResults;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<String> getExams() {
        return exams;
    }

    public void setExams(List<String> exams) {
        this.exams = exams;
    }

    public List<String> getStudentResults() {
        return studentResults;
    }

    public void setStudentResults(List<String> studentResults) {
        this.studentResults = studentResults;
    }
}
