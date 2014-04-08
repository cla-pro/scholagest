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
 * Entity that model a teacher detail in the DB
 * 
 * @author CLA
 * @since 0.15.0
 */
@Entity(name = "teacher_detail")
public class TeacherDetailEntity {
    private final static String TOSTRING_FORMAT = "TeacherDetail [id=%d, address=%s, email=%s, phone=%s]";

    @Id
    @SequenceGenerator(name = "seq_teacher_detail", sequenceName = "seq_teacher_detail_id")
    @GeneratedValue(generator = "seq_teacher_detail", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @JoinColumn(name = "teacher_id")
    @OneToOne
    private TeacherEntity teacher;

    public TeacherDetailEntity() {}

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public TeacherEntity getTeacher() {
        return teacher;
    }

    void setTeacher(final TeacherEntity teacher) {
        this.teacher = teacher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, address, email, phone);
    }
}
