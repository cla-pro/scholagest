package net.scholagest.managers.impl;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;

import org.apache.shiro.subject.Subject;

public class AuthorizationManager {
	public boolean canCreateObject(String requestId, ITransaction transaction,
			Subject subject, String objectType) throws Exception {
		if (subject.isPermitted(":*:create")) {
			return true;
		}

		return checkRoles(requestId, transaction, subject, objectType,
				AuthorizationNamespace.pAuthorizationCreate);
	}

	private boolean checkRoles(String requestId, ITransaction transaction,
			Subject subject, String objectType, String action)
			throws DatabaseException {
		Set<String> roles = fetchNodeRoles(requestId, transaction, objectType,
				action);
		for (String role : roles) {
			if (subject.hasRole(role)) {
				return true;
			}
		}

		return false;
	}

	private Set<String> fetchNodeRoles(String requestId,
			ITransaction transaction, String objectType, String action)
			throws DatabaseException {
		Set<String> roles = new HashSet<>();

		String authorizationNode = (String) transaction.get(objectType,
				AuthorizationNamespace.pAuthorization, null);

		if (authorizationNode != null) {
			String roleNode = (String) transaction.get(authorizationNode,
					action, null);

			if (roleNode != null) {
				roles.addAll(transaction.getColumns(roleNode));
			}
		}

		return roles;
	}
}
