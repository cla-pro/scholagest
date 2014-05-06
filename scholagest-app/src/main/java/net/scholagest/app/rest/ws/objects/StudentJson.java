package net.scholagest.app.rest.ws.objects;

/**
 * Json object representing a student
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentJson extends BaseJson {
    private String firstName;
    private String lastName;
    private String personal;
    private String medical;
    private String classes;

    public StudentJson() {}

    public StudentJson(final String id, final String firstName, final String lastName, final String personal, final String medical,
            final String classes) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personal = personal;
        this.medical = medical;
        this.classes = classes;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
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
