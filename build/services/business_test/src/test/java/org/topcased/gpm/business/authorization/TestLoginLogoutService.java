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

import java.rmi.server.UID;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.exception.InvalidTokenException;

/**
 * Tests the method <CODE>createRole<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestLoginLogoutService extends AbstractBusinessServiceTestCase {

    /** The user login */
    private static final String USER_LOGIN = GpmTestValues.USER_USER2;

    /** The user password */
    private static final String USER_PASSWORD = "pwd2";

    /** A not valid token */
    private static final String INVALIDTOKEN = new UID().toString();

    /** The Product Service. */
    private AuthorizationService authorizationService;

    private static final String INSTANCE_RESOURCE_CASE_INSENSITIVE =
            "authorization/TestCaseInsensitive.xml";

    /**
     * testWrongPassword
     */
    public void testWrongPassword() {
        authorizationService = serviceLocator.getAuthorizationService();
        String lUserToken = authorizationService.login(USER_LOGIN, "");
        assertNull("Login didn't failed with a wrong password", lUserToken);
    }

    /**
     * testWrongLogin
     */
    public void testWrongLogin() {
        // Login with a blank login and password 
        authorizationService = serviceLocator.getAuthorizationService();
        String lUserToken = authorizationService.login("", USER_PASSWORD);
        assertNull("Login didn't failed with a wrong login", lUserToken);
    }

    /**
     * testLogoutBlankUserToken
     */
    public void testLogoutBlankUserToken() {
        // Logout with a blank userToken
        authorizationService = serviceLocator.getAuthorizationService();
        try {
            authorizationService.logout("");
        }
        catch (InvalidTokenException lITEx) {
            // ok
        }
        catch (Exception lEx) {
            fail("InvalidTokenException was expected instead of a "
                    + lEx.getClass().getName());
        }
    }

    /**
     * testLogoutNullUserToken
     */
    public void testLogoutNullUserToken() {
        // Logout with a null userToken
        authorizationService = serviceLocator.getAuthorizationService();
        try {
            authorizationService.logout(null);
        }
        catch (InvalidTokenException lITEx) {
            // ok
        }
        catch (Exception lEx) {
            fail("InvalidTokenException was expected instead of a "
                    + lEx.getClass().getName());
        }
    }

    /**
     * testLogoutNullUserToken
     */
    public void testLogoutInvalidUserToken() {
        // Logout with an invalid userToken
        authorizationService = serviceLocator.getAuthorizationService();
        try {
            authorizationService.logout(INVALIDTOKEN);
        }
        catch (InvalidTokenException lITEx) {
            // ok
        }
        catch (Exception lEx) {
            fail("InvalidTokenException was expected instead of a "
                    + lEx.getClass().getName());
        }
    }

    /**
     * testLoginCaseSensitive
     */
    public void testLoginCaseSensitive() {
        // Login with case insensitive
        authorizationService = serviceLocator.getAuthorizationService();

        assertTrue("The login is not case sensitive.",
                authorizationService.isLoginCaseSensitive());

        String lLoginToken =
                authorizationService.login(StringUtils.upperCase(USER_LOGIN),
                        USER_PASSWORD);

        assertNull("The user can be logon with a login in an other case.",
                lLoginToken);
    }

    /**
     * testLoginCaseInsensitive
     */
    public void testLoginCaseInsensitive() {

        // launch base update
        instantiate(getProcessName(), INSTANCE_RESOURCE_CASE_INSENSITIVE);

        // Login with case sensitive
        authorizationService = serviceLocator.getAuthorizationService();

        assertFalse("The login is case sensitive.",
                authorizationService.isLoginCaseSensitive());

        String lLoginToken =
                authorizationService.login(StringUtils.upperCase(USER_LOGIN),
                        USER_PASSWORD);

        assertNotNull(
                "The user can not be logon with a login in an other case.",
                lLoginToken);
    }
}
