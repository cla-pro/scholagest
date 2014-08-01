package net.scholagest.app.rest.ember;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String id;
    private String firstName;
    private String lastName;

    private Map<String, String> links;

    public Student() {}

    public Student(String id, String firstName, String lastName, String personal, String medical) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.links = new HashMap<>();
        links.put("personal", personal);
        links.put("medical", medical);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    // public String getPersonal() {
    // return personal;
    // }
    //
    // public String getMedical() {
    // return medical;
    // }
}
