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

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.exception.InvalidTokenException;

/**
 * Tests the user session methods
 * 
 * @author srene
 */
public class TestUserSessionService extends AbstractBusinessServiceTestCase {

    /** A not valid token */
    private static final String INVALIDTOKEN = new UID().toString();

    /** The Authorization Service. */
    private AuthorizationService authorizationService;

    /**
     * testGetUserSessionFromRoleSessionWithNullRoleToken
     */
    public void testGetUserSessionFromRoleSessionWithNullRoleToken() {
        authorizationService = serviceLocator.getAuthorizationService();

        try {
            authorizationService.getUserSessionFromRoleSession(null);
        }
        catch (InvalidTokenException lITEx) {
            //ok
        }
        catch (Exception lEx) {
            fail("InvalidTokenException hasn't been thrown");
        }
    }

    /**
     * testGetUserSessionFromRoleSessionWithNotValidRoleToken
     */
    public void testGetUserSessionFromRoleSessionWithNotValidRoleToken() {
        authorizationService = serviceLocator.getAuthorizationService();

        try {
            authorizationService.getUserSessionFromRoleSession(INVALIDTOKEN);
        }
        catch (InvalidTokenException lITEx) {
            //ok
        }
        catch (Exception lEx) {
            fail("InvalidTokenException hasn't been thrown");
        }
    }

    /**
     * testGetLoggedUserData
     */
    public void testGetLoggedUserDataWithNullToken() {
        authorizationService = serviceLocator.getAuthorizationService();

        try {
            authorizationService.getLoggedUserData(null);
        }
        catch (InvalidTokenException lITEx) {
            //ok
        }
        catch (Exception lEx) {
            fail("InvalidTokenException hasn't been thrown");
        }
    }

    /**
     * testHasAdminAccess
     */
    public void testHasAdminAccess() {
        authorizationService = serviceLocator.getAuthorizationService();

        boolean lResult = authorizationService.hasAdminAccess(adminRoleToken);

        assertTrue("The current user with role " + getAdminRoleName()
                + " isn't admin.", lResult);
    }

    /**
     * testHasAdminAccessWithInvalidToken
     */
    public void testHasAdminAccessWithInvalidToken() {
        authorizationService = serviceLocator.getAuthorizationService();

        try {
            authorizationService.hasAdminAccess(INVALIDTOKEN);
        }
        catch (InvalidTokenException lITEx) {
            //ok
        }
        catch (Exception lEx) {
            fail("InvalidTokenException hasn't been thrown");
        }
    }
}
