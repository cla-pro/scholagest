package net.scholagest.app.rest.ember.objects;

public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String personal;
    private String medical;

    public Student() {}

    public Student(String id, String firstName, String lastName, String personal, String medical) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personal = personal;
        this.medical = medical;
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

    public String getPersonal() {
        return personal;
    }

    public String getMedical() {
        return medical;
    }
}
