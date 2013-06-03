package net.scholagest.initializer;

import net.scholagest.shiro.RealmAuthenticationAndAuthorization;

import org.apache.shiro.guice.ShiroModule;

public class ShiroGuiceContext extends ShiroModule {
    @Override
    protected void configureShiro() {
        bindRealm().to(RealmAuthenticationAndAuthorization.class);
    }
}
