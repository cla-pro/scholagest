package net.scholagest.app.rest.object;

public class RestRequest {
    private String token;
    private RestObject object;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RestObject getObject() {
        return object;
    }

    public void setObject(RestObject object) {
        this.object = object;
    }
}
