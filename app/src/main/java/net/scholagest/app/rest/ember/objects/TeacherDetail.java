package net.scholagest.app.rest.ember.objects;

public class TeacherDetail {
    private String id;
    private String address;
    private String email;
    private String phone;

    public TeacherDetail() {}

    public TeacherDetail(final String id, final String address, final String email, final String phone) {
        this.id = id;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
