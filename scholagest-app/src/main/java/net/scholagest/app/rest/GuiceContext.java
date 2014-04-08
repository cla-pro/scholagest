package net.scholagest.app.rest;

import net.scholagest.app.rest.guice.ScholagestJerseyServletModule;
import net.scholagest.app.rest.guice.ScholagestShiroModule;

import org.apache.shiro.SecurityUtils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceContext extends GuiceServletContextListener {
    private static final String SCHOLAGEST_PERSISTENCE_UNIT = "scholagest-pu";

    private final String persistenceUnitName;

    public GuiceContext() {
        this.persistenceUnitName = SCHOLAGEST_PERSISTENCE_UNIT;
    }

    public GuiceContext(final String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    @Override
    protected Injector getInjector() {
        final Injector injector = Guice.createInjector(new JpaPersistModule(persistenceUnitName), new ScholagestJerseyServletModule(),
                new ScholagestShiroModule());

        final org.apache.shiro.mgt.SecurityManager securityManager = injector.getInstance(org.apache.shiro.mgt.SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);

        return injector;
    }
}
