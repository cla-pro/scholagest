package net.scholagest.authorization;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernameToken extends UsernamePasswordToken {
    private static final long serialVersionUID = -1508220405157339067L;

    private String token;

    public UsernameToken(String token, String username, String password) {
        super(username, password);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
