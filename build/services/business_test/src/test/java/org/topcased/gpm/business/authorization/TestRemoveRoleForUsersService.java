/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.common.accesscontrol.Roles;

/**
 * TestRemoveRoleForUsersService
 */
public class TestRemoveRoleForUsersService extends
        AbstractBusinessServiceTestCase {

    private static final String PRODUCT_ADMIN_PRODUCT_NAME =
            GpmTestValues.PRODUCT1_NAME;

    private static final String PRODUCT_ADMIN_LOGIN = GpmTestValues.USER_ADMIN3;

    private static final String PRODUCT_ADMIN_PWD = "pwd3";

    private static final String PRODUCT_ADMIN_ROLE_NAME =
            GpmTestValues.USER_ADMIN;

    private static final String ROLE_NAME = "viewer";

    private static final String[] LOGINS_BEFORE_REMOVAL =
            new String[] { GpmTestValues.USER_USER6,
                          GpmTestValues.USER_VIEWER1,
                          GpmTestValues.USER_VIEWER3 };

    private static final String[] LOGINS_TO_REMOVE =
            new String[] { GpmTestValues.USER_USER6, GpmTestValues.USER_VIEWER1 };

    private static final String[] LOGINS_AFTER_REMOVAL =
            new String[] { GpmTestValues.USER_VIEWER3 };

    /**
     * Test the method in normal conditions : A user admin on a product affects
     * a new role on users on this product.
     */
    public void testNormalCase() {
        authorizationService = serviceLocator.getAuthorizationService();
        String lUserToken =
                authorizationService.login(PRODUCT_ADMIN_LOGIN,
                        PRODUCT_ADMIN_PWD);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        PRODUCT_ADMIN_ROLE_NAME, PRODUCT_ADMIN_PRODUCT_NAME,
                        getProcessName());

        List<String> lLogins =
                authorizationService.getUsersWithRole(getProcessName(),
                        ROLE_NAME, new String[] { PRODUCT_ADMIN_PRODUCT_NAME },
                        Roles.PRODUCT_ROLE | Roles.ROLE_ON_ONE_PRODUCT);

        assertNotNull("Unexpected DB content", lLogins);
        assertEquals("Unexpected DB content", LOGINS_BEFORE_REMOVAL.length,
                lLogins.size());
        assertTrue(
                "Unexpected DB content",
                lLogins.containsAll((List<String>) Arrays.asList(LOGINS_BEFORE_REMOVAL)));

        //Call tested method
        authorizationService.removeRoleForUsers(lRoleToken, LOGINS_TO_REMOVE,
                ROLE_NAME, PRODUCT_ADMIN_PRODUCT_NAME, getProcessName());

        // Check that this role is not present anymore for all specified users

        lLogins =
                authorizationService.getUsersWithRole(getProcessName(),
                        ROLE_NAME, new String[] { PRODUCT_ADMIN_PRODUCT_NAME },
                        Roles.PRODUCT_ROLE | Roles.ROLE_ON_ONE_PRODUCT);
        assertNotNull("Some roles have not been correctly removed.", lLogins);
        assertEquals("Some roles have not been correctly removed.",
                LOGINS_AFTER_REMOVAL.length, lLogins.size());
        assertTrue(
                "Some roles have not been correctly removed.",
                lLogins.containsAll((List<String>) Arrays.asList(LOGINS_AFTER_REMOVAL)));
    }
}
