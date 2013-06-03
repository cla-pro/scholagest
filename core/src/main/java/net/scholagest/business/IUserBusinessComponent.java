package net.scholagest.business;

import net.scholagest.objects.UserObject;
import net.scholagest.services.kdom.KSet;

import org.apache.shiro.subject.Subject;

public interface IUserBusinessComponent {
    public Subject authenticateUser(String username, String password) throws Exception;

    public Subject authenticateToken(String tokenId) throws Exception;

    public UserObject createUser(String teacherKey) throws Exception;

    public void removeUsersPermissions(String teacherKey, KSet students) throws Exception;

    public void addUsersPermissions(String teacherKey, KSet students) throws Exception;
}
