package net.scholagest.app.rest.ws.objects;

public class TeacherDetailJson extends BaseJson {
    private String address;
    private String email;
    private String phone;

    public TeacherDetailJson() {}

    public TeacherDetailJson(final String id, final String address, final String email, final String phone) {
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
}
