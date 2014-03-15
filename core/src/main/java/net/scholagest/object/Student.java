package net.scholagest.object;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent a student. The medical and personal information are linked.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class Student extends Base {
    private String id;
    private String firstName;
    private String lastName;

    public Student() {}

    public Student(final Student toCopy) {
        this.id = toCopy.id;
        this.firstName = toCopy.firstName;
        this.lastName = toCopy.lastName;
    }

    public Student(final String id, final String firstName, final String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
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
        return new EqualsBuilder().append(id, other.id).append(firstName, other.firstName).append(lastName, other.lastName).isEquals();
    }
}
