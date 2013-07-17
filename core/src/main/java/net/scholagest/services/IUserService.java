package net.scholagest.services;

import java.util.List;

import org.apache.shiro.subject.Subject;

public interface IUserService {
    public Subject authenticateWithUsername(String username, String password) throws Exception;

    public List<String> getVisibleModules(String userKey) throws Exception;

    public Subject authenticateWithToken(String token) throws Exception;
}
