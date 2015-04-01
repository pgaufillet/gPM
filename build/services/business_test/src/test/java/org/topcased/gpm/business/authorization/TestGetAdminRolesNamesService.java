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

import java.util.Arrays;
import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AuthorizationService;

/**
 * Tests the method <CODE>getAdminRolesNames<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestGetAdminRolesNamesService extends
        AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private AuthorizationService authorizationService;

    /** The roles names. */
    private static final String[] ROLE_NAMES = { GpmTestValues.USER_ADMIN };

    /**
     * Tests the getAdminRolesNames method.
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the role names
        Collection<String> lRoleNames =
                authorizationService.getAdminRolesNames(adminUserToken);

        assertNotNull("Method getAdminRolesNames returns null.", lRoleNames);
        int lSize = lRoleNames.size();
        int lExpectedSize = ROLE_NAMES.length;
        assertEquals("Method getAdminRolesNames returns " + lSize
                + " role names instead of " + lExpectedSize, lExpectedSize,
                lSize);

        assertTrue("Roles Names returned are not thoses expected. ",
                lRoleNames.containsAll(Arrays.asList(ROLE_NAMES)));
    }
}