package net.scholagest.app.rest;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.app.rest.ws.BranchPeriodsRest;
import net.scholagest.app.rest.ws.BranchesRest;
import net.scholagest.app.rest.ws.ClassesRest;
import net.scholagest.app.rest.ws.ExamsRest;
import net.scholagest.app.rest.ws.LoginRest;
import net.scholagest.app.rest.ws.MeansRest;
import net.scholagest.app.rest.ws.PeriodsRest;
import net.scholagest.app.rest.ws.ResultsRest;
import net.scholagest.app.rest.ws.StudentMedicalsRest;
import net.scholagest.app.rest.ws.StudentPersonalsRest;
import net.scholagest.app.rest.ws.StudentsRest;
import net.scholagest.app.rest.ws.TeacherDetailsRest;
import net.scholagest.app.rest.ws.TeachersRest;
import net.scholagest.app.rest.ws.UsersRest;
import net.scholagest.app.rest.ws.YearsRest;
import net.scholagest.app.rest.ws.authorization.AuthorizationVerifier;
import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.authorization.AuthorizationInterceptor;
import net.scholagest.authorization.RealmAuthenticationAndAuthorization;
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.SessionBusinessBean;
import net.scholagest.business.SessionBusinessLocal;
import net.scholagest.business.TeacherBusinessBean;
import net.scholagest.business.TeacherBusinessLocal;
import net.scholagest.business.UserBusinessBean;
import net.scholagest.business.UserBusinessLocal;
import net.scholagest.service.SessionServiceBean;
import net.scholagest.service.SessionServiceLocal;
import net.scholagest.service.TeacherServiceBean;
import net.scholagest.service.TeacherServiceLocal;
import net.scholagest.service.UserServiceBean;
import net.scholagest.service.UserServiceLocal;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.guice.ShiroModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceContext extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        final Injector injector = Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                final AuthorizationVerifier authorizationVerifier = new AuthorizationVerifier();
                requestInjection(authorizationVerifier);
                bindInterceptor(Matchers.any(), Matchers.annotatedWith(CheckAuthorization.class), authorizationVerifier);

                final AuthorizationInterceptor authorizationInterceptor = new AuthorizationInterceptor();
                requestInjection(authorizationInterceptor);
                bindInterceptor(Matchers.any(), Matchers.annotatedWith(RolesAndPermissions.class), authorizationInterceptor);

                bind(SessionBusinessLocal.class).to(SessionBusinessBean.class);
                bind(TeacherBusinessLocal.class).to(TeacherBusinessBean.class);
                bind(UserBusinessLocal.class).to(UserBusinessBean.class);

                bind(SessionServiceLocal.class).to(SessionServiceBean.class);
                // bind(StudentServiceLocal.class).to(StudentServiceBean.class);
                bind(TeacherServiceLocal.class).to(TeacherServiceBean.class);
                bind(UserServiceLocal.class).to(UserServiceBean.class);

                bind(LoginRest.class);
                bind(UsersRest.class);
                bind(YearsRest.class);
                bind(ClassesRest.class);
                bind(PeriodsRest.class);
                bind(BranchesRest.class);
                bind(BranchPeriodsRest.class);
                bind(ResultsRest.class);
                bind(MeansRest.class);
                bind(ExamsRest.class);
                bind(YearsRest.class);
                bind(StudentsRest.class);
                bind(StudentPersonalsRest.class);
                bind(StudentMedicalsRest.class);
                bind(TeachersRest.class);
                bind(TeacherDetailsRest.class);

                // Route all requests through GuiceContainer
                final Map<String, String> params = new HashMap<>();
                params.put("com.sun.jersey.config.property.packages", "net.scholagest.rest.ws");
                params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
                serve("/services/*").with(GuiceContainer.class, params);
            }
        }, new ShiroModule() {
            @Override
            protected void configureShiro() {
                bindRealm().to(RealmAuthenticationAndAuthorization.class);
            }
        });

        final org.apache.shiro.mgt.SecurityManager securityManager = injector.getInstance(org.apache.shiro.mgt.SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);

        return injector;
    }
}
