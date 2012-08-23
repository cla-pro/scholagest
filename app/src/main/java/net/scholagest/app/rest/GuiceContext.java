package net.scholagest.app.rest;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.database.Database;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.IDatabase;
import net.scholagest.database.IDatabaseConfiguration;
import net.scholagest.managers.IStudentManager;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.managers.StudentManager;
import net.scholagest.managers.TeacherManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IStudentService;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.IUserService;
import net.scholagest.services.OntologyService;
import net.scholagest.services.StudentService;
import net.scholagest.services.TeacherService;
import net.scholagest.services.UserService;

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
				bind(IDatabaseConfiguration.class).to(
						DefaultDatabaseConfiguration.class);
				bind(IDatabase.class).to(Database.class);

				bind(ITeacherManager.class).to(TeacherManager.class);
				bind(ITeacherService.class).to(TeacherService.class);
				bind(OntologyManager.class);
				bind(IOntologyService.class).to(OntologyService.class);
				bind(IUserService.class).to(UserService.class);
				bind(IStudentManager.class).to(StudentManager.class);
				bind(IStudentService.class).to(StudentService.class);

				bind(RestTeacherService.class);
				bind(RestStudentService.class);
				bind(RestUserService.class);

				// Route all requests through GuiceContainer
				Map<String, String> params = new HashMap<>();
				params.put("com.sun.jersey.config.property.packages",
						"net.scholagest.rest");
				serve("/services/*").with(GuiceContainer.class, params);
			}
		});
	}
}
