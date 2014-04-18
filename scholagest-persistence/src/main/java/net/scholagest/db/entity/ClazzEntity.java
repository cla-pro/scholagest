package net.scholagest.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a class in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "class")
public class ClazzEntity {
    private final static String TOSTRING_FORMAT = "Class [id=%d, name=%s]";

    @Id
    @SequenceGenerator(name = "seq_class", sequenceName = "seq_class_id")
    @GeneratedValue(generator = "seq_class", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "year_id")
    @ManyToOne
    private YearEntity year;

    @OneToMany(mappedBy = "clazz")
    private List<BranchEntity> branches;

    @OneToMany(mappedBy = "clazz")
    private List<PeriodEntity> periods;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "class_teacher", joinColumns = { @JoinColumn(name = "class_id") }, inverseJoinColumns = { @JoinColumn(name = "teacher_id") })
    private List<TeacherEntity> teachers;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "class_student", joinColumns = { @JoinColumn(name = "class_id") }, inverseJoinColumns = { @JoinColumn(name = "student_id") })
    private List<StudentEntity> students;

    public ClazzEntity() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public YearEntity getYear() {
        return year;
    }

    public void setYear(final YearEntity year) {
        this.year = year;
    }

    public List<BranchEntity> getBranches() {
        if (branches == null) {
            return new ArrayList<>();
        } else {
            return branches;
        }
    }

    public void setBranches(final List<BranchEntity> branches) {
        this.branches = branches;
    }

    public List<PeriodEntity> getPeriods() {
        if (periods == null) {
            return new ArrayList<>();
        } else {
            return periods;
        }
    }

    public void setPeriods(final List<PeriodEntity> periods) {
        this.periods = periods;
    }

    public List<TeacherEntity> getTeachers() {
        if (teachers == null) {
            return new ArrayList<>();
        } else {
            return teachers;
        }
    }

    public void setTeachers(final List<TeacherEntity> teachers) {
        this.teachers = teachers;
    }

    public List<StudentEntity> getStudents() {
        if (students == null) {
            return new ArrayList<>();
        } else {
            return students;
        }
    }

    public void setStudents(final List<StudentEntity> students) {
        this.students = students;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, name);
    }
}
