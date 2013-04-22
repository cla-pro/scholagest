package net.scholagest.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.scholagest.business.IPageBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.objects.PageObject;
import net.scholagest.services.IUserService;

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
    public List<String> getVisibleModules(String requestId, String userKey) throws Exception {
        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            Set<PageObject> pageObjects = pageBusinessComponent.getAllPages(requestId, transaction);
            return extractPath(pageObjects);
            // TODO read in DB
            // return new String[] { "js/base.js", "js/base-html.js",
            // "modules/teacher.html", "js/teacher.js", "modules/student.html",
            // "js/student.js",
            // "js/exam.js", "js/branch.js", "js/period.js",
            // "modules/year.html", "js/year.js", "js/class.js",
            // "modules/grades.html" };
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private List<String> extractPath(Set<PageObject> pageObjects) {
        List<String> paths = new ArrayList<>();

        for (PageObject pageObject : pageObjects) {
            paths.add(pageObject.getPath());
        }

        return paths;
    }

    @Override
    public Subject authenticateWithUsername(String requestId, String username, String password) throws Exception {
        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);

        Subject subject = null;
        try {
            subject = userBusinessComponent.authenticateUser(requestId, transaction, username, password);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return subject;
    }

    @Override
    public Subject authenticateWithToken(String requestId, String token) throws Exception {
        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);

        Subject subject = null;
        try {
            subject = userBusinessComponent.authenticateToken(requestId, transaction, token);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return subject;
    }
}
