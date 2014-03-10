package net.scholagest.app.rest.ws.objects;

public class User extends Base {
    private String role;
    private String teacher;
    private String clazz;

    public User(final String id, final String role, final String teacher, final String clazz) {
        super(id);
        this.role = role;
        this.teacher = teacher;
        this.clazz = clazz;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
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
