package net.scholagest.app.rest.guice;

import net.scholagest.authorization.RealmAuthenticationAndAuthorization;

import org.apache.shiro.guice.ShiroModule;

/**
 * Configure the ShiroModule via Guice
 * 
 * @author CLA
 * @since 0.15.0
 */
public class ScholagestShiroModule extends ShiroModule {
    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureShiro() {
        bindRealm().to(RealmAuthenticationAndAuthorization.class);
    }
}
