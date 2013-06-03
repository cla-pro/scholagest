package net.scholagest.business.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.managers.IUserManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.UserObject;
import net.scholagest.services.kdom.KSet;
import net.scholagest.shiro.ScholagestTokenToken;
import net.scholagest.shiro.ScholagestUsernameToken;

import org.apache.shiro.SecurityUtils;
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
        BaseObject teacherProperties = teacherManager.getTeacherProperties(teacherKey,
                new HashSet<>(Arrays.asList("pTeacherFirstName", "pTeacherLastName")));
        String username = generateUsername(teacherProperties);
        return userManager.createUser(username, "");
    }

    private String generateUsername(BaseObject teacherProperties) {
        String firstName = (String) teacherProperties.getProperty("pTeacherFirstName");
        String lastName = (String) teacherProperties.getProperty("pTeacherLastName");
        return firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
    }

    @Override
    public void removeUsersPermissions(String teacherKey, KSet students) throws Exception {
        BaseObject teacher = teacherManager.getTeacherProperties(teacherKey, new HashSet<>(Arrays.asList(CoreNamespace.pTeacherUser)));
        UserObject userObject = userManager.getUser((String) teacher.getProperties().get(CoreNamespace.pTeacherUser));

        for (Object studentKey : students.getValues()) {
            userObject.getPermissions().remove((String) studentKey);
        }
    }

    @Override
    public void addUsersPermissions(String teacherKey, KSet students) throws Exception {
        BaseObject teacher = teacherManager.getTeacherProperties(teacherKey, new HashSet<>(Arrays.asList(CoreNamespace.pTeacherUser)));
        UserObject userObject = userManager.getUser((String) teacher.getProperties().get(CoreNamespace.pTeacherUser));

        for (Object studentKey : students.getValues()) {
            userObject.getPermissions().add((String) studentKey);
        }
    }
}
