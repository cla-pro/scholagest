package net.scholagest.app.rest.ember.objects;

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

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
