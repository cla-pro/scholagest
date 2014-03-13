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
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.TeacherBusinessBean;
import net.scholagest.business.TeacherBusinessLocal;
import net.scholagest.service.TeacherServiceBean;
import net.scholagest.service.TeacherServiceLocal;

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

                // bind(IDatabaseConfiguration.class).to(DefaultDatabaseConfiguration.class);
                // bind(IDatabase.class).to(Database.class);

                // bind(ITeacherManager.class).to(TeacherManager.class);
                // bind(ITeacherBusinessComponent.class).to(TeacherBusinessComponent.class);
                // bind(ITeacherService.class).to(TeacherService.class);
                //
                // bind(IOntologyManager.class).to(OntologyManager.class);
                // bind(IOntologyBusinessComponent.class).to(OntologyBusinessComponent.class);
                // bind(IOntologyService.class).to(OntologyService.class);
                //
                // bind(IUserManager.class).to(UserManager.class);
                // bind(IUserBusinessComponent.class).to(UserBusinessComponent.class);
                // bind(IUserService.class).to(UserService.class);
                //
                // bind(IStudentManager.class).to(StudentManager.class);
                // bind(IStudentBusinessComponent.class).to(StudentBusinessComponent.class);
                // bind(IStudentService.class).to(StudentService.class);
                //
                // bind(IYearManager.class).to(YearManager.class);
                // bind(IYearBusinessComponent.class).to(YearBusinessComponent.class);
                // bind(IYearService.class).to(YearService.class);
                //
                // bind(IClassManager.class).to(ClassManager.class);
                // bind(IClassBusinessComponent.class).to(ClassBusinessComponent.class);
                // bind(IClassService.class).to(ClassService.class);
                //
                // bind(IBranchManager.class).to(BranchManager.class);
                // bind(IBranchBusinessComponent.class).to(BranchBusinessComponent.class);
                // bind(IBranchService.class).to(BranchService.class);
                //
                // bind(IPeriodManager.class).to(PeriodManager.class);
                // bind(IPeriodBusinessComponent.class).to(PeriodBusinessComponent.class);
                // bind(IPeriodService.class).to(PeriodService.class);
                //
                // bind(IExamManager.class).to(ExamManager.class);
                // bind(IExamBusinessComponent.class).to(ExamBusinessComponent.class);
                // bind(IExamService.class).to(ExamService.class);
                //
                // bind(IPageManager.class).to(PageManager.class);
                // bind(IPageBusinessComponent.class).to(PageBusinessComponent.class);

                // bind(RestTeacherService.class);
                // bind(RestStudentService.class);
                // bind(RestUserService.class);
                // bind(RestYearService.class);
                // bind(RestClassService.class);
                // bind(RestBranchService.class);
                // bind(RestPeriodService.class);
                // bind(RestClassService.class);
                // bind(RestExamService.class);
                bind(TeacherBusinessLocal.class).to(TeacherBusinessBean.class);
                bind(TeacherServiceLocal.class).to(TeacherServiceBean.class);

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
        }
        // , new ShiroModule() {
        // @Override
        // protected void configureShiro() {
        // bindRealm().to(RealmAuthenticationAndAuthorization.class);
        // }
        // }
                );

        // final org.apache.shiro.mgt.SecurityManager securityManager =
        // injector.getInstance(org.apache.shiro.mgt.SecurityManager.class);
        // SecurityUtils.setSecurityManager(securityManager);

        return injector;
    }
}
