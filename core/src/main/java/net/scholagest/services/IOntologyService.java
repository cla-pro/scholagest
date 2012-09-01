package net.scholagest.services;

import net.scholagest.managers.ontology.parser.OntologyElement;

public interface IOntologyService {
	public OntologyElement getElementWithName(String elementName)
			throws Exception;

	boolean isSubtypeOf(String type, String supertype) throws Exception;
}
