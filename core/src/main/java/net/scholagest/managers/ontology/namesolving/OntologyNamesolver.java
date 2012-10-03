package net.scholagest.managers.ontology.namesolving;

import java.util.Set;

import net.scholagest.managers.ontology.Ontology;
import net.scholagest.managers.ontology.OntologyClass;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.RDFS;

public class OntologyNamesolver {
    public void solveNames(Ontology ontology) {
        solveDomainRange(ontology);
    }

    private void solveDomainRange(Ontology ontology) {
        for (OntologyElement ontologyElement : ontology.getElementByType(RDFS.clazz)) {
            Set<OntologyElement> propertiesWithDomain = ontology.getElementByProperty(RDFS.domain, ontologyElement.getName());
            Set<OntologyElement> propertiesWithRange = ontology.getElementByProperty(RDFS.range, ontologyElement.getName());

            if (ontologyElement instanceof OntologyClass) {
                ((OntologyClass) ontologyElement).setPropertiesDomain(propertiesWithDomain);
                ((OntologyClass) ontologyElement).setPropertiesRange(propertiesWithRange);
            }
        }
    }
}
