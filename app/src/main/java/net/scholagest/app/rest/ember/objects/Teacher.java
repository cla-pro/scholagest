package net.scholagest.app.rest.ember.objects;

public class Teacher {
    private String id;
    private String firstName;
    private String lastName;
    private String detail;

    public Teacher() {

    }

    public Teacher(final String id, final String firstName, final String lastName, final String detail) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDetail() {
        return detail;
    }
}
