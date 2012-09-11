package net.scholagest.managers.ontology.saver;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.ontology.Ontology;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.managers.ontology.parser.OntologyElement;

public class OntologySaver {
	public void saveOntology(String requestId, ITransaction transaction,
			Ontology ontology) throws Exception {
		for (OntologyElement element : ontology.getAllOntologyElement()) {
			this.saveElement(requestId, transaction, element);
			
			transaction.insert(CoreNamespace.ontologyBase,
					element.getName(), element.getName(), null);
		}
	}
	
	private void saveElement(String requestId, ITransaction transaction,
			OntologyElement element) throws Exception {
		transaction.insert(element.getName(), RDF.type,
				element.getType(), null);
		System.out.println("Type: " + element.getType());
		
		for (Map.Entry<String, Set<OntologyElement>> subElement :
				element.getSubElements().entrySet()) {
			for (OntologyElement sub : subElement.getValue()) {
				if (sub.getAttributes().containsKey(RDF.resource)) {
					transaction.insert(element.getName(), sub.getType(),
							sub.getAttributes().get(RDF.resource), null);
				}
			}
		}
	}
}
