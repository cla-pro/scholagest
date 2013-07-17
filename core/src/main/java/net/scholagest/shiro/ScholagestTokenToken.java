package net.scholagest.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class ScholagestTokenToken implements AuthenticationToken {
    private static final long serialVersionUID = 8604723492049518832L;

    private String token;

    public ScholagestTokenToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s[token=%s]", getClass().getName(), token);
    }
}
