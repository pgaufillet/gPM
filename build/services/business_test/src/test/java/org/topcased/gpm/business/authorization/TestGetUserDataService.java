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

/**
 * Tests the method <CODE>getUserData<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestGetUserDataService extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private AuthorizationService authorizationService;

    /** The user login. */
    private final static String USER_LOGIN = GpmTestValues.USER_ADMIN;

    /**
     * Tests the getUserData method.
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the user
        EndUserData lUser = authorizationService.getUserData(USER_LOGIN);

        assertNotNull("User with login " + USER_LOGIN + " is not in DB.", lUser);
    }
}