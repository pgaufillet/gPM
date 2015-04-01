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

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidTokenException;

/**
 * Tests the method <CODE>selectRole<CODE> of the Authorization
 * Service.
 * 
 * @author srene
 */
public class TestSelectRole extends AbstractBusinessServiceTestCase {

    /** The products name of process Bugzilla */
    private static final String[] NEW_PROCESS_PRODUCTS_NAMES =
            { GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
             GpmTestValues.PRODUCT1_NAME };

    /** The role name */
    private static final String ROLE_NAME = "notadmin";

    /**
     * testSelectProductRoleWithoutAdminRole tests the method on a new business
     * process in which admin has a role on a product and no admin role selects
     * here a role for a product
     */
    public void testSelectProductRoleWithoutAdminRole() {
        // Retrieves the products
        try {
            authorizationService.selectRole(normalUserToken, ROLE_NAME,
                    NEW_PROCESS_PRODUCTS_NAMES[0], getProcessName());
        }
        catch (InvalidTokenException lITEx) {
            fail("Select role " + ROLE_NAME + " failed.");
        }
    }

    /**
     * testWithoutAdminRole tests the method on a new business process in which
     * admin has a role on a product and no admin role selects here an admin
     * role : uses a blank product name
     */
    public void testWithoutAdminRole() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieves the products
        try {
            authorizationService.selectRole(normalUserToken, ROLE_NAME, "",
                    getProcessName());
        }
        catch (AuthorizationException lITEx) {
            //ok
            // Select a new role refused because haven't an admin on this business process
        }
        catch (Exception lEx) {
            fail("Should return a AuthorizationException");
        }
    }
}