package net.scholagest.authorization;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernameToken extends UsernamePasswordToken {
    private static final long serialVersionUID = -1508220405157339067L;

    public UsernameToken(final String username, final String password) {
        super(username, password);
    }
}
