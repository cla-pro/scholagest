package net.scholagest.app.rest.ws.objects;

/**
 * Json object representing a teacher
 * 
 * @author CLA
 * @since 0.13.0
 */
public class TeacherJson extends BaseJson {
    private String firstname;
    private String lastname;
    private String detail;

    public TeacherJson() {}

    public TeacherJson(final String id, final String firstname, final String lastname, final String detail) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.detail = detail;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(final String detail) {
        this.detail = detail;
    }
}
