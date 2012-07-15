package net.scholagest.managers.ontology.namesolving;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.managers.ontology.RDF;
import net.scholagest.managers.ontology.parser.OntologyElement;

public class OntologyNamesolver {
	private Map<String, OntologyElement> elements = new HashMap<>();
	
	public void solveNames(Map<String, Set<OntologyElement>> ontology)
			throws Exception {
		//Find the elements (by name).
		for (Map.Entry<String, Set<OntologyElement>> entry
				: ontology.entrySet()) {
			for (OntologyElement element : entry.getValue()) {
				this.findElement(element);
			}
		}
		
		//Check the references.
		for (Map.Entry<String, Set<OntologyElement>> entry
				: ontology.entrySet()) {
			for (OntologyElement element : entry.getValue()) {
				this.handleElement(element);
			}
		}
	}
	
	private void findElement(OntologyElement element) throws Exception {
		Map<String, String> attributes = element.getAttributes();
		
		//Extract the element's name.
		String name = attributes.get(RDF.id);
		if (name == null) {
			name = attributes.get(RDF.about);
		}
		
		if (name == null)
			return;
		
		this.elements.put(name, element);
	}
	
	private void handleElement(OntologyElement element) throws Exception {
		Map<String, String> attributes = element.getAttributes();

		String resource = attributes.get(RDF.resource);
		if (resource != null) {
			if (!elements.containsKey(resource)) {
				throw new Exception("Reference to " + resource
						+ " not valid");
			}
		}
	}
}
