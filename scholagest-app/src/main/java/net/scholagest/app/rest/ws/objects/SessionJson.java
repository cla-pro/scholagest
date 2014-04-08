package net.scholagest.app.rest.ws.objects;

/**
 * Json object representing a session
 * 
 * @author CLA
 * @since 0.14.0
 */
public class SessionJson {
    private String token;
    private String user;

    public SessionJson(final String token, final String user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }
}
