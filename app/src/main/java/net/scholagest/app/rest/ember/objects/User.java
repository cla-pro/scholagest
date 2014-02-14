package net.scholagest.app.rest.ember.objects;

public class User {
    private String id;
    private String teacher;

    public User(final String id, final String teacher) {
        this.id = id;
        this.teacher = teacher;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTeacher() {
        return teacher;
    }
}
