package net.scholagest.managers.ontology.parser;

import net.scholagest.managers.ontology.OntologyClass;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.RDFS;

public class OntologyElementFactory {
    public OntologyElement createOntologyElement(String type) {
        if (type.equals(RDFS.clazz)) {
            return new OntologyClass();
        }
        return new OntologyElement();
    }
}
