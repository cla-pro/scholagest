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
 * Entity that model a mean in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "mean")
public class MeanEntity {
    private final static String TOSTRING_FORMAT = "Mean [id=%d, grade=%s]";

    @Id
    @SequenceGenerator(name = "seq_mean", sequenceName = "seq_mean_id")
    @GeneratedValue(generator = "seq_mean", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "active")
    private String grade;

    @JoinColumn(name = "student_mean_id")
    @OneToOne
    private StudentResultEntity studentResult;

    public MeanEntity() {}

    public Long getId() {
        return id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(final String grade) {
        this.grade = grade;
    }

    public StudentResultEntity getStudentResult() {
        return studentResult;
    }

    public void setStudentResult(final StudentResultEntity studentResult) {
        this.studentResult = studentResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, grade);
    }
}
