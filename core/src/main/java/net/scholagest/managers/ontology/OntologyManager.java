package net.scholagest.managers.ontology;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.parser.OntologyElement;

import com.google.inject.Inject;

public class OntologyManager {
	
	@Inject
	public OntologyManager() {
	}
	
	public OntologyElement getElementWithName(String requestId,
			ITransaction transaction, String elementName) throws Exception {
		String type = (String) transaction.get(elementName, RDF.type, null);
		
		if (type == null) {
			return null;
		}
		
		OntologyElement element = new OntologyElement();
		element.setType(type);
		element.setName(elementName);
		
		for (String col : transaction.getColumns(elementName)) {
			if (!col.equals(RDF.type)) {
				element.setAttribute(col, (String) transaction.get(elementName, col, null));
			}
		}
		
		return element;
	}

	public boolean isSubtypeOf(String requestId, ITransaction transaction,
			String type, String supertype) throws Exception {
		if (type.equals(supertype))
			return true;
		
		OntologyElement typeElement = getElementWithName(requestId, transaction, type);
		Map<String, Set<OntologyElement>> subElements = typeElement.getSubElements();
		if (subElements.containsKey("rdfs:subClassOf")) {
			Set<OntologyElement> parentSet = subElements.get("rdfs:subClassOf");
			for (OntologyElement parent : parentSet) {
				if (isSubtypeOf(requestId, transaction, parent.getAttributes().get("rdf:ID"), supertype)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
