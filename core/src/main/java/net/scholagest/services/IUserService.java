package net.scholagest.services;

import org.apache.shiro.subject.Subject;

public interface IUserService {
    public Subject authenticateWithUsername(String requestId, String username, String password) throws Exception;

    public String[] getVisibleModules(String requestId, String userKey) throws Exception;

    public Subject authenticateWithToken(String requestId, String token) throws Exception;
}
