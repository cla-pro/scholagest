package net.scholagest.shiro;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IUserManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.TokenObject;
import net.scholagest.objects.UserObject;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.google.inject.Inject;

public class RealmAuthenticationAndAuthorization extends AuthorizingRealm {
    private static final String ROLES_KEY = "roles";
    private static final String PERMISSIONS_KEY = "permissions";
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String TRANSACTION_KEY = "transaction";

    private final IUserManager userManager;

    @Inject
    public RealmAuthenticationAndAuthorization(IUserManager userManager) {
        this.userManager = userManager;
        super.setAuthenticationCachingEnabled(false);
        super.setAuthorizationCachingEnabled(false);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();

        for (Object roleObject : principals.fromRealm(ROLES_KEY)) {
            sai.addRole((String) roleObject);
        }

        for (Object permissionObject : principals.fromRealm(PERMISSIONS_KEY)) {
            sai.addStringPermission((String) permissionObject);
        }

        return sai;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ScholagestUsernameToken || token instanceof ScholagestTokenToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token instanceof ScholagestUsernameToken) {
            return checkUsernameToken((ScholagestUsernameToken) token);
        } else if (token instanceof ScholagestTokenToken) {
            return checkToken((ScholagestTokenToken) token);
        }

        throw new AuthenticationException("Invalid informations");
    }

    private AuthenticationInfo checkUsernameToken(ScholagestUsernameToken token) {
        try {
            return getAndCheckUserFromDb(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private AuthenticationInfo getAndCheckUserFromDb(ScholagestUsernameToken token) throws Exception {
        UserObject userObject = null;

        if (isValidUsernamePassword(userObject, new String(token.getPassword()))) {
            return createAuthenticationInfo(token.getRequestId(), token.getTransaction(), userObject);
        }

        return null;
    }

    private boolean isValidUsernamePassword(UserObject userObject, String password) {
        return userObject != null && userObject.getPassword().equals(password);
    }

    private AuthenticationInfo checkToken(ScholagestTokenToken token) {
        try {
            return getAndCheckTokenFromDb(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private AuthenticationInfo getAndCheckTokenFromDb(ScholagestTokenToken token) throws Exception {
        String requestId = token.getRequestId();
        ITransaction transaction = token.getTransaction();

        TokenObject tokenObject = userManager.getToken(requestId, transaction, token.getToken());

        if (isTokenValid(tokenObject)) {
            UserObject userObject = userManager.getUser(requestId, transaction, tokenObject.getUserObjectKey());
            return createAuthenticationInfo(requestId, transaction, userObject);
        }

        return null;
    }

    private boolean isTokenValid(TokenObject tokenObject) {
        return tokenObject != null && tokenObject.getEndValidityTime().isBeforeNow();
    }

    private AuthenticationInfo createAuthenticationInfo(String requestId, ITransaction transaction, UserObject userObject) throws Exception {
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo();

        SimplePrincipalCollection principals = readRolesAndPermissions(requestId, transaction, userObject);
        principals.add(transaction, TRANSACTION_KEY);
        principals.add(requestId, REQUEST_ID_KEY);

        authenticationInfo.setPrincipals(principals);

        return authenticationInfo;
    }

    private SimplePrincipalCollection readRolesAndPermissions(String requestId, ITransaction transaction, UserObject userObject)
            throws DatabaseException {
        SimplePrincipalCollection principals = new SimplePrincipalCollection();

        DBSet roles = userObject.getRoles();
        for (String role : roles.values()) {
            principals.add(role, ROLES_KEY);
        }

        DBSet permissions = userObject.getPermissions();
        for (String permission : permissions.values()) {
            principals.add(permission, PERMISSIONS_KEY);
        }

        return principals;
    }
}
