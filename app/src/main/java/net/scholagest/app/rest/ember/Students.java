package net.scholagest.app.rest.ember;

import java.util.List;

public class Students {
    private List<Student> students;

    public Students() {}

    public Students(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
