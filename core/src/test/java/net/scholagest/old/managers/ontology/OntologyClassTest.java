package net.scholagest.old.managers.ontology;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.old.managers.ontology.OntologyClass;
import net.scholagest.old.managers.ontology.OntologyElement;
import net.scholagest.old.managers.ontology.RDFS;

import org.junit.Test;

public class OntologyClassTest {
    @Test
    public void testHashEquals() {
        OntologyClass ontologyClass = new OntologyClass();
        ontologyClass.setName("className");
        ontologyClass.setType(RDFS.clazz);

        OntologyElement ontologyElement = new OntologyElement();
        ontologyElement.setName("className");
        ontologyElement.setType(RDFS.clazz);

        assertEquals(ontologyElement.hashCode(), ontologyClass.hashCode());
    }

    @Test
    public void testHashEqualsWithProperties() {
        OntologyClass ontologyClass = new OntologyClass();
        ontologyClass.setName("className");
        ontologyClass.setType(RDFS.clazz);

        OntologyElement ontologyElement = new OntologyElement();
        ontologyElement.setName("className");
        ontologyElement.setType(RDFS.clazz);

        Set<OntologyElement> propertiesDomain = new HashSet<>();
        propertiesDomain.add(new OntologyElement());
        ontologyClass.setPropertiesDomain(propertiesDomain);
        Set<OntologyElement> propertiesRange = new HashSet<>();
        propertiesRange.add(new OntologyElement());
        ontologyClass.setPropertiesRange(propertiesRange);

        assertEquals(ontologyElement.hashCode(), ontologyClass.hashCode());
    }
}
