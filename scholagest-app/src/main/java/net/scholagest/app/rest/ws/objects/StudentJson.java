package net.scholagest.app.rest.ws.objects;

/**
 * Json object representing a student
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentJson extends BaseJson {
    private String firstname;
    private String lastname;
    private String personal;
    private String medical;
    private String classes;

    public StudentJson() {}

    public StudentJson(final String id, final String firstname, final String lastname, final String personal, final String medical,
            final String classes) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.personal = personal;
        this.medical = medical;
        this.classes = classes;
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

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(final String personal) {
        this.personal = personal;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(final String medical) {
        this.medical = medical;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(final String classes) {
        this.classes = classes;
    }
}
