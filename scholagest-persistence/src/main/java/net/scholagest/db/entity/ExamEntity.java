package net.scholagest.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a exam in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "exam")
public class ExamEntity {
    private final static String TOSTRING_FORMAT = "Exam [id=%d, name=%s, coeff=%d]";

    @Id
    @SequenceGenerator(name = "seq_exam", sequenceName = "seq_exam_id")
    @GeneratedValue(generator = "seq_exam", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "coeff")
    private int coeff;

    @JoinColumn(name = "branch_period_id")
    @ManyToOne
    private BranchPeriodEntity branchPeriod;

    public ExamEntity() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getCoeff() {
        return coeff;
    }

    public void setCoeff(final int coeff) {
        this.coeff = coeff;
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
        return String.format(TOSTRING_FORMAT, id, name, coeff);
    }
}
