package net.scholagest.services.impl;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.namespace.AuthorizationNamespace;
import net.scholagest.services.IOntologyService;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class OntologyService implements IOntologyService {
    private IDatabase database = null;
    private OntologyManager ontologyManager = null;

    @Inject
    public OntologyService(IDatabase database, OntologyManager ontologyManager) {
        this.database = database;
        this.ontologyManager = ontologyManager;
    }

    @Override
    public OntologyElement getElementWithName(String elementName) throws Exception {
        OntologyElement result = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());

            result = this.ontologyManager.getElementWithName(elementName);

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

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());

            result = this.ontologyManager.isSubtypeOf(type, supertype);

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

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());

            result = this.ontologyManager.filterPropertiesWithCorrectDomain(domain, properties);

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

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());

            result = this.ontologyManager.getPropertiesForType(type);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return result;
    }
}
