package net.scholagest.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a branch in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "branch")
public class BranchEntity {
    private final static String TOSTRING_FORMAT = "Branch [id=%d, name=%s]";

    @Id
    @SequenceGenerator(name = "seq_branch", sequenceName = "seq_branch_id")
    @GeneratedValue(generator = "seq_branch", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "class_id")
    @ManyToOne
    private ClazzEntity clazz;

    @OneToMany(mappedBy = "branch")
    private List<BranchPeriodEntity> branchPeriods;

    public BranchEntity() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ClazzEntity getClazz() {
        return clazz;
    }

    public void setClazz(final ClazzEntity clazz) {
        this.clazz = clazz;
    }

    public List<BranchPeriodEntity> getBranchPeriods() {
        if (branchPeriods == null) {
            return new ArrayList<>();
        } else {
            return branchPeriods;
        }
    }

    public void setBranchPeriods(final List<BranchPeriodEntity> branchPeriods) {
        this.branchPeriods = branchPeriods;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, name);
    }
}
