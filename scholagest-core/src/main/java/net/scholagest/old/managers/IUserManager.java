package net.scholagest.old.managers;

import net.scholagest.old.objects.TokenObject;
import net.scholagest.old.objects.UserObject;

public interface IUserManager {
    public UserObject createUser(String username, String teacherKey);

    public UserObject getUser(String userKey);

    public TokenObject createToken(String userKey, String token);

    public TokenObject getToken(String tokenId);

    public void deleteToken(String tokenId);

    public UserObject getUserWithUsername(String username);
}
