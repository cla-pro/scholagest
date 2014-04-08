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
    private StudentClass studentClasses;

    public Student() {
        this.studentPersonal = new StudentPersonal();
        this.studentMedical = new StudentMedical();
        this.studentClasses = new StudentClass();
    }

    public Student(final Student toCopy) {
        this(toCopy.getId(), toCopy.firstName, toCopy.lastName, new StudentPersonal(toCopy.studentPersonal),
                new StudentMedical(toCopy.studentMedical), new StudentClass(toCopy.studentClasses));
    }

    public Student(final String id, final String firstName, final String lastName, final StudentPersonal studentPersonal,
            final StudentMedical studentMedical, final StudentClass studentClasses) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentPersonal = studentPersonal;
        this.studentMedical = studentMedical;
        this.studentClasses = studentClasses;
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

    public StudentClass getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(final StudentClass studentClasses) {
        this.studentClasses = studentClasses;
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
