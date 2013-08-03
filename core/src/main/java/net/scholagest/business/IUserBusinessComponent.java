package net.scholagest.business;

import java.util.Set;

import net.scholagest.objects.UserObject;

import org.apache.shiro.subject.Subject;

public interface IUserBusinessComponent {
    public Subject authenticateUser(String username, String password) throws Exception;

    public Subject authenticateToken(String tokenId) throws Exception;

    public UserObject createUser(String teacherKey) throws Exception;

    public void removeUsersPermissions(String teacherKey, Set<String> rights) throws Exception;

    public void addUsersPermissions(String teacherKey, Set<String> rights) throws Exception;

    public String getTeacherKeyForToken(String token);

    public void setPassword(String teacherKey, String newPassword);
}
