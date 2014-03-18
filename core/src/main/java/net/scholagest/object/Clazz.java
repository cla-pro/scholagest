package net.scholagest.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent a clazz.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class Clazz extends Base {
    private String name;
    private String year;
    private List<String> periods;
    private List<String> teachers;
    private List<String> students;
    private List<String> branches;

    public Clazz() {
        this.teachers = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public Clazz(final Clazz toCopy) {
        super(toCopy.getId());
        this.name = toCopy.name;
        this.year = toCopy.year;
        this.periods = new ArrayList<String>(toCopy.periods);
        this.teachers = new ArrayList<String>(toCopy.teachers);
        this.students = new ArrayList<String>(toCopy.students);
        this.branches = new ArrayList<String>(toCopy.branches);
    }

    public Clazz(final String id, final String name, final String year, final List<String> periods, final List<String> teachers,
            final List<String> students, final List<String> branches) {
        super(id);
        this.name = name;
        this.year = year;
        this.periods = new ArrayList<String>(periods);
        this.teachers = new ArrayList<String>(teachers);
        this.students = new ArrayList<String>(students);
        this.branches = new ArrayList<String>(branches);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<String> getPeriods() {
        return periods;
    }

    public void setPeriods(final List<String> periods) {
        this.periods = periods;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(final List<String> teachers) {
        this.teachers = teachers;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(final List<String> students) {
        this.students = students;
    }

    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(final List<String> branches) {
        this.branches = branches;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof Clazz)) {
            return false;
        }

        final Clazz other = (Clazz) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(name, other.name).append(year, other.year).isEquals();
    }
}
