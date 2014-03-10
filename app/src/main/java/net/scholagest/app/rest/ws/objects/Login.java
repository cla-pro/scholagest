package net.scholagest.app.rest.ws.objects;

public class Login {
    private String username;
    private String password;
    private String token;

    public Login() {}

    public Login(final String username, final String password, final String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public boolean hasToken() {
        return token != null && !token.isEmpty();
    }

    public boolean hasUsername() {
        return username != null && !username.isEmpty();
    }
}
