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
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.authorization.service.PasswordEncoding;
import org.topcased.gpm.business.authorization.service.UserAttributesData;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;

/**
 * Tests set and get methods for User attributes
 * 
 * @author srene
 */
public class TestUserAttributesService extends AbstractBusinessServiceTestCase {

    /** The Authorization Service. */
    private AuthorizationService authorizationService;

    /** The user login. */
    private final static String USER_LOGIN = "AlbertD";

    /** The user Name. */
    private final static String USER_NAME = "Albert";

    /** The user mail adress. */
    private final static String USER_MAIL = "Albert.Dupont@happymouse.com";

    /** The user ForName. */
    private final static String USER_FORNAME = "Albert";

    /** The user pwd. */
    private final static String USER_PWD = "pass";

    /** The user attributes. */
    private final static String[][] USER_ATTRIBUTES =
            { { "eyes", "blue" }, { "nose", "small" }, { "hair", "dark" },
             { "hair", "blond" } };

    /**
     * Tests the getValueOfUserAttribute method.
     */
    public void testGetValueOfUserAttribute() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

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

        String lResult =
                authorizationService.getValueOfUserAttribute(lUser,
                        USER_ATTRIBUTES[0][0]);

        assertEquals("Bad returned value for user attribute : "
                + USER_ATTRIBUTES[0][0], lResult, USER_ATTRIBUTES[0][1]);
    }

    /**
     * Tests the getValueOfUserAttribute method.
     */
    public void testNullUserAttributes() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Create the user
        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(USER_LOGIN);
        lUserData.setName(USER_NAME);
        lUserData.setMailAddr(USER_MAIL);
        lUserData.setForname(USER_FORNAME);

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

        String lResult =
                authorizationService.getValueOfUserAttribute(lUser,
                        USER_ATTRIBUTES[0][0]);

        assertNull("UserAttributes aren't null", lResult);
    }

    /**
     * Tests the getValueOfUserAttribute method.
     */
    public void testNullUser() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        String lResult =
                authorizationService.getValueOfUserAttribute(null,
                        USER_ATTRIBUTES[0][0]);

        assertNull("UserAttributes aren't null", lResult);
    }

    /**
     * Tests the getValueOfUserAttribute method.
     */
    public void testNullAttributeName() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Create the user
        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(USER_LOGIN);
        lUserData.setName(USER_NAME);
        lUserData.setMailAddr(USER_MAIL);
        lUserData.setForname(USER_FORNAME);

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

        String lResult =
                authorizationService.getValueOfUserAttribute(lUser, null);

        assertNull("UserAttributes aren't null", lResult);
    }

    /**
     * Tests the addUserAttribute method.
     */
    public void testAddUserAttribute() {
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

        authorizationService.createUser(adminRoleToken, lUserData, USER_PWD,
                lContext);
        authorizationService.setValueToUserAttribute(lUserData,
                USER_ATTRIBUTES[2][0], USER_ATTRIBUTES[2][1]);

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
                + " has not the correct user attribute name in DB.",
                USER_ATTRIBUTES[0][0], (lUser.getUserAttributes()[0]).getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute value in DB.",
                USER_ATTRIBUTES[0][1],
                (lUser.getUserAttributes()[0]).getValue());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute name in DB.",
                USER_ATTRIBUTES[2][0], (lUser.getUserAttributes()[1]).getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute value in DB.",
                USER_ATTRIBUTES[2][1],
                (lUser.getUserAttributes()[1]).getValue());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute name in DB.",
                USER_ATTRIBUTES[1][0], (lUser.getUserAttributes()[2]).getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute value in DB.",
                USER_ATTRIBUTES[1][1],
                (lUser.getUserAttributes()[2]).getValue());
    }

    /**
     * Tests the addUserAttribute method.
     */
    public void testAddUserAttributeWithNullUserAttributesData() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Create the user
        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(USER_LOGIN);
        lUserData.setName(USER_NAME);
        lUserData.setMailAddr(USER_MAIL);
        lUserData.setForname(USER_FORNAME);

        // Create a context with a password encoding.
        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        authorizationService.createUser(adminRoleToken, lUserData, USER_PWD,
                lContext);
        authorizationService.setValueToUserAttribute(lUserData,
                USER_ATTRIBUTES[2][0], USER_ATTRIBUTES[2][1]);

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
                + " has not the correct user attribute name in DB.",
                USER_ATTRIBUTES[2][0], (lUser.getUserAttributes()[0]).getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute value in DB.",
                USER_ATTRIBUTES[2][1],
                (lUser.getUserAttributes()[0]).getValue());
    }
}