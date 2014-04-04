package net.scholagest.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Entity that model a teacher in the DB
 * 
 * @author CLA
 * @since 0.15.0
 */
@Entity(name = "teacher")
public class TeacherEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @OneToOne(mappedBy = "teacher", fetch = FetchType.EAGER)
    private TeacherDetailEntity teacherDetail;

    public TeacherEntity() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public TeacherDetailEntity getTeacherDetail() {
        return teacherDetail;
    }

    public void setTeacherDetail(final TeacherDetailEntity teacherDetail) {
        this.teacherDetail = teacherDetail;
    }
}
