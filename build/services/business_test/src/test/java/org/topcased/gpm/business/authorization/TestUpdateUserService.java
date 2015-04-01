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

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.authorization.service.PasswordEncoding;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;

/**
 * Tests the method <CODE>updateUser<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestUpdateUserService extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private AuthorizationService authorizationService;

    /** The user login. */
    private final static String USER_LOGIN = GpmTestValues.USER_ADMIN;

    /** The bad user login. */
    private final static String BAD_USER_LOGIN = "bertrand";

    /** The user Name. */
    private final static String USER_NAME = "Albert";

    /** The user mail adress. */
    private final static String USER_MAIL = "Albert.Dupont@happymouse.com";

    /** The user pwd. */
    private final static String USER_PWD = "pass";

    /**
     * Tests the createUser method.
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieves the user
        EndUserData lUser = authorizationService.getUserData(USER_LOGIN);

        assertNotNull("User with login " + USER_LOGIN + " is not in DB.", lUser);

        // modifies the user
        lUser.setMailAddr(USER_MAIL);
        lUser.setName(USER_NAME);
        lUser.setLogin(USER_LOGIN);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        authorizationService.updateUser(adminRoleToken, lUser, USER_PWD,
                lContext);

        // Retrieving the user
        try {
            lUser = authorizationService.getUserData(USER_LOGIN);
        }
        // exception thrown when user is not in DB.
        catch (InvalidNameException lINE) {
            lUser = null;
        }

        assertNotNull("User with login " + USER_LOGIN + " is not in DB.", lUser);

        assertEquals("User with login " + USER_LOGIN
                + " has not the correct login in DB.", USER_LOGIN,
                lUser.getLogin());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct name in DB.", USER_NAME,
                lUser.getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct eMail in DB.", USER_MAIL,
                lUser.getMailAddr());
    }

    /**
     * Tests the createUser method with a bad login.
     */
    public void testBadLoginCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieves the user
        EndUserData lUser = authorizationService.getUserData(USER_LOGIN);

        assertNotNull("User with login " + USER_LOGIN + " is not in DB.", lUser);

        // modifies the user
        lUser.setLogin(BAD_USER_LOGIN);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        try {
            authorizationService.updateUser(adminRoleToken, lUser, USER_PWD,
                    lContext);
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
     * Tests the createUser with a non admin role
     */
    public void testNoAuthorizationCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, "notadmin",
                        getProductName(), getProcessName());

        EndUserData lUser = authorizationService.getUserData(USER_LOGIN);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        try {
            authorizationService.updateUser(lRoleToken, lUser, USER_PWD,
                    lContext);
            fail("An exception should have been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a AuthorizationException.");
        }
    }

    /**
     * Tests the method update user with an admin access set on instance
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

        // Retrieves the user
        EndUserData lUser = authorizationService.getUserData(USER_LOGIN);

        assertNotNull("User with login " + USER_LOGIN + " is not in DB.", lUser);

        // modifies the user
        lUser.setMailAddr(USER_MAIL);
        lUser.setName(USER_NAME);
        lUser.setLogin(USER_LOGIN);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        authorizationService.updateUser(lAdminInstanceRoleToken, lUser,
                USER_PWD, lContext);

        // Retrieving the user
        try {
            lUser = authorizationService.getUserData(USER_LOGIN);
        }
        // exception thrown when user is not in DB.
        catch (InvalidNameException lINE) {
            lUser = null;
        }

        assertNotNull("User with login " + USER_LOGIN + " is not in DB.", lUser);

        assertEquals("User with login " + USER_LOGIN
                + " has not the correct login in DB.", USER_LOGIN,
                lUser.getLogin());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct name in DB.", USER_NAME,
                lUser.getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct eMail in DB.", USER_MAIL,
                lUser.getMailAddr());
    }

    /**
     * Tests the method update user with ad admin access set on product
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

        // Retrieves the user
        EndUserData lUser = authorizationService.getUserData(USER_LOGIN);

        assertNotNull("User with login " + USER_LOGIN + " is not in DB.", lUser);

        // modifies the user
        lUser.setMailAddr(USER_MAIL);
        lUser.setName(USER_NAME);
        lUser.setLogin(USER_LOGIN);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        authorizationService.updateUser(lProductInstanceRoleToken, lUser,
                USER_PWD, lContext);

        // Retrieving the user
        try {
            lUser = authorizationService.getUserData(USER_LOGIN);
        }
        // exception thrown when user is not in DB.
        catch (InvalidNameException lINE) {
            lUser = null;
        }

        assertNotNull("User with login " + USER_LOGIN + " is not in DB.", lUser);

        assertEquals("User with login " + USER_LOGIN
                + " has not the correct login in DB.", USER_LOGIN,
                lUser.getLogin());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct name in DB.", USER_NAME,
                lUser.getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct eMail in DB.", USER_MAIL,
                lUser.getMailAddr());
    }

}