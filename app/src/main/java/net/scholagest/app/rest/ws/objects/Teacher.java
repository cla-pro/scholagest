package net.scholagest.app.rest.ws.objects;

public class Teacher extends Base {
    private String firstName;
    private String lastName;
    private String detail;

    public Teacher() {}

    public Teacher(final String id, final String firstName, final String lastName, final String detail) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.detail = detail;
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

    public String getDetail() {
        return detail;
    }
}
