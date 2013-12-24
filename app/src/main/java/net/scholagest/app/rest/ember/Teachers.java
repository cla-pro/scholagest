package net.scholagest.app.rest.ember;

import java.util.List;

public class Teachers {
    private List<Teacher> teachers;

    public Teachers() {

    }

    public Teachers(final List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
}
