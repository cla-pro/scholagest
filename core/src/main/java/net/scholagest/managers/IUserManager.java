package net.scholagest.managers;

import net.scholagest.objects.TokenObject;
import net.scholagest.objects.UserObject;

public interface IUserManager {
    public UserObject createUser(String username, String password) throws Exception;

    public UserObject getUser(String userKey) throws Exception;

    public TokenObject createToken(String userKey, String token) throws Exception;

    public TokenObject getToken(String tokenId) throws Exception;

    public void deleteToken(String tokenId) throws Exception;

    public UserObject getUserWithUsername(String username) throws Exception;
}
