package net.scholagest.shiro;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class RealmAuthenticationAndAuthorization extends AuthorizingRealm {
    private static final String ROLES_KEY = "roles";
    private static final String PERMISSIONS_KEY = "permissions";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    private final ITransaction transaction;

    public RealmAuthenticationAndAuthorization(ITransaction transaction) {
        this.transaction = transaction;
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
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token.getCredentials();
        String username = usernamePasswordToken.getUsername();
        char[] password = usernamePasswordToken.getPassword();

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, getName());

        SimplePrincipalCollection principals = new SimplePrincipalCollection();
        principals.add(username, USERNAME_KEY);
        principals.add(new String(password), PASSWORD_KEY);

        try {
            setRolesAndPermissions(principals, username);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        info.setPrincipals(principals);

        return info;
    }

    private void setRolesAndPermissions(SimplePrincipalCollection principals, String username) throws DatabaseException {
        String roleNode = (String) transaction.get(username, AuthorizationNamespace.pUserRoles, null);
        if (roleNode != null) {
            for (String role : transaction.getColumns(roleNode)) {
                principals.add(role, ROLES_KEY);
            }
        }

        String stringPermissionNode = (String) transaction.get(username, AuthorizationNamespace.pUserPermissions, null);
        if (stringPermissionNode != null) {
            for (String stringPermission : transaction.getColumns(stringPermissionNode)) {
                principals.add(stringPermission, PERMISSIONS_KEY);
            }
        }
    }
}
