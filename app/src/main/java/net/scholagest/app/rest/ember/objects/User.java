package net.scholagest.app.rest.ember.objects;

public class User {
    private String id;
    private String teacher;
    private String clazz;

    public User(final String id, final String teacher, final String clazz) {
        this.id = id;
        this.teacher = teacher;
        this.clazz = clazz;
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

    public String getClazz() {
        return clazz;
    }
}
