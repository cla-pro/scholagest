package net.scholagest.managers.impl;

import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IUserManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.TokenObject;
import net.scholagest.objects.UserObject;

import com.google.inject.Inject;

public class UserManager extends ObjectManager implements IUserManager {
    @Inject
    public UserManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public UserObject createUser(String requestId, ITransaction transaction, String username) throws Exception {
        UserObject userObject = createUserObject(transaction, username);

        persistObject(requestId, transaction, userObject);

        return userObject;
    }

    private UserObject createUserObject(ITransaction transaction, String username) {
        UserObject userObject = new UserObject(UUID.randomUUID().toString());

        userObject.setUsername(username);
        userObject.setPermissions(new DBSet(transaction, UUID.randomUUID().toString()));
        userObject.setRoles(new DBSet(transaction, UUID.randomUUID().toString()));

        return userObject;
    }

    @Override
    public UserObject getUser(String requestId, ITransaction transaction, String userKey) throws Exception {
        UserObject userObject = new UserObject(userKey);

        userObject.setProperties(getAllObjectProperties(requestId, transaction, userKey));

        return userObject;
    }

    @Override
    public TokenObject createToken(String requestId, ITransaction transaction, String userKey, String tokenId) throws Exception {
        TokenObject tokenObject = createTokenObject(userKey, tokenId);

        persistObject(requestId, transaction, tokenObject);

        transaction.insert(CoreNamespace.tokenBase, tokenObject.getKey(), userKey, null);

        return tokenObject;
    }

    private TokenObject createTokenObject(String userKey, String tokenId) {
        TokenObject tokenObject = new TokenObject(tokenId);

        tokenObject.setUserObjectKey(userKey);

        return tokenObject;
    }

    @Override
    public TokenObject getToken(String requestId, ITransaction transaction, String tokenId) throws Exception {
        TokenObject tokenObject = null;

        if (isTokenExists(transaction, tokenId)) {
            return readTokenObject(requestId, transaction, tokenId);
        }

        return tokenObject;
    }

    private TokenObject readTokenObject(String requestId, ITransaction transaction, String tokenId) throws Exception {
        TokenObject tokenObject = new TokenObject(tokenId);

        tokenObject.setProperties(getAllObjectProperties(requestId, transaction, tokenId));

        return tokenObject;
    }

    private boolean isTokenExists(ITransaction transaction, String tokenId) throws Exception {
        return transaction.get(CoreNamespace.tokenBase, tokenId, null) != null;
    }

    @Override
    public void deleteToken(String requestId, ITransaction transaction, String tokenId) throws Exception {
        String tokenKey = (String) transaction.get(CoreNamespace.tokenBase, tokenId, null);
        if (tokenKey != null) {
            transaction.delete(CoreNamespace.tokenBase, tokenId, null);
            deleteObject(requestId, transaction, tokenKey);
        }
    }
}
