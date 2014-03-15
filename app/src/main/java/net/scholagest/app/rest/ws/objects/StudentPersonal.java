package net.scholagest.app.rest.ws.objects;

public class StudentPersonal extends BaseJson {
    private String street;
    private String city;
    private String postcode;
    private String religion;

    public StudentPersonal() {}

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
}
