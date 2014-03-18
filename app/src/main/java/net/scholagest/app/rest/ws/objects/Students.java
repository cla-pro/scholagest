package net.scholagest.app.rest.ws.objects;

import java.util.List;

public class Students {
    private List<StudentJson> students;

    public Students() {}

    public Students(List<StudentJson> students) {
        this.students = students;
    }

    public List<StudentJson> getStudents() {
        return students;
    }

    public void setStudents(List<StudentJson> students) {
        this.students = students;
    }
}
