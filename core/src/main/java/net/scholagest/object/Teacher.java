package net.scholagest.object;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Transfer object that represent a teacher. The details are linked.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class Teacher extends Base {
    private String firstName;
    private String lastName;
    private final TeacherDetail detail;

    public Teacher() {
        this.detail = new TeacherDetail();
    }

    public Teacher(final Teacher copy) {
        this.firstName = copy.firstName;
        this.lastName = copy.lastName;
        this.detail = new TeacherDetail(copy.detail);
    }

    public Teacher(final String id, final String firstName, final String lastName, final TeacherDetail detail) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.detail = detail;
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

    public TeacherDetail getDetail() {
        return detail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof Teacher)) {
            return false;
        }

        final Teacher other = (Teacher) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(firstName, other.firstName).append(lastName, other.lastName)
                .append(detail, other.detail).isEquals();
    }
}
