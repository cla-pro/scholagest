package net.scholagest.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a teacher in the DB
 * 
 * @author CLA
 * @since 0.15.0
 */
@Entity(name = "teacher")
public class TeacherEntity {
    private final static String TOSTRING_FORMAT = "Teacher [id=%d, firstname=%s, lastname=%s, detail=%s]";

    @Id
    @SequenceGenerator(name = "seq_teacher", sequenceName = "seq_teacher_id")
    @GeneratedValue(generator = "seq_teacher", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @OneToOne(mappedBy = "teacher", fetch = FetchType.EAGER)
    private TeacherDetailEntity teacherDetail;

    @ManyToMany(mappedBy = "teachers", fetch = FetchType.LAZY)
    private List<ClazzEntity> clazzes;

    public TeacherEntity() {}

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

    public TeacherDetailEntity getTeacherDetail() {
        return teacherDetail;
    }

    public void setTeacherDetail(final TeacherDetailEntity teacherDetail) {
        this.teacherDetail = teacherDetail;
        teacherDetail.setTeacher(this);
    }

    public List<ClazzEntity> getClazzes() {
        if (clazzes == null) {
            return new ArrayList<>();
        } else {
            return clazzes;
        }
    }

    public void setClazzes(final List<ClazzEntity> clazzes) {
        this.clazzes = clazzes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, firstname, lastname, teacherDetail);
    }
}
