package net.scholagest.business;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.UserObject;

import org.apache.shiro.subject.Subject;

public interface IUserBusinessComponent {
    public Subject authenticateUser(String requestId, ITransaction transaction, String username, String password) throws Exception;

    public Subject authenticateToken(String requestId, ITransaction transaction, String tokenId) throws Exception;

    public UserObject createUser(String requestId, ITransaction transaction, String teacherKey) throws Exception;
}
