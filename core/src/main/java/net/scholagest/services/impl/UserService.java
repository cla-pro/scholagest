package net.scholagest.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.business.IPageBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.objects.PageObject;
import net.scholagest.services.IUserService;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationService;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;

public class UserService implements IUserService {
    private IDatabase database;
    private IUserBusinessComponent userBusinessComponent;
    private IPageBusinessComponent pageBusinessComponent;
    private AuthorizationHelper authorizationHelper;

    @Inject
    public UserService(IDatabase database, IUserBusinessComponent userBusinessComponent, IPageBusinessComponent pageBusinessComponent,
            IOntologyBusinessComponent ontologyBusinessComponent) {
        this.database = database;
        this.userBusinessComponent = userBusinessComponent;
        this.pageBusinessComponent = pageBusinessComponent;
        this.authorizationHelper = new AuthorizationHelper(ontologyBusinessComponent);
    }

    @Override
    public List<String> getVisibleModules(String userKey) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);

        List<String> modules = Collections.emptyList();
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            Set<PageObject> pageObjects = pageBusinessComponent.getAllPages();
            Set<PageObject> filteredPageObjects = authorizationHelper.filterPages(pageObjects);

            modules = extractPath(filteredPageObjects);
            // TODO read in DB
            // return new String[] { "js/base.js", "js/base-html.js",
            // "modules/teacher.html", "js/teacher.js", "modules/student.html",
            // "js/student.js",
            // "js/exam.js", "js/branch.js", "js/period.js",
            // "modules/year.html", "js/year.js", "js/class.js",
            // "modules/grades.html" };

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return modules;
    }

    private List<String> extractPath(Set<PageObject> pageObjects) {
        List<String> paths = new ArrayList<>();

        for (PageObject pageObject : pageObjects) {
            paths.add(pageObject.getPath());
        }

        return paths;
    }

    @Override
    public Subject authenticateWithUsername(String username, String password) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);

        Subject subject = null;
        try {
            subject = userBusinessComponent.authenticateUser(username, password);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return subject;
    }

    @Override
    public Subject authenticateWithToken(String token) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);

        Subject subject = null;
        try {
            subject = userBusinessComponent.authenticateToken(token);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return subject;
    }

    @Override
    public String getTeacherKeyForToken(String token) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);

        String teacherKey = null;
        try {
            teacherKey = userBusinessComponent.getTeacherKeyForToken(token);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return teacherKey;
    }

    @Override
    public void setPassword(String teacherKey, String newPassword) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);

        try {
            authorizationHelper.checkAuthorization(Arrays.<String> asList(), Arrays.asList(teacherKey));

            userBusinessComponent.setPassword(teacherKey, newPassword);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void resetPassword(String teacherKey) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);

        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAdminRole());

            userBusinessComponent.resetPassword(teacherKey);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void logout(String token) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);

        try {
            userBusinessComponent.logout(token);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
