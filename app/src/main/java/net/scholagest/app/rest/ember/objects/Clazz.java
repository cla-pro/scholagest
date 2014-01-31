package net.scholagest.app.rest.ember.objects;

import java.util.ArrayList;
import java.util.List;

public class Clazz {
    private String id;
    private String name;
    private List<String> periods;
    private List<String> teachers;
    private List<String> students;

    public Clazz() {
        this.teachers = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public Clazz(final String id, final String name, final List<String> periods, final List<String> teachers, final List<String> students) {
        this.id = id;
        this.name = name;
        this.periods = periods;
        this.teachers = teachers;
        this.students = students;
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

    public List<String> getPeriods() {
        return periods;
    }

    public void setPeriods(List<String> periods) {
        this.periods = periods;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }
}
