package net.scholagest.old.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ScholagestUsernameToken extends UsernamePasswordToken {
    private static final long serialVersionUID = -1508220405157339067L;

    private String token;

    public ScholagestUsernameToken(String token, String username, String password) {
        super(username, password);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
