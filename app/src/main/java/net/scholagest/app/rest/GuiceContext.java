package net.scholagest.app.rest;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.business.BranchBusinessComponent;
import net.scholagest.business.ClassBusinessComponent;
import net.scholagest.business.ExamBusinessComponent;
import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.business.IExamBusinessComponent;
import net.scholagest.business.IPeriodBusinessComponent;
import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.business.IYearBusinessComponent;
import net.scholagest.business.PeriodBusinessComponent;
import net.scholagest.business.StudentBusinessComponent;
import net.scholagest.business.TeacherBusinessComponent;
import net.scholagest.business.YearBusinessComponent;
import net.scholagest.database.Database;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.IDatabase;
import net.scholagest.database.IDatabaseConfiguration;
import net.scholagest.managers.BranchManager;
import net.scholagest.managers.ClassManager;
import net.scholagest.managers.ExamManager;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IStudentManager;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.PeriodManager;
import net.scholagest.managers.StudentManager;
import net.scholagest.managers.TeacherManager;
import net.scholagest.managers.YearManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.services.BranchService;
import net.scholagest.services.ClassService;
import net.scholagest.services.ExamService;
import net.scholagest.services.IBranchService;
import net.scholagest.services.IClassService;
import net.scholagest.services.IExamService;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IPeriodService;
import net.scholagest.services.IStudentService;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.IUserService;
import net.scholagest.services.IYearService;
import net.scholagest.services.OntologyService;
import net.scholagest.services.PeriodService;
import net.scholagest.services.StudentService;
import net.scholagest.services.TeacherService;
import net.scholagest.services.UserService;
import net.scholagest.services.YearService;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceContext extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(IDatabaseConfiguration.class).to(DefaultDatabaseConfiguration.class);
                bind(IDatabase.class).to(Database.class);

                bind(ITeacherManager.class).to(TeacherManager.class);
                bind(ITeacherBusinessComponent.class).to(TeacherBusinessComponent.class);
                bind(ITeacherService.class).to(TeacherService.class);

                bind(OntologyManager.class);
                bind(IOntologyService.class).to(OntologyService.class);

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
        });
    }
}
