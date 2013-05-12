package net.scholagest.shiro;

import net.scholagest.database.ITransaction;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ScholagestUsernameToken extends UsernamePasswordToken {
    private static final long serialVersionUID = -1508220405157339067L;

    private String requestId;
    private ITransaction transaction;
    private String token;

    public ScholagestUsernameToken(String requestId, ITransaction transaction, String token, String username, String password) {
        super(username, password);
        this.requestId = requestId;
        this.token = token;
        this.transaction = transaction;
    }

    public String getToken() {
        return token;
    }

    public ITransaction getTransaction() {
        return transaction;
    }

    public String getRequestId() {
        return requestId;
    }
}
