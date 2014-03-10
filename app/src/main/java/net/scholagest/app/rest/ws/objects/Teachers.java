package net.scholagest.app.rest.ws.objects;

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
}
