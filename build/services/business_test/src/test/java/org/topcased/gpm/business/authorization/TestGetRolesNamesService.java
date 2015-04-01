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

import java.util.Arrays;
import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.RoleProperties;

/**
 * Tests the method <CODE>getRoleNames<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestGetRolesNamesService extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private AuthorizationService authorizationService;

    /** The expected roles names. (this list must be sorted alphabetically) */
    private static final String[] ROLE_NAMES =
            { GpmTestValues.USER_ADMIN, "normal", "notadmin", "reporter",
             "restricted", "unused", "viewer", "administrator" };

    /** The expected roles names for products. */
    private static final String[] ROLE_NAMES_FOR_PRODUCTS =
            { GpmTestValues.USER_ADMIN };

    /**
     * Tests the getRoleNames method.
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the role names
        Collection<String> lRoleNames =
                authorizationService.getRolesNames(getProcessName());

        assertNotNull("Method getRolesNames returns null.", lRoleNames);
        int lSize = lRoleNames.size();
        int lExpectedSize = ROLE_NAMES.length;
        assertEquals("Method getRolesNames returns " + lSize
                + " role names instead of " + lExpectedSize, lExpectedSize,
                lSize);

        assertTrue("Roles Names returned are not thoses expected. ",
                lRoleNames.containsAll(Arrays.asList(ROLE_NAMES)));
    }

    /**
     * Tests the getRoleNames method with the product name.
     */
    public void testNormalCaseWithProductName() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the role names
        Collection<String> lRoleNames =
                authorizationService.getRolesNames(adminUserToken,
                        getProcessName(), getProductName());

        assertNotNull("Method getRolesNames returns null.", lRoleNames);
        int lSize = lRoleNames.size();
        int lExpectedSize = ROLE_NAMES_FOR_PRODUCTS.length;

        assertEquals("Method getRolesNames returns " + lSize
                + " role names instead of " + lExpectedSize, lExpectedSize,
                lSize);

        assertTrue("Roles Names returned are not thoses expected. ",
                lRoleNames.containsAll(Arrays.asList(ROLE_NAMES_FOR_PRODUCTS)));
    }

    private static String[] USER =
            new String[] { GpmTestValues.USER_USER1, "pwd1" };

    private static final String PRODUCT_NAME =
            GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;

    private static String[] ROLES_NAMES_ON_PRODUCT_ONLY =
            new String[] { "viewer" };

    private static String[] ROLES_NAMES_ON_INSTANCE_ONLY =
            new String[] { GpmTestValues.USER_ADMIN };

    private static String[] ROLES_NAMES_ON_PRODUCT_OR_INSTANCE =
            new String[] { "viewer", GpmTestValues.USER_ADMIN };

    public void testGetRolesNamesOnProductOnly() {
        authorizationService = serviceLocator.getAuthorizationService();
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        // Get roles available on specified product
        Collection<String> lRoles =
                authorizationService.getRolesNames(lUserToken,
                        getProcessName(), PRODUCT_NAME,
                        RoleProperties.PRODUCT_ROLE_ONLY);
        assertNotNull("No roles found", lRoles);
        assertEquals("Invalid number of roles returned",
                ROLES_NAMES_ON_PRODUCT_ONLY.length, lRoles.size());
        assertTrue(lRoles.containsAll(Arrays.asList(ROLES_NAMES_ON_PRODUCT_ONLY)));
    }

    public void testGetRolesNamesOnInstanceOnly() {
        authorizationService = serviceLocator.getAuthorizationService();
        String lUserToken = authorizationService.login(USER[0], USER[1]);

        // Get roles available on instance
        Collection<String> lRoles =
                authorizationService.getRolesNames(lUserToken,
                        getProcessName(), PRODUCT_NAME,
                        RoleProperties.INSTANCE_ROLE_ONLY);
        assertNotNull("No roles found", lRoles);
        assertEquals("Invalid number of roles returned",
                ROLES_NAMES_ON_INSTANCE_ONLY.length, lRoles.size());
        assertTrue(lRoles.containsAll(Arrays.asList(ROLES_NAMES_ON_INSTANCE_ONLY)));
    }

    public void testGetRolesNamesOnInstanceOrProduct() {
        authorizationService = serviceLocator.getAuthorizationService();
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        // Get roles available on instance or specified product
        Collection<String> lRoles =
                authorizationService.getRolesNames(lUserToken,
                        getProcessName(), PRODUCT_NAME,
                        RoleProperties.PRODUCT_OR_INSTANCE_ROLE);
        assertNotNull("No roles found", lRoles);
        assertEquals("Invalid number of roles returned",
                ROLES_NAMES_ON_PRODUCT_OR_INSTANCE.length, lRoles.size());
        assertTrue(lRoles.containsAll(Arrays.asList(ROLES_NAMES_ON_PRODUCT_OR_INSTANCE)));
    }
}