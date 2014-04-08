package net.scholagest.initializer;

import net.scholagest.old.business.IBranchBusinessComponent;
import net.scholagest.old.business.IClassBusinessComponent;
import net.scholagest.old.business.IExamBusinessComponent;
import net.scholagest.old.business.IOntologyBusinessComponent;
import net.scholagest.old.business.IPageBusinessComponent;
import net.scholagest.old.business.IPeriodBusinessComponent;
import net.scholagest.old.business.IStudentBusinessComponent;
import net.scholagest.old.business.ITeacherBusinessComponent;
import net.scholagest.old.business.IUserBusinessComponent;
import net.scholagest.old.business.IYearBusinessComponent;
import net.scholagest.old.business.impl.BranchBusinessComponent;
import net.scholagest.old.business.impl.ClassBusinessComponent;
import net.scholagest.old.business.impl.ExamBusinessComponent;
import net.scholagest.old.business.impl.OntologyBusinessComponent;
import net.scholagest.old.business.impl.PageBusinessComponent;
import net.scholagest.old.business.impl.PeriodBusinessComponent;
import net.scholagest.old.business.impl.StudentBusinessComponent;
import net.scholagest.old.business.impl.TeacherBusinessComponent;
import net.scholagest.old.business.impl.UserBusinessComponent;
import net.scholagest.old.business.impl.YearBusinessComponent;
import net.scholagest.old.database.Database;
import net.scholagest.old.database.DefaultDatabaseConfiguration;
import net.scholagest.old.database.IDatabase;
import net.scholagest.old.database.IDatabaseConfiguration;
import net.scholagest.old.managers.IBranchManager;
import net.scholagest.old.managers.IClassManager;
import net.scholagest.old.managers.IExamManager;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.IPageManager;
import net.scholagest.old.managers.IPeriodManager;
import net.scholagest.old.managers.IStudentManager;
import net.scholagest.old.managers.ITeacherManager;
import net.scholagest.old.managers.IUserManager;
import net.scholagest.old.managers.IYearManager;
import net.scholagest.old.managers.impl.BranchManager;
import net.scholagest.old.managers.impl.ClassManager;
import net.scholagest.old.managers.impl.ExamManager;
import net.scholagest.old.managers.impl.PageManager;
import net.scholagest.old.managers.impl.PeriodManager;
import net.scholagest.old.managers.impl.StudentManager;
import net.scholagest.old.managers.impl.TeacherManager;
import net.scholagest.old.managers.impl.UserManager;
import net.scholagest.old.managers.impl.YearManager;
import net.scholagest.old.managers.ontology.OntologyManager;
import net.scholagest.old.services.IBranchService;
import net.scholagest.old.services.IClassService;
import net.scholagest.old.services.IExamService;
import net.scholagest.old.services.IOntologyService;
import net.scholagest.old.services.IPeriodService;
import net.scholagest.old.services.IStudentService;
import net.scholagest.old.services.ITeacherService;
import net.scholagest.old.services.IUserService;
import net.scholagest.old.services.IYearService;
import net.scholagest.old.services.impl.BranchService;
import net.scholagest.old.services.impl.ClassService;
import net.scholagest.old.services.impl.ExamService;
import net.scholagest.old.services.impl.OntologyService;
import net.scholagest.old.services.impl.PeriodService;
import net.scholagest.old.services.impl.StudentService;
import net.scholagest.old.services.impl.TeacherService;
import net.scholagest.old.services.impl.UserService;
import net.scholagest.old.services.impl.YearService;

import com.google.inject.AbstractModule;

public class GuiceContext extends AbstractModule {
    @Override
    protected void configure() {
        bind(IDatabaseConfiguration.class).to(DefaultDatabaseConfiguration.class);
        bind(IDatabase.class).to(Database.class);

        bind(ITeacherManager.class).to(TeacherManager.class);
        bind(ITeacherBusinessComponent.class).to(TeacherBusinessComponent.class);
        bind(ITeacherService.class).to(TeacherService.class);

        bind(IOntologyManager.class).to(OntologyManager.class);
        bind(IOntologyBusinessComponent.class).to(OntologyBusinessComponent.class);
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

        bind(IPageManager.class).to(PageManager.class);
        bind(IPageBusinessComponent.class).to(PageBusinessComponent.class);

        bind(SystemInitializer.class);
    }
}
