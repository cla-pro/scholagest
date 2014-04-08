package net.scholagest.old.managers.ontology.parser;

import net.scholagest.old.managers.ontology.OntologyClass;
import net.scholagest.old.managers.ontology.OntologyElement;
import net.scholagest.old.managers.ontology.RDFS;

public class OntologyElementFactory {
    public OntologyElement createOntologyElement(String type) {
        if (type.equals(RDFS.clazz)) {
            return new OntologyClass();
        }
        return new OntologyElement();
    }
}
