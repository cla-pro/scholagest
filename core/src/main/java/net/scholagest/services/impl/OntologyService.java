package net.scholagest.services.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.services.IOntologyService;

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
        String requestId = UUID.randomUUID().toString();

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            result = this.ontologyManager.getElementWithName(requestId, transaction, elementName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean isSubtypeOf(String type, String supertype) throws Exception {
        boolean result = false;
        String requestId = UUID.randomUUID().toString();

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            result = this.ontologyManager.isSubtypeOf(requestId, transaction, type, supertype);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Set<String> filterPropertiesWithCorrectDomain(String domain, Set<String> properties) throws Exception {
        Set<String> result = new HashSet<>();
        String requestId = UUID.randomUUID().toString();

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            result = this.ontologyManager.filterPropertiesWithCorrectDomain(requestId, transaction, domain, properties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Set<String> getPropertiesForType(String type) throws Exception {
        Set<String> result = new HashSet<>();
        String requestId = UUID.randomUUID().toString();

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            result = this.ontologyManager.getPropertiesForType(requestId, transaction, type);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return result;
    }
}
