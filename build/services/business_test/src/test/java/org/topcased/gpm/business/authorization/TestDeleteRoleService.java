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
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.exception.GDMException;

/**
 * Tests the method <CODE>deleteRole<CODE> of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestDeleteRoleService extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private AuthorizationService authorizationService;

    /** The role name login. */
    private final static String UNUSED_ROLE_NAME = "unused";

    /** The role name login. */
    private final static String BAD_ROLE_NAME = "bad";

    /**
     * Tests the deleteRole method.
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Delete the role
        authorizationService.deleteRole(adminRoleToken, UNUSED_ROLE_NAME,
                getProcessName());

        // Retrieving the roles
        Collection<String> lRoleNames =
                authorizationService.getRolesNames(getProcessName());

        assertNotNull("Method getRolesNames returns null.", lRoleNames);
        assertFalse("Role named " + UNUSED_ROLE_NAME
                + " has not been deleted in DB.",
                lRoleNames.contains(UNUSED_ROLE_NAME));
    }

    /**
     * Tests the deleteRole method with a bad role name.
     */
    public void testBadRoleNameCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Delete the role
        try {
            authorizationService.deleteRole(adminRoleToken, BAD_ROLE_NAME,
                    getProcessName());
            fail("The exception has not been thrown.");
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a GDMException.");
        }
    }
}