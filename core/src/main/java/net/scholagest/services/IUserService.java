package net.scholagest.services;

import java.util.List;

import org.apache.shiro.subject.Subject;

public interface IUserService {
    public Subject authenticateWithUsername(String requestId, String username, String password) throws Exception;

    public List<String> getVisibleModules(String requestId, String userKey) throws Exception;

    public Subject authenticateWithToken(String requestId, String token) throws Exception;
}
