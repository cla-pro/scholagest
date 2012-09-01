package net.scholagest.managers.ontology.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OntologyElement {
	private String type = null;
	private String name = null;
	
	private Map<String, String> attributes = null;
	private Map<String, Set<OntologyElement>> subElements = null;
	
	public OntologyElement() {
		this.attributes = new HashMap<>();
		this.subElements = new HashMap<>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public void setAttribute(String name, String value) {
		attributes.put(name, value);
	}

	public Map<String, Set<OntologyElement>> getSubElements() {
		return subElements;
	}
	
	public void putSubElement(String type, OntologyElement subElement) {
		Set<OntologyElement> set = subElements.get(type);
		if (set == null) {
			set = new HashSet<>();
			subElements.put(type, set);
		}
		
		set.add(subElement);
	}
	
	public OntologyElement getSingleSubElementWithName(String subElementName) {
		Set<OntologyElement> set = subElements.get(subElementName);
		if (set == null || set.size() == 0) {
			return null;
		}
		else if (set.size() >= 1) {
			return set.iterator().next();
		}
		
		return null;
	}
	
	public String getAttributeWithName(String name) {
		return attributes.get(name);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof OntologyElement)) {
			return false;
		}
		OntologyElement ontologyElement = (OntologyElement) other;
		if (name == null) {
			return ontologyElement.getName() == null;
		}
		if (ontologyElement.getName() == null) {
			return false;
		}
		return name.equals(ontologyElement.getName());
	}
}
