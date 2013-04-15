package net.scholagest.business.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.managers.IUserManager;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.UserObject;
import net.scholagest.shiro.ScholagestTokenToken;
import net.scholagest.shiro.ScholagestUsernameToken;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;

public class UserBusinessComponent implements IUserBusinessComponent {
    private IUserManager userManager;
    private final ITeacherManager teacherManager;

    @Inject
    public UserBusinessComponent(IUserManager userManager, ITeacherManager teacherManager) {
        this.userManager = userManager;
        this.teacherManager = teacherManager;
    }

    @Override
    public Subject authenticateUser(String requestId, ITransaction transaction, String username, String password) throws Exception {
        String sessionToken = UUID.randomUUID().toString();

        ScholagestUsernameToken token = new ScholagestUsernameToken(requestId, transaction, sessionToken, username, password);
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return null;
        }

        return subject;
    }

    @Override
    public Subject authenticateToken(String requestId, ITransaction transaction, String tokenId) throws Exception {
        ScholagestTokenToken token = new ScholagestTokenToken(requestId, transaction, tokenId);
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return null;
        }

        return subject;
    }

    @Override
    public UserObject createUser(String requestId, ITransaction transaction, String teacherKey) throws Exception {
        BaseObject teacherProperties = teacherManager.getTeacherProperties(requestId, transaction, teacherKey,
                new HashSet<>(Arrays.asList("pTeacherFirstName", "pTeacherLastName")));
        String username = generateUsername(teacherProperties);
        return userManager.createUser(requestId, transaction, username);
    }

    private String generateUsername(BaseObject teacherProperties) {
        String firstName = (String) teacherProperties.getProperty("pTeacherFirstName");
        String lastName = (String) teacherProperties.getProperty("pTeacherLastName");
        return firstName.substring(0, 1) + lastName;
    }
}
