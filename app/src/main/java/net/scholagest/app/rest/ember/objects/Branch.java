package net.scholagest.app.rest.ember.objects;

import java.util.List;

public class Branch {
    private String id;
    private String name;
    private List<String> exams;
    private final String period;

    public Branch(final String id, final String name, final String period, final List<String> exams) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.exams = exams;
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

    public List<String> getExams() {
        return exams;
    }

    public void setExams(List<String> exams) {
        this.exams = exams;
    }
}
