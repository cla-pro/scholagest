package net.scholagest.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a student in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "student")
public class StudentEntity {
    private final static String TOSTRING_FORMAT = "Student [id=%d, firstname=%s, lastname=%s, personal=%s, medical=%s]";

    @Id
    @SequenceGenerator(name = "seq_student", sequenceName = "seq_student_id")
    @GeneratedValue(generator = "seq_student", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @OneToOne(mappedBy = "student", fetch = FetchType.EAGER)
    private StudentPersonalEntity studentPersonal;

    @OneToOne(mappedBy = "student", fetch = FetchType.EAGER)
    private StudentMedicalEntity studentMedical;

    public StudentEntity() {}

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public StudentPersonalEntity getStudentPersonal() {
        return studentPersonal;
    }

    public void setStudentPersonal(final StudentPersonalEntity studentPersonal) {
        this.studentPersonal = studentPersonal;
        studentPersonal.setStudent(this);
    }

    public StudentMedicalEntity getStudentMedical() {
        return studentMedical;
    }

    public void setStudentMedical(final StudentMedicalEntity studentMedical) {
        this.studentMedical = studentMedical;
        studentMedical.setStudent(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, firstname, lastname, studentPersonal, studentMedical);
    }
}
