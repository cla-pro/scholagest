package net.scholagest.db.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a student result in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "student_result")
public class StudentResultEntity {
    private final static String TOSTRING_FORMAT = "StudentResult [id=%d, active=%s]";

    @Id
    @SequenceGenerator(name = "seq_student_result", sequenceName = "seq_student_result_id")
    @GeneratedValue(generator = "seq_student_result", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "active")
    private boolean active;

    @JoinColumn(name = "student_id")
    @ManyToOne
    private StudentEntity student;

    @JoinColumn(name = "branch_period_id")
    @ManyToOne
    private BranchPeriodEntity branchPeriod;

    @OneToMany(mappedBy = "studentResult")
    private List<ResultEntity> results;

    @OneToOne(mappedBy = "studentResult")
    private MeanEntity mean;

    public StudentResultEntity() {}

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(final StudentEntity student) {
        this.student = student;
    }

    public BranchPeriodEntity getBranchPeriod() {
        return branchPeriod;
    }

    public void setBranchPeriod(final BranchPeriodEntity branchPeriod) {
        this.branchPeriod = branchPeriod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, active);
    }
}
