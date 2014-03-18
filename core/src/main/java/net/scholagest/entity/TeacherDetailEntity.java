package net.scholagest.entity;

public class TeacherDetailEntity extends BaseEntity {
    private String address;
    private String email;
    private String phone;

    public TeacherDetailEntity() {}

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
}
