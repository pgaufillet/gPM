/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
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
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.common.accesscontrol.Roles;

/**
 * TestAddRoleForUsersService
 *
 */
/**
 * TestAddRoleForUsersService
 */
public class TestAddRoleForUsersService extends AbstractBusinessServiceTestCase {

    private static final String PRODUCT_ADMIN_PRODUCT_NAME =
            GpmTestValues.PRODUCT_PRODUCT3;

    private static final String PRODUCT_ADMIN_LOGIN = GpmTestValues.USER_ADMIN3;

    private static final String PRODUCT_ADMIN_PWD = "pwd3";

    private static final String PRODUCT_ADMIN_ROLE_NAME =
            GpmTestValues.USER_ADMIN;

    private static final String PRODUCT_ADMIN_ANTOHER_PRODUCT =
            GpmTestValues.PRODUCT_PRODUCT2;

    private static final String NOT_ADMIN_LOGIN = GpmTestValues.USER_VIEWER1;

    private static final String NOT_ADMIN_PWD = "pwd1";

    private static final String NOT_ADMIN_ROLE_NAME = "viewer";

    private static final String NOT_ADMIN_PRODUCT_NAME =
            GpmTestValues.PRODUCT1_NAME;

    private static final String ROLE_TO_SET = "viewer";

    private static final String ANOTHER_ROLE_TO_SET = "reporter";

    private static final String[] LOGINS =
            new String[] { GpmTestValues.USER_USER1, GpmTestValues.USER_USER2,
                          GpmTestValues.USER_USER3 };

    /**
     * Test when a non-administrator tries to affect a role on users on his
     * product
     */
    public void testNotAdminUser() {
        String lUserToken =
                authorizationService.login(NOT_ADMIN_LOGIN, NOT_ADMIN_PWD);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        NOT_ADMIN_ROLE_NAME, NOT_ADMIN_PRODUCT_NAME,
                        getProcessName());

        try {
            //Call tested method
            authorizationService.addRoleForUsers(lRoleToken, LOGINS,
                    ROLE_TO_SET, NOT_ADMIN_PRODUCT_NAME, getProcessName());
            fail("Role affectation by a not admin user.");
        }
        catch (AuthorizationException e) {
            // do nothing
        }
    }

    /**
     * Test when the role is to be affected on noProduct
     */
    public void testNoProductGlobalAdminUser() {

        //Call tested method
        authorizationService.addRoleForUsers(adminRoleToken, LOGINS,
                ANOTHER_ROLE_TO_SET, null, getProcessName());

        // Check that this role is present for all specified users
        List<String> lLogins =
                authorizationService.getUsersWithRole(getProcessName(),
                        ANOTHER_ROLE_TO_SET, null, Roles.PRODUCT_ROLE
                                | Roles.INSTANCE_ROLE
                                | Roles.ROLE_ON_ONE_PRODUCT);

        assertTrue("Some roles have not been set.",
                lLogins.containsAll((List<String>) Arrays.asList(LOGINS)));
    }

    /**
     * Test when the role is to be affected on noProduct
     */
    public void testNoProductNotGlobalAdminUser() {
        String lUserToken =
                authorizationService.login(PRODUCT_ADMIN_LOGIN,
                        PRODUCT_ADMIN_PWD);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        PRODUCT_ADMIN_ROLE_NAME, PRODUCT_ADMIN_PRODUCT_NAME,
                        getProcessName());
        try {
            //Call tested method
            authorizationService.addRoleForUsers(lRoleToken, LOGINS,
                    ROLE_TO_SET, null, getProcessName());

            fail("Role affectation on instance by a product admin user.");
        }
        catch (AuthorizationException e) {
            // do nothing
        }
    }

    /**
     * Test when the current user is admin of another product
     */
    public void testAdminOnAnotherProduct() {
        String lUserToken =
                authorizationService.login(PRODUCT_ADMIN_LOGIN,
                        PRODUCT_ADMIN_PWD);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        PRODUCT_ADMIN_ROLE_NAME, PRODUCT_ADMIN_PRODUCT_NAME,
                        getProcessName());
        try {
            //Call tested method
            authorizationService.addRoleForUsers(lRoleToken, LOGINS,
                    ROLE_TO_SET, PRODUCT_ADMIN_ANTOHER_PRODUCT,
                    getProcessName());
            fail("Role affectation by an admin user on the wrong product.");
        }
        catch (AuthorizationException e) {
            // do nothing
        }
    }

    /**
     * Test the method in not mal conditions : A user admin on a product affects
     * a new role on users on this product.
     */
    public void testNormalCase() {
        String lUserToken =
                authorizationService.login(PRODUCT_ADMIN_LOGIN,
                        PRODUCT_ADMIN_PWD);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        PRODUCT_ADMIN_ROLE_NAME, PRODUCT_ADMIN_PRODUCT_NAME,
                        getProcessName());

        //Call tested method
        authorizationService.addRoleForUsers(lRoleToken, LOGINS, ROLE_TO_SET,
                PRODUCT_ADMIN_PRODUCT_NAME, getProcessName());

        // Check that this role is present for all specified users

        List<String> lLogins =
                authorizationService.getUsersWithRole(getProcessName(),
                        ROLE_TO_SET,
                        new String[] { PRODUCT_ADMIN_PRODUCT_NAME },
                        Roles.PRODUCT_ROLE | Roles.ROLE_ON_ONE_PRODUCT);

        assertTrue("Some roles have not been set.",
                lLogins.containsAll((List<String>) Arrays.asList(LOGINS)));
    }
}
