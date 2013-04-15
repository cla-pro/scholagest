package net.scholagest.managers;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.TokenObject;
import net.scholagest.objects.UserObject;

public interface IUserManager {
    public UserObject createUser(String requestId, ITransaction transaction, String username) throws Exception;

    public UserObject getUser(String requestId, ITransaction transaction, String userKey) throws Exception;

    public TokenObject createToken(String requestId, ITransaction transaction, String userKey, String token) throws Exception;

    public TokenObject getToken(String requestId, ITransaction transaction, String tokenId) throws Exception;

    void deleteToken(String requestId, ITransaction transaction, String tokenId) throws Exception;
}
