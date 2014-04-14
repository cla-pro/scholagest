package net.scholagest.db.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a branch period in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "branch_period")
public class BranchPeriodEntity {
    private final static String TOSTRING_FORMAT = "BranchPeriod [id=%d]";

    @Id
    @SequenceGenerator(name = "seq_branch", sequenceName = "seq_branch_id")
    @GeneratedValue(generator = "seq_branch", strategy = GenerationType.SEQUENCE)
    private Long id;

    @JoinColumn(name = "branch_id")
    @ManyToOne
    private BranchEntity branch;

    @JoinColumn(name = "period_id")
    @ManyToOne
    private PeriodEntity period;

    @OneToMany(mappedBy = "branchPeriod")
    private List<ExamEntity> exams;

    @OneToMany(mappedBy = "branchPeriod")
    private List<StudentResultEntity> studentResult;

    public BranchPeriodEntity() {}

    public Long getId() {
        return id;
    }

    public BranchEntity getBranch() {
        return branch;
    }

    public void setBranch(final BranchEntity branch) {
        this.branch = branch;
    }

    public PeriodEntity getPeriod() {
        return period;
    }

    public void setPeriod(final PeriodEntity period) {
        this.period = period;
    }

    public List<ExamEntity> getExams() {
        return exams;
    }

    public void setExams(final List<ExamEntity> exams) {
        this.exams = exams;
    }

    public List<StudentResultEntity> getStudentResult() {
        return studentResult;
    }

    public void setStudentResult(final List<StudentResultEntity> studentResult) {
        this.studentResult = studentResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id);
    }
}
