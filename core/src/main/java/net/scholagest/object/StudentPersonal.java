package net.scholagest.object;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object associate to {@link Student} that contains the personal information (except the first and last name) of a student.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentPersonal extends Base {
    private String street;
    private String city;
    private String postcode;
    private String religion;

    public StudentPersonal() {}

    public StudentPersonal(final StudentPersonal toCopy) {
        super(toCopy.getId());
        this.street = toCopy.street;
        this.city = toCopy.city;
        this.postcode = toCopy.postcode;
        this.religion = toCopy.religion;
    }

    public StudentPersonal(final String id, final String street, final String city, final String postcode, final String religion) {
        super(id);
        this.street = street;
        this.city = city;
        this.postcode = postcode;
        this.religion = religion;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(final String postcode) {
        this.postcode = postcode;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(final String religion) {
        this.religion = religion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof StudentPersonal)) {
            return false;
        }

        final StudentPersonal other = (StudentPersonal) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(street, other.street).append(city, other.city)
                .append(postcode, other.postcode).append(religion, other.religion).isEquals();
    }
}
