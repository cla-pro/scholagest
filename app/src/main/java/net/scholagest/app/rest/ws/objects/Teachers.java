package net.scholagest.app.rest.ws.objects;

import java.util.List;

public class Teachers {
    private List<TeacherJson> teachers;

    public Teachers() {

    }

    public Teachers(final List<TeacherJson> teachers) {
        this.teachers = teachers;
    }

    public List<TeacherJson> getTeachers() {
        return teachers;
    }
}
