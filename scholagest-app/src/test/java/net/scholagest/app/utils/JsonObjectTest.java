package net.scholagest.app.utils;

public class JsonObjectTest {
    public static void main(String[] args) {
        System.out.println(new JsonObject("token", "abcdef", "nextpage", "http://localhost:8080/scholagest-app/services/user/getPage?token=")
                .toString());
    }
}
