package net.scholagest.managers.ontology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.managers.IOntologyManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Test;

public class OntologyManagerTest extends AbstractTestWithTransaction {
    @Test
    public void testGetElementWithNameClass() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Ontology" });

        IOntologyManager manager = new OntologyManager();
        OntologyElement elementWithName = manager.getElementWithName(CoreNamespace.tStudent);
        assertNotNull(elementWithName);
        assertEquals(RDFS.clazz, elementWithName.getType());
        assertEquals(CoreNamespace.tStudent, elementWithName.getName());
    }

    @Test
    public void testGetElementWithNameProperty() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Ontology" });

        IOntologyManager manager = new OntologyManager();
        OntologyElement elementWithName = manager.getElementWithName(CoreNamespace.pStudentPersonalInfo);
        assertNotNull(elementWithName);
        assertEquals(RDFS.property, elementWithName.getType());
        assertEquals(CoreNamespace.pStudentPersonalInfo, elementWithName.getName());

        assertEquals(CoreNamespace.tStudent, elementWithName.getAttributeWithName(RDFS.domain));
        assertEquals(CoreNamespace.tStudentPersonalInfo, elementWithName.getAttributeWithName(RDFS.range));
    }

    @Test
    public void testIsSubTypeOf() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Ontology" });

        IOntologyManager manager = new OntologyManager();
        assertFalse(manager.isSubtypeOf(CoreNamespace.tStudent, CoreNamespace.tGroup));
        assertTrue(manager.isSubtypeOf(CoreNamespace.tStudentPersonalInfo, CoreNamespace.tGroup));
    }

    @Test
    public void testFilterPropertiesWithCorrectDomain() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Ontology" });

        IOntologyManager manager = new OntologyManager();

        String domain = CoreNamespace.tTeacher;
        Set<String> notFilteredProperties = new HashSet<>();
        notFilteredProperties.add("pTeacherLastName");
        notFilteredProperties.add("pStudentLastName");

        Set<String> filtered = manager.filterPropertiesWithCorrectDomain(domain, notFilteredProperties);
        assertNotNull(filtered);
        assertEquals(1, filtered.size());
        assertTrue(filtered.contains("pTeacherLastName"));
    }

    @Test
    public void testGetPropertiesForType() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Ontology" });

        String[] expectedProperties = { CoreNamespace.pStudentPersonalInfo, CoreNamespace.pStudentMedicalInfo };

        IOntologyManager manager = new OntologyManager();
        Set<String> propertiesForType = manager.getPropertiesForType(CoreNamespace.tStudent);

        assertNotNull(propertiesForType);
        assertEquals(2, propertiesForType.size());

        for (String property : expectedProperties) {
            assertTrue(propertiesForType.contains(property));
        }
    }
}
