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

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.common.accesscontrol.Roles;

/**
 * Tests the method <CODE>getUsersWithRole<CODE> of the Authorization Service.
 * 
 * @author ahaugomm
 */
public class TestGetUsersWithRoleService extends
        AbstractBusinessServiceTestCase {

    /** The Authorization Service. */
    private AuthorizationService authorizationService;

    /** The role name. */
    private static final String ROLE_NAME = "viewer";

    /** The role name. */
    private static final String UNUSED_ROLE_NAME = "unused";

    /** The expected user logins for role. */
    private static final String[] USERS1 = { GpmTestValues.USER_USER5 };

    private static final String[] USERS2 =
            { GpmTestValues.USER_USER1, GpmTestValues.USER_USER5 };

    private static final String PRODUCT_NAME =
            GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;

    private static final String[] PRODUCT_NAMES =
            { GpmTestValues.PRODUCT1_NAME, GpmTestValues.PRODUCT_PRODUCT2 };

    /**
     * Users with role ROLE_NAME on at least one product (from PRODUCT_NAMES)
     * and not on instance
     */
    private static final String[] USERS_3 =
            { GpmTestValues.USER_USER6, GpmTestValues.USER_VIEWER1,
             GpmTestValues.USER_VIEWER2, GpmTestValues.USER_VIEWER3 };

    /**
     * Users with role ROLE_NAME on at least one product (from PRODUCT_NAMES) or
     * on instance
     */
    private static final String[] USERS_4 =
            { GpmTestValues.USER_USER5, GpmTestValues.USER_USER6,
             GpmTestValues.USER_VIEWER1, GpmTestValues.USER_VIEWER2,
             GpmTestValues.USER_VIEWER3, GpmTestValues.USER_VIEWER4 };

    /**
     * Users with role ROLE_NAME on each product (from PRODUCT_NAMES) or on
     * instance
     */
    private static final String[] USERS_5 =
            { GpmTestValues.USER_USER5, GpmTestValues.USER_VIEWER3,
             GpmTestValues.USER_VIEWER4 };

    /**
     * Users with role ROLE_NAME on each product (from PRODUCT_NAMES) and not on
     * instance
     */
    private static final String[] USERS_6 = { GpmTestValues.USER_VIEWER3 };

    /**
     * Tests the getUsers method.
     */
    public void testNormalCaseWithoutProduct() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the role names
        List<String> lUsers =
                authorizationService.getUsersWithRole(getProcessName(),
                        ROLE_NAME, null);
        assertNotNull("No users have been found with role " + ROLE_NAME, lUsers);
        for (String lUserLogin : USERS1) {
            assertTrue("User " + lUserLogin + " does not have role "
                    + ROLE_NAME + ".", lUsers.contains(lUserLogin));
        }
    }

    /**
     * Tests the getUsers method.
     */
    public void testNormalCaseWithProduct() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the role names
        List<String> lUsers =
                authorizationService.getUsersWithRole(getProcessName(),
                        ROLE_NAME, PRODUCT_NAME);
        assertNotNull("No users have been found with role " + ROLE_NAME, lUsers);
        for (String lUserLogin : USERS2) {
            assertTrue("User " + lUserLogin + " does not have role "
                    + ROLE_NAME + ".", lUsers.contains(lUserLogin));
        }
    }

    /**
     * Tests the getUsers method when no user is defined with this role.
     */
    public void testNoUsersWithThisRole() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the role names
        List<String> lUsers =
                authorizationService.getUsersWithRole(getProcessName(),
                        UNUSED_ROLE_NAME, null);
        assertTrue(
                "Users have been found with unused role " + UNUSED_ROLE_NAME,
                lUsers != null && lUsers.isEmpty());
    }

    /**
     * Tests the method with an invalid role name.
     */
    public void testInvalidRoleNameCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        try {
            authorizationService.getUsersWithRole(getProcessName(), "",
                    PRODUCT_NAME);
            fail("The exception has not been thrown.");
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }

    /**
     * Get all users with role ROLE_NAME on at least one product from
     * PRODUCT_NAMES, but not instanceRole Result should be USERS_3
     */
    public void testRoleOnOneProductAndNotInstance() {
        authorizationService = serviceLocator.getAuthorizationService();

        List<String> lUserLogins =
                authorizationService.getUsersWithRole(getProcessName(),
                        ROLE_NAME, PRODUCT_NAMES, Roles.PRODUCT_ROLE
                                | Roles.ROLE_ON_ONE_PRODUCT);

        for (String lUser : USERS_3) {
            assertTrue("User '" + lUser + "' is not contained in the result",
                    lUserLogins.contains(lUser));
        }
    }

    /**
     * Get all users with role ROLE_NAME on at least one product from
     * PRODUCT_NAMES, or as instanceRole. Result should be USERS_4
     */
    public void testRoleOnOneProductOrInstance() {
        authorizationService = serviceLocator.getAuthorizationService();

        List<String> lUserLogins =
                authorizationService.getUsersWithRole(getProcessName(),
                        ROLE_NAME, PRODUCT_NAMES, Roles.INSTANCE_ROLE
                                | Roles.PRODUCT_ROLE
                                | Roles.ROLE_ON_ONE_PRODUCT);

        for (String lUser : USERS_4) {
            assertTrue("User '" + lUser + "' is not contained in the result",
                    lUserLogins.contains(lUser));
        }
    }

    /**
     * Get all users with role ROLE_NAME on each product from PRODUCT_NAMES, but
     * not instanceRole. Result should be USERS_6
     */
    public void testRoleOnEachProductAndNotInstance() {
        authorizationService = serviceLocator.getAuthorizationService();

        List<String> lUserLogins =
                authorizationService.getUsersWithRole(getProcessName(),
                        ROLE_NAME, PRODUCT_NAMES, Roles.PRODUCT_ROLE
                                | Roles.ROLE_ON_ALL_PRODUCTS);

        for (String lUser : USERS_6) {
            assertTrue("User '" + lUser + "' is not contained in the result",
                    lUserLogins.contains(lUser));
        }
    }

    /**
     * Get all users with role ROLE_NAME on each product from PRODUCT_NAMES, or
     * as instanceRole. Result should be USERS_5
     */
    public void testRoleOnEachProductOrInstance() {
        authorizationService = serviceLocator.getAuthorizationService();

        List<String> lUserLogins =
                authorizationService.getUsersWithRole(getProcessName(),
                        ROLE_NAME, PRODUCT_NAMES, Roles.INSTANCE_ROLE
                                | Roles.PRODUCT_ROLE
                                | Roles.ROLE_ON_ALL_PRODUCTS);

        for (String lUser : USERS_5) {
            assertTrue("User '" + lUser + "' is not contained in the result",
                    lUserLogins.contains(lUser));
        }
    }
}