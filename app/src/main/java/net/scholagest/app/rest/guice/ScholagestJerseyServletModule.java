package net.scholagest.app.rest.guice;

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
import net.scholagest.app.rest.ws.StudentClassesRest;
import net.scholagest.app.rest.ws.StudentMedicalsRest;
import net.scholagest.app.rest.ws.StudentPersonalsRest;
import net.scholagest.app.rest.ws.StudentsRest;
import net.scholagest.app.rest.ws.TeacherDetailsRest;
import net.scholagest.app.rest.ws.TeachersRest;
import net.scholagest.app.rest.ws.UsersRest;
import net.scholagest.app.rest.ws.YearsRest;
import net.scholagest.app.rest.ws.authorization.AuthorizationFilter;
import net.scholagest.app.rest.ws.authorization.AuthorizationVerifier;
import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.authorization.AuthorizationInterceptor;
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.BranchBusinessBean;
import net.scholagest.business.BranchBusinessLocal;
import net.scholagest.business.BranchPeriodBusinessBean;
import net.scholagest.business.BranchPeriodBusinessLocal;
import net.scholagest.business.ClazzBusinessBean;
import net.scholagest.business.ClazzBusinessLocal;
import net.scholagest.business.ExamBusinessBean;
import net.scholagest.business.ExamBusinessLocal;
import net.scholagest.business.PeriodBusinessBean;
import net.scholagest.business.PeriodBusinessLocal;
import net.scholagest.business.ResultBusinessBean;
import net.scholagest.business.ResultBusinessLocal;
import net.scholagest.business.SessionBusinessBean;
import net.scholagest.business.SessionBusinessLocal;
import net.scholagest.business.StudentBusinessBean;
import net.scholagest.business.StudentBusinessLocal;
import net.scholagest.business.StudentResultBusinessBean;
import net.scholagest.business.StudentResultBusinessLocal;
import net.scholagest.business.TeacherBusinessBean;
import net.scholagest.business.TeacherBusinessLocal;
import net.scholagest.business.UserBusinessBean;
import net.scholagest.business.UserBusinessLocal;
import net.scholagest.business.YearBusinessBean;
import net.scholagest.business.YearBusinessLocal;
import net.scholagest.service.BranchPeriodServiceBean;
import net.scholagest.service.BranchPeriodServiceLocal;
import net.scholagest.service.BranchServiceBean;
import net.scholagest.service.BranchServiceLocal;
import net.scholagest.service.ClazzServiceBean;
import net.scholagest.service.ClazzServiceLocal;
import net.scholagest.service.ExamServiceBean;
import net.scholagest.service.ExamServiceLocal;
import net.scholagest.service.PeriodServiceBean;
import net.scholagest.service.PeriodServiceLocal;
import net.scholagest.service.ResultServiceBean;
import net.scholagest.service.ResultServiceLocal;
import net.scholagest.service.SessionServiceBean;
import net.scholagest.service.SessionServiceLocal;
import net.scholagest.service.StudentResultServiceBean;
import net.scholagest.service.StudentResultServiceLocal;
import net.scholagest.service.StudentServiceBean;
import net.scholagest.service.StudentServiceLocal;
import net.scholagest.service.TeacherServiceBean;
import net.scholagest.service.TeacherServiceLocal;
import net.scholagest.service.UserServiceBean;
import net.scholagest.service.UserServiceLocal;
import net.scholagest.service.YearServiceBean;
import net.scholagest.service.YearServiceLocal;

import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.persist.PersistFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Configure via Guice the JerseyServletModule
 * 
 * @author CLA
 * @since 0.15.0
 */
public class ScholagestJerseyServletModule extends JerseyServletModule {
    @Override
    protected void configureServlets() {
        bind(AuthorizationFilter.class).in(Singleton.class);

        final AuthorizationVerifier authorizationVerifier = new AuthorizationVerifier();
        requestInjection(authorizationVerifier);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(CheckAuthorization.class), authorizationVerifier);

        final AuthorizationInterceptor authorizationInterceptor = new AuthorizationInterceptor();
        requestInjection(authorizationInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(RolesAndPermissions.class), authorizationInterceptor);

        bind(BranchPeriodBusinessLocal.class).to(BranchPeriodBusinessBean.class);
        bind(BranchBusinessLocal.class).to(BranchBusinessBean.class);
        bind(ClazzBusinessLocal.class).to(ClazzBusinessBean.class);
        bind(ExamBusinessLocal.class).to(ExamBusinessBean.class);
        bind(PeriodBusinessLocal.class).to(PeriodBusinessBean.class);
        bind(ResultBusinessLocal.class).to(ResultBusinessBean.class);
        bind(SessionBusinessLocal.class).to(SessionBusinessBean.class);
        bind(StudentBusinessLocal.class).to(StudentBusinessBean.class);
        bind(StudentResultBusinessLocal.class).to(StudentResultBusinessBean.class);
        bind(TeacherBusinessLocal.class).to(TeacherBusinessBean.class);
        bind(UserBusinessLocal.class).to(UserBusinessBean.class);
        bind(YearBusinessLocal.class).to(YearBusinessBean.class);

        bind(BranchPeriodServiceLocal.class).to(BranchPeriodServiceBean.class);
        bind(BranchServiceLocal.class).to(BranchServiceBean.class);
        bind(ClazzServiceLocal.class).to(ClazzServiceBean.class);
        bind(ExamServiceLocal.class).to(ExamServiceBean.class);
        bind(PeriodServiceLocal.class).to(PeriodServiceBean.class);
        bind(ResultServiceLocal.class).to(ResultServiceBean.class);
        bind(SessionServiceLocal.class).to(SessionServiceBean.class);
        bind(StudentServiceLocal.class).to(StudentServiceBean.class);
        bind(StudentResultServiceLocal.class).to(StudentResultServiceBean.class);
        bind(TeacherServiceLocal.class).to(TeacherServiceBean.class);
        bind(UserServiceLocal.class).to(UserServiceBean.class);
        bind(YearServiceLocal.class).to(YearServiceBean.class);

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
        bind(StudentClassesRest.class);
        bind(TeachersRest.class);
        bind(TeacherDetailsRest.class);

        // Route all requests through GuiceContainer
        final Map<String, String> params = new HashMap<>();
        // params.put("com.sun.jersey.config.property.packages",
        // "net.scholagest.rest.ws");
        params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        filter("/services/*").through(AuthorizationFilter.class);
        filter("/services/*").through(PersistFilter.class);
        serve("/services/*").with(GuiceContainer.class, params);
    }
}
