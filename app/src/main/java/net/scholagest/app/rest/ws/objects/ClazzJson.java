package net.scholagest.app.rest.ws.objects;

import java.util.ArrayList;
import java.util.List;

public class ClazzJson extends BaseJson {
    private String name;
    private String year;
    private List<String> periods;
    private List<String> teachers;
    private List<String> students;
    private List<String> branches;

    public ClazzJson() {
        this.teachers = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public ClazzJson(final String id, final String name, final String year, final List<String> periods, final List<String> teachers,
            final List<String> students, final List<String> branches) {
        super(id);
        this.name = name;
        this.year = year;
        this.periods = new ArrayList<String>(periods);
        this.teachers = new ArrayList<String>(teachers);
        this.students = new ArrayList<String>(students);
        this.branches = new ArrayList<String>(branches);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<String> getPeriods() {
        return periods;
    }

    public void setPeriods(final List<String> periods) {
        this.periods = periods;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(final List<String> teachers) {
        this.teachers = teachers;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(final List<String> students) {
        this.students = students;
    }

    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(final List<String> branches) {
        this.branches = branches;
    }
}
