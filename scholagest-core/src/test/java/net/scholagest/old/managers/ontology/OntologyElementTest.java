package net.scholagest.old.managers.ontology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import net.scholagest.old.managers.ontology.OntologyElement;
import net.scholagest.old.managers.ontology.RDFS;

import org.junit.Test;

public class OntologyElementTest {
    @Test
    public void testHashEquals() {
        OntologyElement ontologyElement1 = new OntologyElement();
        ontologyElement1.setName("className");
        ontologyElement1.setType(RDFS.clazz);

        OntologyElement ontologyElement2 = new OntologyElement();
        ontologyElement2.setName("className");
        ontologyElement2.setType(RDFS.clazz);

        assertEquals(ontologyElement2.hashCode(), ontologyElement1.hashCode());
    }

    @Test
    public void testHashEqualsWithAttributes() {
        OntologyElement ontologyElement1 = new OntologyElement();
        ontologyElement1.setName("className");
        ontologyElement1.setType(RDFS.clazz);

        OntologyElement ontologyElement2 = new OntologyElement();
        ontologyElement2.setName("className");
        ontologyElement2.setType(RDFS.clazz);

        ontologyElement1.setAttribute("attribute1", "value1");

        assertEquals(ontologyElement2.hashCode(), ontologyElement1.hashCode());
    }

    @Test
    public void testHashEqualsWithSubElements() {
        OntologyElement ontologyElement1 = new OntologyElement();
        ontologyElement1.setName("className");
        ontologyElement1.setType(RDFS.clazz);

        OntologyElement ontologyElement2 = new OntologyElement();
        ontologyElement2.setName("className");
        ontologyElement2.setType(RDFS.clazz);

        ontologyElement1.putSubElement(RDFS.domain, new OntologyElement());

        assertEquals(ontologyElement2.hashCode(), ontologyElement1.hashCode());
    }

    @Test
    public void testHashNotEqualsByName() {
        OntologyElement ontologyElement1 = new OntologyElement();
        ontologyElement1.setName("className1");
        ontologyElement1.setType(RDFS.clazz);

        OntologyElement ontologyElement2 = new OntologyElement();
        ontologyElement2.setName("className2");
        ontologyElement2.setType(RDFS.clazz);

        ontologyElement1.putSubElement(RDFS.domain, new OntologyElement());

        assertFalse(ontologyElement2.hashCode() == ontologyElement1.hashCode());
    }

    @Test
    public void testHashNotEqualsByType() {
        OntologyElement ontologyElement1 = new OntologyElement();
        ontologyElement1.setName("className");
        ontologyElement1.setType(RDFS.clazz + "1");

        OntologyElement ontologyElement2 = new OntologyElement();
        ontologyElement2.setName("className");
        ontologyElement2.setType(RDFS.clazz);

        ontologyElement1.putSubElement(RDFS.domain, new OntologyElement());

        assertFalse(ontologyElement2.hashCode() == ontologyElement1.hashCode());
    }
}
