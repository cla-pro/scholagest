package net.scholagest.app.rest.ember.objects;

public class StudentPersonal {
    private String id;
    private String street;
    private String city;
    private String postcode;
    private String religion;

    public StudentPersonal() {}

    public StudentPersonal(String id, String street, String city, String postcode, String religion) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
        this.religion = religion;
    }

    public String getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getReligion() {
        return religion;
    }
}
