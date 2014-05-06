package net.scholagest.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a student personal in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "student_medical")
public class StudentMedicalEntity {
    private final static String TOSTRING_FORMAT = "StudentMedical [id=%d, doctor=%s]";

    @Id
    @SequenceGenerator(name = "seq_student_medical", sequenceName = "seq_student_medical_id")
    @GeneratedValue(generator = "seq_student_medical", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "doctor")
    private String doctor;

    @JoinColumn(name = "student_id")
    @OneToOne
    private StudentEntity student;

    public StudentMedicalEntity() {}

    public Long getId() {
        return id;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(final String doctor) {
        this.doctor = doctor;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(final StudentEntity student) {
        this.student = student;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, doctor);
    }
}
