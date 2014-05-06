package net.scholagest.old.business.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.scholagest.old.business.IUserBusinessComponent;
import net.scholagest.old.managers.ITeacherManager;
import net.scholagest.old.managers.IUserManager;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.TeacherObject;
import net.scholagest.old.objects.TokenObject;
import net.scholagest.old.objects.UserObject;
import net.scholagest.old.shiro.RealmAuthenticationAndAuthorization;
import net.scholagest.old.shiro.ScholagestTokenToken;
import net.scholagest.old.shiro.ScholagestUsernameToken;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import com.google.inject.Inject;

public class UserBusinessComponent implements IUserBusinessComponent {
    private final IUserManager userManager;
    private final ITeacherManager teacherManager;

    @Inject
    public UserBusinessComponent(IUserManager userManager, ITeacherManager teacherManager) {
        this.userManager = userManager;
        this.teacherManager = teacherManager;
    }

    @Override
    public Subject authenticateUser(String username, String password) throws Exception {
        String sessionToken = UUID.randomUUID().toString();

        ScholagestUsernameToken token = new ScholagestUsernameToken(sessionToken, username, password);
        Subject subject = SecurityUtils.getSubject();

        subject.login(token);

        return subject;
    }

    @Override
    public Subject authenticateToken(String tokenId) throws Exception {
        ScholagestTokenToken token = new ScholagestTokenToken(tokenId);
        Subject subject = SecurityUtils.getSubject();

        subject.login(token);

        return subject;
    }

    @Override
    public UserObject createUser(String teacherKey) throws Exception {
        TeacherObject teacherObject = teacherManager.getTeacherProperties(teacherKey,
                new HashSet<>(Arrays.asList("pTeacherFirstName", "pTeacherLastName")));

        String username = generateUsername(teacherObject);
        UserObject userObject = userManager.createUser(username, teacherKey);
        teacherObject.setUserKey(userObject.getKey());

        setPassword(teacherKey, generateNewPassword());

        return userObject;
    }

    private String generateNewPassword() {
        return "";
    }

    private String generateUsername(BaseObject teacherProperties) {
        String firstName = (String) teacherProperties.getProperty("pTeacherFirstName");
        String lastName = (String) teacherProperties.getProperty("pTeacherLastName");

        return firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
    }

    @Override
    public void removeUsersPermissions(String teacherKey, Set<String> rights) throws Exception {
        BaseObject teacher = teacherManager.getTeacherProperties(teacherKey, new HashSet<>(Arrays.asList(CoreNamespace.pTeacherUser)));
        UserObject userObject = userManager.getUser((String) teacher.getProperties().get(CoreNamespace.pTeacherUser));

        for (String singleRight : rights) {
            userObject.getPermissions().remove(singleRight);
        }
    }

    @Override
    public void addUsersPermissions(String teacherKey, Set<String> rights) throws Exception {
        BaseObject teacher = teacherManager.getTeacherProperties(teacherKey, new HashSet<>(Arrays.asList(CoreNamespace.pTeacherUser)));
        UserObject userObject = userManager.getUser((String) teacher.getProperties().get(CoreNamespace.pTeacherUser));

        for (String singleRight : rights) {
            userObject.getPermissions().add(singleRight);
        }
    }

    @Override
    public String getTeacherKeyForToken(String token) {
        TokenObject tokenObject = userManager.getToken(token);
        UserObject userObject = userManager.getUser(tokenObject.getUserObjectKey());
        return userObject.getTeacherKey();
    }

    @Override
    public void setPassword(String teacherKey, String newPassword) {
        TeacherObject teacherObject = teacherManager.getTeacherProperties(teacherKey, new HashSet<String>());
        UserObject userObject = userManager.getUser(teacherObject.getUserKey());

        String encryptedPassword = encryptPassword(userObject.getKey(), newPassword);

        userObject.setPassword(encryptedPassword);
    }

    @Override
    public void resetPassword(String teacherKey) {
        setPassword(teacherKey, generateNewPassword());
    }

    private String encryptPassword(String userKey, String newPassword) {
        Sha1Hash hash = new Sha1Hash(newPassword, ByteSource.Util.bytes(userKey), RealmAuthenticationAndAuthorization.HASH_ITERATIONS);

        return hash.toHex();
    }

    @Override
    public void logout(String token) {
        userManager.deleteToken(token);
    }

    @Override
    public UserObject getUser(String userKey) throws Exception {
        return userManager.getUser(userKey);
    }
}
