package net.scholagest.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.scholagest.business.IPageBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationNamespace;
import net.scholagest.objects.PageObject;
import net.scholagest.services.IUserService;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;

public class UserService implements IUserService {
    private IDatabase database;
    private IUserBusinessComponent userBusinessComponent;
    private IPageBusinessComponent pageBusinessComponent;

    @Inject
    public UserService(IDatabase database, IUserBusinessComponent userBusinessComponent, IPageBusinessComponent pageBusinessComponent) {
        this.database = database;
        this.userBusinessComponent = userBusinessComponent;
        this.pageBusinessComponent = pageBusinessComponent;
    }

    @Override
    public List<String> getVisibleModules(String userKey) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);

        List<String> modules = Collections.emptyList();
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());

            Set<PageObject> pageObjects = pageBusinessComponent.getAllPages();
            modules = extractPath(pageObjects);
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
        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
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
        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
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
}
