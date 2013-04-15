package net.scholagest.app.rest;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.business.IExamBusinessComponent;
import net.scholagest.business.IPeriodBusinessComponent;
import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.business.IYearBusinessComponent;
import net.scholagest.business.impl.BranchBusinessComponent;
import net.scholagest.business.impl.ClassBusinessComponent;
import net.scholagest.business.impl.ExamBusinessComponent;
import net.scholagest.business.impl.PeriodBusinessComponent;
import net.scholagest.business.impl.StudentBusinessComponent;
import net.scholagest.business.impl.TeacherBusinessComponent;
import net.scholagest.business.impl.UserBusinessComponent;
import net.scholagest.business.impl.YearBusinessComponent;
import net.scholagest.database.Database;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.IDatabase;
import net.scholagest.database.IDatabaseConfiguration;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IStudentManager;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.managers.IUserManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.impl.BranchManager;
import net.scholagest.managers.impl.ClassManager;
import net.scholagest.managers.impl.ExamManager;
import net.scholagest.managers.impl.PeriodManager;
import net.scholagest.managers.impl.StudentManager;
import net.scholagest.managers.impl.TeacherManager;
import net.scholagest.managers.impl.UserManager;
import net.scholagest.managers.impl.YearManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.services.IBranchService;
import net.scholagest.services.IClassService;
import net.scholagest.services.IExamService;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IPeriodService;
import net.scholagest.services.IStudentService;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.IUserService;
import net.scholagest.services.IYearService;
import net.scholagest.services.impl.BranchService;
import net.scholagest.services.impl.ClassService;
import net.scholagest.services.impl.ExamService;
import net.scholagest.services.impl.OntologyService;
import net.scholagest.services.impl.PeriodService;
import net.scholagest.services.impl.StudentService;
import net.scholagest.services.impl.TeacherService;
import net.scholagest.services.impl.UserService;
import net.scholagest.services.impl.YearService;
import net.scholagest.shiro.RealmAuthenticationAndAuthorization;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.guice.ShiroModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceContext extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        Injector injector = Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(IDatabaseConfiguration.class).to(DefaultDatabaseConfiguration.class);
                bind(IDatabase.class).to(Database.class);

                bind(ITeacherManager.class).to(TeacherManager.class);
                bind(ITeacherBusinessComponent.class).to(TeacherBusinessComponent.class);
                bind(ITeacherService.class).to(TeacherService.class);

                bind(OntologyManager.class);
                bind(IOntologyService.class).to(OntologyService.class);

                bind(IUserManager.class).to(UserManager.class);
                bind(IUserBusinessComponent.class).to(UserBusinessComponent.class);
                bind(IUserService.class).to(UserService.class);

                bind(IStudentManager.class).to(StudentManager.class);
                bind(IStudentBusinessComponent.class).to(StudentBusinessComponent.class);
                bind(IStudentService.class).to(StudentService.class);

                bind(IYearManager.class).to(YearManager.class);
                bind(IYearBusinessComponent.class).to(YearBusinessComponent.class);
                bind(IYearService.class).to(YearService.class);

                bind(IClassManager.class).to(ClassManager.class);
                bind(IClassBusinessComponent.class).to(ClassBusinessComponent.class);
                bind(IClassService.class).to(ClassService.class);

                bind(IBranchManager.class).to(BranchManager.class);
                bind(IBranchBusinessComponent.class).to(BranchBusinessComponent.class);
                bind(IBranchService.class).to(BranchService.class);

                bind(IPeriodManager.class).to(PeriodManager.class);
                bind(IPeriodBusinessComponent.class).to(PeriodBusinessComponent.class);
                bind(IPeriodService.class).to(PeriodService.class);

                bind(IExamManager.class).to(ExamManager.class);
                bind(IExamBusinessComponent.class).to(ExamBusinessComponent.class);
                bind(IExamService.class).to(ExamService.class);

                bind(RestTeacherService.class);
                bind(RestStudentService.class);
                bind(RestUserService.class);
                bind(RestYearService.class);
                bind(RestClassService.class);
                bind(RestBranchService.class);
                bind(RestPeriodService.class);
                bind(RestClassService.class);
                bind(RestExamService.class);

                // Route all requests through GuiceContainer
                Map<String, String> params = new HashMap<>();
                params.put("com.sun.jersey.config.property.packages", "net.scholagest.rest");
                serve("/services/*").with(GuiceContainer.class, params);
            }
        }, new ShiroModule() {
            @Override
            protected void configureShiro() {
                bindRealm().to(RealmAuthenticationAndAuthorization.class);
            }
        });

        org.apache.shiro.mgt.SecurityManager securityManager = injector.getInstance(org.apache.shiro.mgt.SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);

        return injector;
    }
}
