package net.scholagest.app.rest.ws.objects;

/**
 * Json object representing a student medical
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentMedicalJson extends BaseJson {
    private String doctor;

    public StudentMedicalJson() {}

    public StudentMedicalJson(final String id, final String doctor) {
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
