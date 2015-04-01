/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Sébastien René
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import static org.topcased.gpm.business.GpmTestValues.USERS_COUNT;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.EndUserData;

/**
 * Tests the method <CODE>getAllUserData<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestGetAllUserDataService extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private AuthorizationService authorizationService;

    /**
     * Tests the getAllUserData method.
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Get all user data defined in the gPM server
        EndUserData[] lUserData = authorizationService.getAllUserData();

        // Check that the count is OK
        assertEquals("Wrong users count in getAllUserData().", USERS_COUNT,
                lUserData.length);
    }
}