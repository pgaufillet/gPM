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

import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.RoleData;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;

/**
 * Tests the method <CODE>addRole<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestAddRoleService extends AbstractBusinessServiceTestCase {

    /** The User Login. */
    private static final String USER_LOGIN = GpmTestValues.USER_ADMIN;

    /** The Role name. */
    private static final String ROLE_NAME = "viewer";

    /** The bad Role name. */
    private static final String BAD_ROLE_NAME = "bad";

    /** The bad product name. */
    private static final String BAD_PRODUCT_NAME = "bad";

    /**
     * Tests the addRole method.
     */
    public void testNormalCase() {
        // Add role for admin
        authorizationService.addRole(adminRoleToken, USER_LOGIN,
                getProcessName(), ROLE_NAME);

        // Verify
        Collection<RoleData> lRoles =
                authorizationService.getRoles(USER_LOGIN, getProcessName());
        assertNotNull("Method getRoles returns null.", lRoles);
        assertFalse("Method getRoles returns a empty list.", lRoles.isEmpty());
        boolean lRoleAdded = false;
        for (RoleData lRoleData : lRoles) {
            lRoleAdded = lRoleData.getRoleName().equals(ROLE_NAME);
            if (lRoleAdded) {
                break;
            }
        }
        assertTrue("Role named " + ROLE_NAME
                + " has not been adding to the user " + USER_LOGIN + " in DB.",
                lRoleAdded);
    }

    /**
     * Tests the addRole method with a product name.
     */
    public void testRoleForProductCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Add role for admin
        authorizationService.addRole(adminRoleToken, USER_LOGIN,
                getProcessName(), ROLE_NAME, getProductName());

        // Verify
        Collection<String> lRoleNames =
                authorizationService.getRolesNames(adminUserToken,
                        getProcessName(), getProductName());
        assertNotNull("Method getRolesNames returns null.", lRoleNames);
        assertFalse("Method getRolesNames returns a empty list.",
                lRoleNames.isEmpty());
        assertTrue("Role named " + ROLE_NAME
                + " has not been adding to the user " + USER_LOGIN + " in DB.",
                lRoleNames.contains(ROLE_NAME));
    }

    /**
     * Tests the addRole method with a bad role name.
     */
    public void testBadRoleNameCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Add role for admin
        try {
            authorizationService.addRole(adminRoleToken, USER_LOGIN,
                    getProcessName(), BAD_ROLE_NAME);
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
     * Tests the addRole method with a bad role name.
     */
    public void testBadRoleNameCaseWithProductName() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Add role for admin
        try {
            authorizationService.addRole(USER_LOGIN, getProcessName(),
                    BAD_ROLE_NAME, getProductName());
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
     * Tests the addRole method with a bad role name.
     */
    public void testBadRoleNameWithProductNameAndRoleToken() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Add role for admin
        try {
            authorizationService.addRole(adminRoleToken, USER_LOGIN,
                    getProcessName(), BAD_ROLE_NAME, getProductName());
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
     * Tests the addRole method with a bad product name.
     */
    public void testBadProductNameCaseWithProductName() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Add role for admin
        try {
            authorizationService.addRole(USER_LOGIN, getProcessName(),
                    ROLE_NAME, BAD_PRODUCT_NAME);
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
     * Tests the addRole method with a bad product name.
     */
    public void testBadProductNameCaseWithProductNameAndRoleToken() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Add role for admin
        try {
            authorizationService.addRole(adminRoleToken, USER_LOGIN,
                    getProcessName(), ROLE_NAME, BAD_PRODUCT_NAME);
            fail("The exception has not been thrown.");
        }
        catch (InvalidNameException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a InvalidNameException.");
        }
    }
}