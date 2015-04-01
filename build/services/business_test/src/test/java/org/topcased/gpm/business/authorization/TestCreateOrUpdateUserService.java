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
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.authorization.service.PasswordEncoding;
import org.topcased.gpm.business.authorization.service.UserAttributesData;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;

/**
 * TestCreateOrUpdateUserService tests the method
 * <code>createOrUpdateUser<code> of the Authorization Service.
 * 
 * @author srene
 */
public class TestCreateOrUpdateUserService extends
        AbstractBusinessServiceTestCase {

    /** The user login. */
    private final static String USER_LOGIN = "Jean-Pierre";

    /** The user Name. */
    private final static String USER_NAME = "Jean-Pierre";

    /** The updated user Name. */
    private final static String USER_NAME_UPDATED = "jp";

    /** The user mail address. */
    private final static String USER_MAIL = "Jean-Pierre.Dupont@happymouse.com";

    /** The updated user mail address. */
    private final static String USER_MAIL_UPDATED = "jp.Dupont@happymouse.fr";

    /** The user ForName. */
    private final static String USER_FORNAME = "Jean-Pierre";

    /** The updated user ForName. */
    private final static String USER_FORNAME_UPDATED = "jp";

    /** The user pwd. */
    private final static String USER_PWD = "pass";

    /** The updated user pwd. */
    private final static String USER_PWD_UPDATED = "@pass1";

    /** The user attributes. */
    private final static String[][] USER_ATTRIBUTES =
            { { "eyes", "blue" }, { "nose", "small" } };

    /**
     * The user attributes.
     * <P>
     * Note: These attributes must be sorted in alphabetic order, as the
     * attributes API returns attributes sorted alphabetically.
     */
    private final static String[][] USER_ATTRIBUTES_UPDATED =
            { { "eyes", GpmTestValues.CATEGORY_COLOR_VALUE_GREEN },
             { "hair", "dark" }, { "nose", "small" }, };

    /**
     * testNormalCase
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

        // checks if the user doesn't exist
        EndUserData lUser;
        try {
            lUser = authorizationService.getUserData(USER_LOGIN);
            assertNull(
                    "The user already exisits : can't test the user creation",
                    lUser);
        }
        // exception thrown when user is not in DB.
        catch (InvalidNameException lINE) {
        }

        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        authorizationService.createOrUpdateUser(adminRoleToken, lUserData,
                USER_PWD, lContext);

        // Retrieving the user
        lUser = null;
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

        // update the user

        UserAttributesData[] lUpdatedUserAttributesData =
                {
                 new UserAttributesData(USER_ATTRIBUTES_UPDATED[0][0],
                         USER_ATTRIBUTES_UPDATED[0][1]),
                 new UserAttributesData(USER_ATTRIBUTES_UPDATED[1][0],
                         USER_ATTRIBUTES_UPDATED[1][1]),
                 new UserAttributesData(USER_ATTRIBUTES_UPDATED[2][0],
                         USER_ATTRIBUTES_UPDATED[2][1]) };

        lUser.setName(USER_NAME_UPDATED);
        lUser.setMailAddr(USER_MAIL_UPDATED);
        lUser.setForname(USER_FORNAME_UPDATED);
        lUser.setUserAttributes(lUpdatedUserAttributesData);

        authorizationService.createOrUpdateUser(adminRoleToken, lUser,
                USER_PWD_UPDATED, lContext);

        // Retrieving the user
        EndUserData lUpdatedUser;
        try {
            lUpdatedUser = authorizationService.getUserData(USER_LOGIN);
        }
        // exception thrown when user is not in DB.
        catch (InvalidNameException lINE) {
            lUpdatedUser = null;
        }

        assertNotNull("User with login " + USER_LOGIN + " is not in DB.",
                lUpdatedUser);

        assertEquals("User with login " + USER_LOGIN
                + " has not the correct login in DB.", USER_LOGIN,
                lUpdatedUser.getLogin());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct name in DB.", USER_NAME_UPDATED,
                lUpdatedUser.getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct eMail in DB.", USER_MAIL_UPDATED,
                lUpdatedUser.getMailAddr());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct forname in DB.", USER_FORNAME_UPDATED,
                lUpdatedUser.getForname());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute name in DB.",
                USER_ATTRIBUTES_UPDATED[0][0],
                (lUpdatedUser.getUserAttributes()[0]).getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute value in DB.",
                USER_ATTRIBUTES_UPDATED[0][1],
                (lUpdatedUser.getUserAttributes()[0]).getValue());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute name in DB.",
                USER_ATTRIBUTES_UPDATED[1][0],
                (lUpdatedUser.getUserAttributes()[1]).getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute value in DB.",
                USER_ATTRIBUTES_UPDATED[1][1],
                (lUpdatedUser.getUserAttributes()[1]).getValue());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute name in DB.",
                USER_ATTRIBUTES_UPDATED[2][0],
                (lUpdatedUser.getUserAttributes()[2]).getName());
        assertEquals("User with login " + USER_LOGIN
                + " has not the correct user attribute value in DB.",
                USER_ATTRIBUTES_UPDATED[2][1],
                (lUpdatedUser.getUserAttributes()[2]).getValue());

    }
}
