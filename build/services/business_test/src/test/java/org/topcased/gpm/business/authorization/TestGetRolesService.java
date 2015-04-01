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

import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.RoleData;

/**
 * Tests the method <CODE>getRoles<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestGetRolesService extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private AuthorizationService authorizationService;

    /** The user login. */
    private static final String USER_LOGIN = GpmTestValues.USER_ADMIN;

    /** The role name. */
    private static final String ROLE_NAME = GpmTestValues.USER_ADMIN;

    /**
     * Tests the getRoles method.
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the role names
        Collection<RoleData> lRoles =
                authorizationService.getRoles(USER_LOGIN, getProcessName());

        assertNotNull("Method getRolesNames returns null.", lRoles);
        assertFalse("Method getRolesNames returns a empty list of role Names.",
                lRoles.isEmpty());
        assertEquals("", ROLE_NAME, lRoles.iterator().next().getRoleName());
    }
}