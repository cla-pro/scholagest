package net.scholagest.app.rest.ember.objects;

public class Student extends Base {
    private String firstName;
    private String lastName;
    private String personal;
    private String medical;
    private String clazz;

    public Student() {}

    public Student(final String id, final String firstName, final String lastName, final String personal, final String medical, final String clazz) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personal = personal;
        this.medical = medical;
        this.clazz = clazz;
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

    public String getClazz() {
        return clazz;
    }

    public void setClazz(final String clazz) {
        this.clazz = clazz;
    }
}
