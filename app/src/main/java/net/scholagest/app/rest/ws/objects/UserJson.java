package net.scholagest.app.rest.ws.objects;

/**
 * Json object representing a user
 * 
 * @author CLA
 * @since 0.13.0
 */
public class UserJson extends BaseJson {
    private String role;
    private String teacher;
    private String clazz;

    public UserJson() {}

    public UserJson(final String id, final String role, final String teacher, final String clazz) {
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
