package net.scholagest.app.rest.ws.objects;

public class StudentMedical extends BaseJson {
    private String doctor;

    public StudentMedical() {}

    public StudentMedical(final String id, final String doctor) {
        super(id);
        this.doctor = doctor;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(final String doctor) {
        this.doctor = doctor;
    }
}
