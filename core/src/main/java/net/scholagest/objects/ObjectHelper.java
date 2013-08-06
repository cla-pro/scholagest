package net.scholagest.objects;

import java.util.Collection;
import java.util.UUID;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.RDFS;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ObjectHelper {
    private static final String DATE_TIME_FORMAT_PATTERN = "YYYY-MM-dd HH:mm:ss:SSS";

    private DateTimeFormatter dateTimeFormatter;

    private final IOntologyManager ontologyManager;

    public ObjectHelper(IOntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
        this.dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT_PATTERN);
    }

    public Object getObjectProperty(ITransaction transaction, String objectKey, String propertyName) {
        Object value = transaction.get(objectKey, propertyName, null);
        String type = getPropertyType(transaction, propertyName);
        if (type != null && ontologyManager.isSubtypeOf(type, CoreNamespace.tSet)) {
            if (value == null) {
                value = DBSet.createDBSet(transaction, UUID.randomUUID().toString());
            } else {
                value = new DBSet(transaction, (String) value);
            }
        } else if (type != null && ontologyManager.isSubtypeOf(type, CoreNamespace.tCalendar)) {
            value = dateTimeFormatter.parseDateTime((String) value);
        }
        return value;
    }

    private String getPropertyType(ITransaction transaction, String propertyName) {
        OntologyElement element = ontologyManager.getElementWithName(propertyName);
        if (element == null) {
            return null;
        }
        return element.getAttributeWithName(RDFS.range);
    }

    @SuppressWarnings("unchecked")
    public void setObjectProperty(ITransaction transaction, String objectKey, String propertyName, Object value) throws DatabaseException {
        String type = getPropertyType(transaction, propertyName);

        if (value instanceof DBSet) {
            transaction.insert(objectKey, propertyName, ((DBSet) value).getKey(), null);
        } else if (type != null && ontologyManager.isSubtypeOf(type, CoreNamespace.tSet)) {
            String setKey = (String) transaction.get(objectKey, propertyName, null);
            DBSet dbSet = null;
            if (setKey == null) {
                dbSet = DBSet.createDBSet(transaction, UUID.randomUUID().toString());
                transaction.insert(objectKey, propertyName, dbSet.getKey(), null);
            } else {
                dbSet = new DBSet(transaction, setKey);
            }
            dbSet.clear();

            for (Object element : (Collection<Object>) value) {
                dbSet.add(element.toString());
            }
        } else if (type != null && ontologyManager.isSubtypeOf(type, CoreNamespace.tCalendar)) {
            transaction.insert(objectKey, propertyName, dateTimeFormatter.print((DateTime) value), null);
        } else {
            transaction.insert(objectKey, propertyName, value, null);
        }
    }
}
