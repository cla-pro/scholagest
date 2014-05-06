package net.scholagest.object;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent a student. The medical and personal information are linked.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class Student extends Base {
    private String firstname;
    private String lastname;
    private StudentPersonal studentPersonal;
    private StudentMedical studentMedical;
    private StudentClass studentClasses;

    public Student() {
        this.studentPersonal = new StudentPersonal();
        this.studentMedical = new StudentMedical();
        this.studentClasses = new StudentClass();
    }

    public Student(final Student toCopy) {
        this(toCopy.getId(), toCopy.firstname, toCopy.lastname, new StudentPersonal(toCopy.studentPersonal),
                new StudentMedical(toCopy.studentMedical), new StudentClass(toCopy.studentClasses));
    }

    public Student(final String id, final String firstname, final String lastname, final StudentPersonal studentPersonal,
            final StudentMedical studentMedical, final StudentClass studentClasses) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.studentPersonal = studentPersonal;
        this.studentMedical = studentMedical;
        this.studentClasses = studentClasses;
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

    public void setLastname(final String lastName) {
        this.lastname = lastName;
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
        return new EqualsBuilder().append(getId(), other.getId()).append(firstname, other.firstname).append(lastname, other.lastname).isEquals();
    }
}
