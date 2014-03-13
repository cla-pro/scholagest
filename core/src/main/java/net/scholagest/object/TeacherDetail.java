package net.scholagest.object;

import org.apache.commons.lang.builder.EqualsBuilder;

public class TeacherDetail extends Base {
    private String address;
    private String email;
    private String phone;

    public TeacherDetail() {}

    public TeacherDetail(final TeacherDetail copy) {
        this.address = copy.address;
        this.email = copy.email;
        this.phone = copy.phone;
    }

    public TeacherDetail(final String id, final String address, final String email, final String phone) {
        super(id);
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (!(that instanceof TeacherDetail)) {
            return false;
        }

        final TeacherDetail other = (TeacherDetail) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(address, other.address).append(email, other.email)
                .append(phone, other.phone).isEquals();
    }
}
