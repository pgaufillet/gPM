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
import org.topcased.gpm.business.authorization.service.PasswordEncoding;
import org.topcased.gpm.business.authorization.service.UserAttributesData;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;

/**
 * Tests the method <CODE>createUser<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestCreateUserService extends AbstractBusinessServiceTestCase {

    /** The user login. */
    private final static String USER_LOGIN = "AlbertD";

    /** The already exist user login. */
    private final static String BAD_USER_LOGIN = GpmTestValues.USER_ADMIN;

    /** The user Name. */
    private final static String USER_NAME = "Albert";

    /** The user mail adress. */
    private final static String USER_MAIL = "Albert.Dupont@happymouse.com";

    /** The user ForName. */
    private final static String USER_FORNAME = "Albert";

    /** The user pwd. */
    private final static String USER_PWD = "pass";

    /** The bad user pwd. */
    private final static String BAD_USER_PWD = StringUtils.EMPTY;

    /** The user attributes. */
    private final static String[][] USER_ATTRIBUTES =
            { { "eyes", "blue" }, { "nose", "small" } };

    /** The admin login in an other case. */
    private final static String ADMIN_LOGIN_OTHER_CASE =
            GpmTestValues.USER_ADMIN;

    private static final String INSTANCE_RESOURCE_CASE_INSENSITIVE =
            "authorization/TestCaseInsensitive.xml";

    /**
     * Tests the createUser method.
     */
    public void testNormalCase() {
        // Create the user
        UserAttributesData[] lUserAttributesData =
                {
                 new UserAttributesData(USER_ATTRIBUTES[0][0],
                         USER_ATTRIBUTES[0][1]),
                 new UserAttributesData(USER_ATTRIBUTES[1][0],
                         USER_ATTRIBUTES[1][1]) };

        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(USER_LOGIN);
        lUserData.setName(USER_NAME);
        lUserData.setMailAddr(USER_MAIL);
        lUserData.setForname(USER_FORNAME);
        lUserData.setUserAttributes(lUserAttributesData);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        authorizationService.createUser(adminRoleToken, lUserData, USER_PWD,
                lContext);

        // Retrieving the user
        EndUserData lUser;
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
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct forname in DB.", USER_FORNAME,
                lUser.getForname());
    }

    /**
     * Tests the createUser method with a null login.
     */
    public void testNullLoginCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Create the user
        UserAttributesData[] lUserAttributesDataTab = new UserAttributesData[2];

        UserAttributesData lFstUserAttributeData = new UserAttributesData();
        lFstUserAttributeData.setName(USER_ATTRIBUTES[0][0]);
        lFstUserAttributeData.setValue(USER_ATTRIBUTES[0][1]);
        lUserAttributesDataTab[0] = lFstUserAttributeData;

        UserAttributesData lSndUserAttributeData = new UserAttributesData();
        lSndUserAttributeData.setName(USER_ATTRIBUTES[1][0]);
        lSndUserAttributeData.setValue(USER_ATTRIBUTES[1][1]);
        lUserAttributesDataTab[1] = lSndUserAttributeData;

        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(null);
        lUserData.setName(USER_NAME);
        lUserData.setMailAddr(USER_MAIL);
        lUserData.setForname(USER_FORNAME);
        lUserData.setUserAttributes(lUserAttributesDataTab);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        try {
            authorizationService.createUser(adminRoleToken, lUserData,
                    USER_PWD, lContext);
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
     * Tests the createUser method with a bad login.
     */
    public void testBadLoginCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Create the user
        UserAttributesData[] lUserAttributesData =
                {
                 new UserAttributesData(USER_ATTRIBUTES[0][0],
                         USER_ATTRIBUTES[0][1]),
                 new UserAttributesData(USER_ATTRIBUTES[1][0],
                         USER_ATTRIBUTES[1][1]) };

        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(BAD_USER_LOGIN);
        lUserData.setName(USER_NAME);
        lUserData.setMailAddr(USER_MAIL);
        lUserData.setForname(USER_FORNAME);
        lUserData.setUserAttributes(lUserAttributesData);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        try {
            authorizationService.createUser(adminRoleToken, lUserData,
                    USER_PWD, lContext);
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
     * Tests the createUser method with a bad login.
     */
    public void testBadPassCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Create the user
        UserAttributesData[] lUserAttributesData =
                {
                 new UserAttributesData(USER_ATTRIBUTES[0][0],
                         USER_ATTRIBUTES[0][1]),
                 new UserAttributesData(USER_ATTRIBUTES[1][0],
                         USER_ATTRIBUTES[1][1]) };

        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(USER_LOGIN);
        lUserData.setName(USER_NAME);
        lUserData.setMailAddr(USER_MAIL);
        lUserData.setForname(USER_FORNAME);
        lUserData.setUserAttributes(lUserAttributesData);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        try {
            authorizationService.createUser(adminRoleToken, lUserData,
                    BAD_USER_PWD, lContext);
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
     * Tests the create admin user case (it is not possible to create a new
     * admin user since it has already been created before)
     */
    public void testCreateAdminUserCase() {
        try {
            authorizationService.createAdminUser("");
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
     * Create an end user data structure
     * 
     * @return created end user data
     */
    protected EndUserData createEndUserData() {
        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(USER_LOGIN);
        lUserData.setName(USER_NAME);
        lUserData.setMailAddr(USER_MAIL);
        lUserData.setForname(USER_FORNAME);
        return lUserData;
    }

    /**
     * Check that an admin access set on instance can create a user
     */
    public void testCreateUserWithAdminInstanceCase() {
        // Login with Admin Instance
        String lAdminInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[1]);
        String lAdminInstanceRoleToken =
                authorizationService.selectRole(lAdminInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE, getProductName(),
                        getProcessName());

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        authorizationService.createUser(lAdminInstanceRoleToken,
                createEndUserData(), USER_PWD, lContext);

        // Retrieving the user
        EndUserData lUser;
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
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct forname in DB.", USER_FORNAME,
                lUser.getForname());
    }

    /**
     * Check that an admin access set on product cannot create a user
     */
    public void testCreateUserWithProductInstanceCase() {
        // Login with Product Instance
        String lProductInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[1]);
        String lProductInstanceRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        try {
            authorizationService.createUser(lProductInstanceRoleToken,
                    createEndUserData(), USER_PWD, lContext);
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
     * instance, no admin access on product) cannot create a user
     */
    public void testCreateUserWithNoAdminAccessCase() {
        // Login with classical role
        String lUserToken =
                authorizationService.login(GpmTestValues.NO_ADMIN_LOGIN_PWD[0],
                        GpmTestValues.NO_ADMIN_LOGIN_PWD[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        GpmTestValues.VIEWER_ROLE, getProductName(),
                        getProcessName());

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        try {
            authorizationService.createUser(lRoleToken, createEndUserData(),
                    USER_PWD, lContext);
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
     * Test if it is possible to create an other user with the login in an other
     * case but with the login case sensitive
     */
    public void testCreateUserOtherCaseWithCaseSensitive() {
        // Create user with login in an other case and with case sensitive
        authorizationService = serviceLocator.getAuthorizationService();

        assertTrue("The login is case insensitive.",
                authorizationService.isLoginCaseSensitive());

        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(ADMIN_LOGIN_OTHER_CASE);
        lUserData.setName(USER_NAME);
        lUserData.setMailAddr(USER_MAIL);
        lUserData.setForname(USER_FORNAME);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        String lUserId = null;
        try {
            lUserId =
                    authorizationService.createUser(adminRoleToken, lUserData,
                            USER_PWD, lContext);
        }
        catch (InvalidValueException e) {
            // OK
        }

        assertNull("The user is created.", lUserId);
    }

    /**
     * Test if it is possible to create an other user with the login in an other
     * case but with the login case insensitive
     */
    public void testCreateUserOtherCaseWithCaseInsensitive() {
        // launch base update
        instantiate(getProcessName(), INSTANCE_RESOURCE_CASE_INSENSITIVE);

        // Create user with login in an other case and with case insensitive
        authorizationService = serviceLocator.getAuthorizationService();

        assertFalse("The login is case sensitive.",
                authorizationService.isLoginCaseSensitive());

        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(ADMIN_LOGIN_OTHER_CASE);
        lUserData.setName(USER_NAME);
        lUserData.setMailAddr(USER_MAIL);
        lUserData.setForname(USER_FORNAME);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        String lUserId = null;
        try {
            lUserId =
                    authorizationService.createUser(adminRoleToken, lUserData,
                            USER_PWD, lContext);
        }
        catch (InvalidValueException e) {
            // OK
        }

        assertNull("The user is created.", lUserId);
    }
}
