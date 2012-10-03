package net.scholagest.managers.ontology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.scholagest.managers.CoreNamespace;
import net.scholagest.utils.AbstractTestWithTransaction;

import org.junit.Test;

public class OntologyManagerTest extends AbstractTestWithTransaction {
    @Test
    public void testGetElementWithNameClass() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Ontology" });

        OntologyManager manager = new OntologyManager();
        OntologyElement elementWithName = manager.getElementWithName(UUID.randomUUID().toString(), transaction, CoreNamespace.tStudent);
        assertNotNull(elementWithName);
        assertEquals(RDFS.clazz, elementWithName.getType());
        assertEquals(CoreNamespace.tStudent, elementWithName.getName());
    }

    @Test
    public void testGetElementWithNameProperty() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Ontology" });

        OntologyManager manager = new OntologyManager();
        OntologyElement elementWithName = manager.getElementWithName(UUID.randomUUID().toString(), transaction, CoreNamespace.pStudentPersonalInfo);
        assertNotNull(elementWithName);
        assertEquals(RDFS.property, elementWithName.getType());
        assertEquals(CoreNamespace.pStudentPersonalInfo, elementWithName.getName());

        assertEquals(CoreNamespace.tStudent, elementWithName.getAttributeWithName(RDFS.domain));
        assertEquals(CoreNamespace.tStudentPersonalInfo, elementWithName.getAttributeWithName(RDFS.range));
    }

    @Test
    public void testIsSubTypeOf() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Ontology" });

        OntologyManager manager = new OntologyManager();
        assertFalse(manager.isSubtypeOf(UUID.randomUUID().toString(), transaction, CoreNamespace.tStudent, CoreNamespace.tGroup));
        assertTrue(manager.isSubtypeOf(UUID.randomUUID().toString(), transaction, CoreNamespace.tStudentPersonalInfo, CoreNamespace.tGroup));
    }

    @Test
    public void testFilterPropertiesWithCorrectDomain() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Ontology" });

        OntologyManager manager = new OntologyManager();

        String domain = CoreNamespace.tTeacher;
        Set<String> notFilteredProperties = new HashSet<>();
        notFilteredProperties.add("pTeacherLastName");
        notFilteredProperties.add("pStudentLastName");

        Set<String> filtered = manager.filterPropertiesWithCorrectDomain(UUID.randomUUID().toString(), transaction, domain, notFilteredProperties);
        assertNotNull(filtered);
        assertEquals(1, filtered.size());
        assertTrue(filtered.contains("pTeacherLastName"));
    }

    @Test
    public void testGetPropertiesForType() throws Exception {
        super.fillTransactionWithDataSets(new String[] { "Ontology" });

        String[] expectedProperties = { CoreNamespace.pStudentPersonalInfo, CoreNamespace.pStudentMedicalInfo };

        OntologyManager manager = new OntologyManager();
        Set<String> propertiesForType = manager.getPropertiesForType(UUID.randomUUID().toString(), transaction, CoreNamespace.tStudent);

        assertNotNull(propertiesForType);
        assertEquals(2, propertiesForType.size());

        for (String property : expectedProperties) {
            assertTrue(propertiesForType.contains(property));
        }
    }
}
