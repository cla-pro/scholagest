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
@Entity(name = "student_personal")
public class StudentPersonalEntity {
    private final static String TOSTRING_FORMAT = "StudentPersonal [id=%d, street=%s, postcode=%s, city=%s, religion=%s]";

    @Id
    @SequenceGenerator(name = "seq_student_personal", sequenceName = "seq_student_personal_id")
    @GeneratedValue(generator = "seq_student_personal", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "city")
    private String city;

    @Column(name = "religion")
    private String religion;

    @JoinColumn(name = "student_id")
    @OneToOne
    private StudentEntity student;

    public StudentPersonalEntity() {}

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(final String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(final String religion) {
        this.religion = religion;
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
        return String.format(TOSTRING_FORMAT, id, street, postcode, city, religion);
    }
}
