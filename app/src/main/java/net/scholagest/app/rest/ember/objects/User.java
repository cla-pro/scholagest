package net.scholagest.app.rest.ember.objects;

public class User extends Base {
    private String teacher;
    private String clazz;

    public User(final String id, final String teacher, final String clazz) {
        super(id);
        this.teacher = teacher;
        this.clazz = clazz;
    }

    public void setTeacher(final String teacher) {
        this.teacher = teacher;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setClazz(final String clazz) {
        this.clazz = clazz;
    }

    public String getClazz() {
        return clazz;
    }
}
