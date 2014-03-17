package net.scholagest.object;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent a student. The medical and personal information are linked.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class Student extends Base {
    private String firstName;
    private String lastName;
    private StudentPersonal studentPersonal;
    private StudentMedical studentMedical;

    public Student() {
        this.studentPersonal = new StudentPersonal();
        this.studentMedical = new StudentMedical();
    }

    public Student(final Student toCopy) {
        super(toCopy.getId());
        this.firstName = toCopy.firstName;
        this.lastName = toCopy.lastName;
        this.studentPersonal = new StudentPersonal(toCopy.studentPersonal);
        this.studentMedical = new StudentMedical(toCopy.studentMedical);
    }

    public Student(final String id, final String firstName, final String lastName, final StudentPersonal studentPersonal,
            final StudentMedical studentMedical) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentPersonal = studentPersonal;
        this.studentMedical = studentMedical;
    }

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

    public StudentPersonal getStudentPersonal() {
        return studentPersonal;
    }

    public void setStudentPersonal(final StudentPersonal studentPersonal) {
        this.studentPersonal = studentPersonal;
    }

    public StudentMedical getStudentMedical() {
        return studentMedical;
    }

    public void setStudentMedical(final StudentMedical studentMedical) {
        this.studentMedical = studentMedical;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof Student)) {
            return false;
        }

        final Student other = (Student) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(firstName, other.firstName).append(lastName, other.lastName).isEquals();
    }
}
