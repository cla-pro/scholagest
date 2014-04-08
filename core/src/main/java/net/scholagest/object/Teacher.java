package net.scholagest.object;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Transfer object that represent a teacher. The details are linked.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class Teacher extends Base {
    private String firstname;
    private String lastname;
    private final TeacherDetail detail;

    public Teacher() {
        this.detail = new TeacherDetail();
    }

    public Teacher(final Teacher copy) {
        this(copy.getId(), copy.firstname, copy.lastname, new TeacherDetail(copy.detail));
    }

    public Teacher(final String id, final String firstname, final String lastname, final TeacherDetail detail) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.detail = detail;
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

    public void setLastname(final String lastname) {
        this.lastname = lastname;
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
        return new EqualsBuilder().append(getId(), other.getId()).append(firstname, other.firstname).append(lastname, other.lastname)
                .append(detail, other.detail).isEquals();
    }
}
