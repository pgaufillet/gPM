/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Sébastien René
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import java.util.ArrayList;
import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.RoleData;
import org.topcased.gpm.business.exception.AuthorizationException;

/**
 * Tests the method <CODE>setRoles<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestSetRolesService extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private AuthorizationService authorizationService;

    /** The user login. */
    private static final String USER_LOGIN = GpmTestValues.USER_ADMIN;

    /** The admin role name. */
    private static final String ADMIN_ROLE_NAME = GpmTestValues.USER_ADMIN;

    /** The user role name. */
    private static final String PRODUCT_ROLE_NAME = "viewer";

    /**
     * Test adding an user role (not admin) to a user
     */
    public void testAddNormalRoleCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the role names
        RoleData lRoleData = new RoleData(PRODUCT_ROLE_NAME, getProductName());
        Collection<RoleData> lRoles = new ArrayList<RoleData>();
        lRoles.add(lRoleData);
        authorizationService.setRoles(adminRoleToken, USER_LOGIN,
                getProcessName(), null, lRoles, null);

        // Retrieving the role names
        lRoles = authorizationService.getRoles(USER_LOGIN, getProcessName());

        assertNotNull("Method getRolesNames returns null.", lRoles);
        assertFalse("Method getRolesNames returns a empty list of role Names.",
                lRoles.isEmpty());

        assertTrue("Role not added to the user", hasRole(lRoles,
                PRODUCT_ROLE_NAME));
    }

    /**
     * Test adding an admin role to a user
     */
    public void testAddAdminRoleCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the role names
        RoleData lRoleData = new RoleData(ADMIN_ROLE_NAME, null);
        Collection<RoleData> lRoles = new ArrayList<RoleData>();
        lRoles.add(lRoleData);
        authorizationService.setRoles(adminRoleToken, USER_LOGIN,
                getProcessName(), null, lRoles, null);

        // Retrieving the role names
        lRoles = authorizationService.getRoles(USER_LOGIN, getProcessName());

        assertNotNull("Method getRolesNames returns null.", lRoles);
        assertFalse("Method getRolesNames returns a empty list of role Names.",
                lRoles.isEmpty());

        assertTrue("Admin role not added to the user", hasRole(lRoles,
                ADMIN_ROLE_NAME));
    }

    /**
     * Chain the tow previous tests: Set an admin role (only), then set a user
     * role (only), and finally check that the admin role is not present in
     * user's roles.
     */
    public void testAddMultipleRolesCase() {
        // First, set an admin role
        testAddAdminRoleCase();

        // Then, set a user role  (the admin role should not be present any more)
        testAddNormalRoleCase();

        Collection<RoleData> lRoles;
        lRoles = authorizationService.getRoles(USER_LOGIN, getProcessName());

        assertNotNull("Method getRolesNames returns null.", lRoles);
        assertFalse("Method getRolesNames returns a empty list of role Names.",
                lRoles.isEmpty());

        assertTrue("Normal role not present in user roles", hasRole(lRoles,
                PRODUCT_ROLE_NAME));
        assertFalse("Admin role still present in user roles", hasRole(lRoles,
                ADMIN_ROLE_NAME));
    }

    private boolean hasRole(Collection<RoleData> pRolesData, String pRoleName) {
        for (RoleData lRoleData : pRolesData) {
            if (lRoleData.getRoleName().equals(pRoleName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests the method setRoles with an admin access set on instance : setRoles
     * is enabled.
     */
    public void testWithAdminAccessOnInstanceCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with Admin Instance
        String lAdminInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[1]);
        String lAdminInstanceRoleToken =
                authorizationService.selectRole(lAdminInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE, getProductName(),
                        getProcessName());

        RoleData lRoleData = new RoleData(PRODUCT_ROLE_NAME, getProductName());
        Collection<RoleData> lRoles = new ArrayList<RoleData>();
        lRoles.add(lRoleData);
        authorizationService.setRoles(lAdminInstanceRoleToken, USER_LOGIN,
                getProcessName(), null, lRoles, null);

        lRoles = authorizationService.getRoles(USER_LOGIN, getProcessName());

        assertNotNull("Method getRolesNames returns null.", lRoles);
        assertFalse("Method getRolesNames returns a empty list of role Names.",
                lRoles.isEmpty());

        assertTrue("Role not added to the user", hasRole(lRoles,
                PRODUCT_ROLE_NAME));
    }

    /**
     * Tests the method setRoles with an admin access set on product : setRoles
     * is enabled only if role name is not admin and if product name is the
     * current product
     */
    public void testWithAdminAccessOnProductCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with Product Instance
        String lProductInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[1]);
        String lProductInstanceRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        // Impossible to set roles on another product
        RoleData lRoleData = new RoleData(PRODUCT_ROLE_NAME, getProductName());
        Collection<RoleData> lRoles = new ArrayList<RoleData>();
        lRoles.add(lRoleData);
        try {
            authorizationService.setRoles(lProductInstanceRoleToken,
                    USER_LOGIN, getProcessName(), null, lRoles, null);
            fail("An exception should have been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a AuthorizationException.");
        }

        // Impossible to set admin role
        lRoleData = new RoleData(ADMIN_ROLE_NAME, GpmTestValues.PRODUCT1_NAME);
        lRoles = new ArrayList<RoleData>();
        lRoles.add(lRoleData);
        try {
            authorizationService.setRoles(lProductInstanceRoleToken,
                    USER_LOGIN, getProcessName(), null, lRoles, null);
            fail("An exception should have been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a AuthorizationException.");
        }

        // Other cases are available
        lRoleData =
                new RoleData(PRODUCT_ROLE_NAME, GpmTestValues.PRODUCT1_NAME);
        lRoles = new ArrayList<RoleData>();
        lRoles.add(lRoleData);
        authorizationService.setRoles(lProductInstanceRoleToken, USER_LOGIN,
                getProcessName(), null, lRoles, null);

        assertNotNull("Method getRolesNames returns null.", lRoles);
        assertFalse("Method getRolesNames returns a empty list of role Names.",
                lRoles.isEmpty());

        assertTrue("Role not added to the user", hasRole(lRoles,
                PRODUCT_ROLE_NAME));
    }

    /**
     * Tests the method setRoles with no admin access : setRoles is not enabled.
     */
    public void testWithNoAdminAccessCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with classical role
        String lUserToken =
                authorizationService.login(GpmTestValues.NO_ADMIN_LOGIN_PWD[0],
                        GpmTestValues.NO_ADMIN_LOGIN_PWD[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        GpmTestValues.VIEWER_ROLE, getProductName(),
                        getProcessName());

        RoleData lRoleData = new RoleData(PRODUCT_ROLE_NAME, getProductName());
        Collection<RoleData> lRoles = new ArrayList<RoleData>();
        lRoles.add(lRoleData);

        try {
            authorizationService.setRoles(lRoleToken, USER_LOGIN,
                    getProcessName(), null, lRoles, null);
            fail("An exception should have been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a AuthorizationException.");
        }
    }

}