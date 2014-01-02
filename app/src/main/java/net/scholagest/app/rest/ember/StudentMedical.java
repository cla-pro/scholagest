package net.scholagest.app.rest.ember;

public class StudentMedical {
    private String id;
    private String doctor;

    public StudentMedical() {}

    public StudentMedical(String id, String doctor) {
        this.id = id;
        this.doctor = doctor;
    }

    public String getId() {
        return id;
    }

    public String getDoctor() {
        return doctor;
    }
}
