package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface IClassBusinessComponent {

    String createClass(String requestId, ITransaction transaction, Map<String, Object> classProperties) throws Exception;

    Map<String, Set<String>> getClassesForYear(String requestId, ITransaction transaction, Set<String> yearKeySet) throws Exception;

    Map<String, Object> getClassProperties(String requestId, ITransaction transaction, String classKey, Set<String> propertiesName) throws Exception;
}
