package net.scholagest.managers.ontology;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.managers.ontology.parser.OntologyElement;

public class Ontology {
	private Set<OntologyElement> ontologyElementSet;
	
	public Ontology() {
		this.ontologyElementSet = new HashSet<>();
	}
	
	public void addOntologyElement(OntologyElement ontologyElement) {
		ontologyElementSet.add(ontologyElement);
	}
	
	public void addAllOntologyElement(Set<OntologyElement> toAdd) {
		ontologyElementSet.addAll(toAdd);
	}
	
	public Set<OntologyElement> getAllOntologyElement() {
		return ontologyElementSet;
	}
	
	public OntologyElement getElementByName(String name) {
		if (name == null)
			return null;
		
		for (OntologyElement ontologyElement : ontologyElementSet) {
			if (name.equals(ontologyElement.getName())) {
				return ontologyElement;
			}
		}
		
		return null;
	}
	
	public Set<OntologyElement> getElementByProperty(String propertyName, String propertyValue) {
		Set<OntologyElement> resultSet = new HashSet<>();
		
		for (OntologyElement ontologyElement : resultSet) {
			String value = ontologyElement.getAttributeWithName(propertyName);
			if (checkValue(propertyValue, value)) {
				resultSet.add(ontologyElement);
			}
		}
		
		return resultSet;
	}

	private boolean checkValue(String propertyValue, String value) {
		if (value == null) {
			return propertyValue == null;
		}
		return value.equals(propertyValue);
	}
}
