package net.scholagest.app.rest.object;

public class RestAuthenticationRequest extends RestBaseRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
