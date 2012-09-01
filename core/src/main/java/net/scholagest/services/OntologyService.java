package net.scholagest.services;

import java.util.UUID;

import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.parser.OntologyElement;

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
	public OntologyElement getElementWithName(String elementName)
			throws Exception {
		OntologyElement result = null;
		String requestId = UUID.randomUUID().toString();

		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			result = this.ontologyManager.getElementWithName(requestId,
					transaction, elementName);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}

		return result;
	}
	
	public boolean isSubtypeOf(String type, String supertype) throws Exception {
		boolean result = false;
		String requestId = UUID.randomUUID().toString();

		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			result = this.ontologyManager.isSubtypeOf(requestId, transaction, type, supertype);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}

		return result;
	}
}
