package net.scholagest.services;

import net.scholagest.managers.ontology.parser.OntologyElement;

public interface IOntologyService {
	public OntologyElement getElementWithName(String elementName)
			throws Exception;
}
