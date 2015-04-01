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

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidValueException;

/**
 * Tests the method <CODE>removeUser<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestRemoveUserService extends AbstractBusinessServiceTestCase {

    private final static String INCORRECT_LOGIN = "";

    private static final String INSTANCE_RESOURCE_CASE_INSENSITIVE =
            "authorization/TestCaseInsensitive.xml";

    /**
     * Tests the removeUser method.
     */
    public void testNormalCase() {
        // Retrieving all the userData
        EndUserData[] lUserDatas = authorizationService.getAllUserData();

        assertNotNull(
                "Method getAllUserData returns null instead of the list of userDatas.",
                lUserDatas);

        String lLogin = lUserDatas[0].getLogin();

        // Remove The user
        authorizationService.removeUser(adminRoleToken, lLogin);

        // Verify the user is not in DB
        EndUserData lUser;
        try {
            lUser = authorizationService.getUserData(lLogin);
        }
        // exception thrown when user is not in DB.
        catch (InvalidNameException lINE) {
            lUser = null;
        }

        assertNull("The user with login " + lLogin + " has not been removed.",
                lUser);
    }

    /**
     * Tests the method removeUser with an invalid login name
     */
    public void testInvalidLoginCase() {
        try {
            authorizationService.removeUser(adminRoleToken, INCORRECT_LOGIN);
            fail("The exception has not been thrown.");
        }
        catch (InvalidValueException lEx) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an InvalidValueException.");
        }
    }

    /**
     * Check that an admin access set on instance can remove a user
     */
    public void testRemoveUserWithAdminInstanceCase() {
        // Login with Admin Instance
        String lAdminInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[1]);
        String lAdminInstanceRoleToken =
                authorizationService.selectRole(lAdminInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE, getProductName(),
                        getProcessName());

        EndUserData[] lUserDatas = authorizationService.getAllUserData();

        assertNotNull(
                "Method getAllUserData returns null instead of the list of userDatas.",
                lUserDatas);

        String lLogin = lUserDatas[0].getLogin();

        authorizationService.removeUser(lAdminInstanceRoleToken, lLogin);

        // Verify the user is not in DB
        EndUserData lUser;
        try {
            lUser = authorizationService.getUserData(lLogin);
        }
        // exception thrown when user is not in DB.
        catch (InvalidNameException lINE) {
            lUser = null;
        }

        assertNull("The user with login " + lLogin + " has not been removed.",
                lUser);
    }

    /**
     * Check that an admin access set on product cannot remove a user
     */
    public void testRemoveUserWithProductInstanceCase() {
        // Login with Product Instance
        String lProductInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[1]);
        String lProductInstanceRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        EndUserData[] lUserDatas = authorizationService.getAllUserData();
        String lLogin = lUserDatas[0].getLogin();

        try {
            authorizationService.removeUser(lProductInstanceRoleToken, lLogin);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }

    /**
     * Check that a classical role (no global admin, no admin access on
     * instance, no admin access on product) cannot remove a user
     */
    public void testRemoveUserWithNoAdminAccessCase() {
        // Login with classical role
        String lUserToken =
                authorizationService.login(GpmTestValues.NO_ADMIN_LOGIN_PWD[0],
                        GpmTestValues.NO_ADMIN_LOGIN_PWD[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        GpmTestValues.VIEWER_ROLE, getProductName(),
                        getProcessName());

        EndUserData[] lUserDatas = authorizationService.getAllUserData();
        String lLogin = lUserDatas[0].getLogin();

        try {
            authorizationService.removeUser(lRoleToken, lLogin);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }

    /**
     * Test if it is possible to remove user with the login in an other case but
     * with the login case sensitive
     */
    public void testRemoveUserCaseSensitive() {
        authorizationService = serviceLocator.getAuthorizationService();

        assertTrue("The login is case insensitive.",
                authorizationService.isLoginCaseSensitive());

        EndUserData[] lUserDatas = authorizationService.getAllUserData();

        assertNotNull(
                "Method getAllUserData returns null instead of the list of userDatas.",
                lUserDatas);

        String lLogin = lUserDatas[0].getLogin();
        String lUpperLogin = StringUtils.upperCase(lLogin);

        try {
            authorizationService.removeUser(adminRoleToken, lUpperLogin);
            fail("The user is deleted.");
        }
        catch (InvalidValueException e) {
            //OK
        }

        EndUserData lUser = authorizationService.getUserData(lLogin);
        assertNotNull("The user is deleted.", lUser);
    }

    /**
     * Test if it is possible to remove user with the login in an other case but
     * with the login case insensitive
     */
    public void testRemoveUserCaseInsensitive() {
        // launch base update
        instantiate(getProcessName(), INSTANCE_RESOURCE_CASE_INSENSITIVE);

        authorizationService = serviceLocator.getAuthorizationService();

        assertFalse("The login is case sensitive.",
                authorizationService.isLoginCaseSensitive());

        EndUserData[] lUserDatas = authorizationService.getAllUserData();

        assertNotNull(
                "Method getAllUserData returns null instead of the list of userDatas.",
                lUserDatas);

        String lLogin = lUserDatas[0].getLogin();
        String lUpperLogin = StringUtils.upperCase(lLogin);

        authorizationService.removeUser(adminRoleToken, lUpperLogin);

        EndUserData lUser = authorizationService.getUserData(lLogin);
        assertNull("The user is deleted.", lUser);
    }
}
