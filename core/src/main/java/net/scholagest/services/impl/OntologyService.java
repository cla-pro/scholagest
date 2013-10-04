package net.scholagest.services.impl;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.services.IOntologyService;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationService;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class OntologyService implements IOntologyService {
    private IDatabase database = null;
    private IOntologyBusinessComponent ontologyBusinessComponent;
    private AuthorizationHelper authorizationHelper;

    @Inject
    public OntologyService(IDatabase database, IOntologyBusinessComponent ontologyBusinessComponent) {
        this.database = database;
        this.ontologyBusinessComponent = ontologyBusinessComponent;
        this.authorizationHelper = new AuthorizationHelper(ontologyBusinessComponent);
    }

    @Override
    public OntologyElement getElementWithName(String elementName) throws Exception {
        OntologyElement result = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            result = ontologyBusinessComponent.getElementWithName(elementName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return result;
    }

    @Override
    public boolean isSubtypeOf(String type, String supertype) throws Exception {
        boolean result = false;

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            result = ontologyBusinessComponent.isSubtypeOf(type, supertype);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return result;
    }

    @Override
    public Set<String> filterPropertiesWithCorrectDomain(String domain, Set<String> properties) throws Exception {
        Set<String> result = new HashSet<>();

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            result = ontologyBusinessComponent.filterPropertiesWithCorrectDomain(domain, properties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return result;
    }

    @Override
    public Set<String> getPropertiesForType(String type) throws Exception {
        Set<String> result = new HashSet<>();

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            result = ontologyBusinessComponent.getPropertiesForType(type);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return result;
    }
}
