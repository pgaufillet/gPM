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

/**
 * Tests the method <CODE>createRole<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestCreateRoleService extends AbstractBusinessServiceTestCase {

    /** The role name login. */
    private final static String ROLE_NAME = "tester";

    /**
     * Tests the createRole method.
     */
    public void testNormalCase() {
        // Create the role
        authorizationService.createRole(adminRoleToken, ROLE_NAME,
                getProcessName());

        // Retrieving the role
        Collection<String> lRoleNames =
                authorizationService.getRolesNames(getProcessName());

        assertNotNull("Method getRolesNames returns null.", lRoleNames);
        assertFalse("Method getRolesNames returns a empty list of role Names.",
                lRoleNames.isEmpty());
        assertTrue("Role named " + ROLE_NAME + " has not been created in DB.",
                lRoleNames.contains(ROLE_NAME));
    }
}