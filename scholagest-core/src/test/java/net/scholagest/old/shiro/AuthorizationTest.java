package net.scholagest.old.shiro;

import java.util.UUID;

import net.scholagest.old.database.Database;
import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.database.DefaultDatabaseConfiguration;
import net.scholagest.old.database.ITransaction;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

public class AuthorizationTest {
    private static final String KOKO_TOTO_VIEW = "koko:toto:view";
    private static final String KOTA = "kota";
    private static final String PASSWORD = "vespa";
    private static final String USERNAME = "lonestarr";
    private static final String USERNAME_KEY = "username";
    private static final String P_USER_ROLES = "pUserRoles";
    private static final String P_USER_PERMISSIONS = "pUserPermissions";
    private static final String ROLES_KEY = "roles";
    private static final String PERMISSIONS_KEY = "permissions";

    public static void main(String[] args) {
        new AuthorizationTest().test();
    }

    public void test() {
        ITransaction transaction = new Database(new DefaultDatabaseConfiguration()).getTransaction("credentials");
        Realm myRealm = new MyAuthRealm(transaction);
        org.apache.shiro.mgt.SecurityManager securityManager = new DefaultSecurityManager(myRealm);
        SecurityUtils.setSecurityManager(securityManager);

        for (Realm realm : ((RealmSecurityManager) SecurityUtils.getSecurityManager()).getRealms()) {
            System.out.println(realm.getName());
        }

        UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, PASSWORD);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);

        checkPermissionsAndRole(subject);

        addPermissionsAndRole(transaction);

        checkPermissionsAndRole(subject);
    }

    private void addPermissionsAndRole(ITransaction transaction) {
        try {
            String rolesNode = UUID.randomUUID().toString();
            transaction.insert(USERNAME, P_USER_ROLES, rolesNode, null);
            transaction.insert(rolesNode, KOTA, KOTA, null);

            String permissionsNode = UUID.randomUUID().toString();
            transaction.insert(USERNAME, P_USER_PERMISSIONS, permissionsNode, null);
            transaction.insert(permissionsNode, KOKO_TOTO_VIEW, KOKO_TOTO_VIEW, null);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void checkPermissionsAndRole(Subject subject) {
        if (subject.hasRole(KOTA)) {
            System.out.println("kota ");
        }
        if (subject.hasRole("kota2")) {
            System.out.println("kota2 ");
        }
        if (subject.isPermitted(KOKO_TOTO_VIEW)) {
            System.out.println("koko ");
        }
        if (subject.isPermitted("koko2:toto:view")) {
            System.out.println("koko2 ");
        }
    }

    public class MyAuthRealm extends AuthorizingRealm {
        private final ITransaction transaction;

        public MyAuthRealm(ITransaction transaction) {
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
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(USERNAME, PASSWORD.toCharArray(), getName());

            SimplePrincipalCollection principals = new SimplePrincipalCollection();
            principals.add(USERNAME, USERNAME_KEY);

            try {
                setRolesAndPermissions(principals);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }

            info.setPrincipals(principals);

            return info;
        }

        private void setRolesAndPermissions(SimplePrincipalCollection principals) throws DatabaseException {
            String roleNode = (String) transaction.get(USERNAME, P_USER_ROLES, null);
            if (roleNode != null) {
                for (String role : transaction.getColumns(roleNode)) {
                    principals.add(role, ROLES_KEY);
                }
            }

            String stringPermissionNode = (String) transaction.get(USERNAME, P_USER_PERMISSIONS, null);
            if (stringPermissionNode != null) {
                for (String stringPermission : transaction.getColumns(stringPermissionNode)) {
                    principals.add(stringPermission, PERMISSIONS_KEY);
                }
            }
        }
    }
}
