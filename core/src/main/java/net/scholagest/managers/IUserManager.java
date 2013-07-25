package net.scholagest.managers;

import net.scholagest.objects.TokenObject;
import net.scholagest.objects.UserObject;

public interface IUserManager {
    public UserObject createUser(String username, String password, String teacherKey);

    public UserObject getUser(String userKey);

    public TokenObject createToken(String userKey, String token);

    public TokenObject getToken(String tokenId);

    public void deleteToken(String tokenId);

    public UserObject getUserWithUsername(String username);
}
