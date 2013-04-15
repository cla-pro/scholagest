package net.scholagest.shiro;

import net.scholagest.database.ITransaction;

import org.apache.shiro.authc.AuthenticationToken;

public class ScholagestTokenToken implements AuthenticationToken {
    private static final long serialVersionUID = 8604723492049518832L;

    private String requestId;
    private ITransaction transaction;
    private String token;

    public ScholagestTokenToken(String requestId, ITransaction transaction, String token) {
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

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isRememberMe() {
        return false;
    }
}
