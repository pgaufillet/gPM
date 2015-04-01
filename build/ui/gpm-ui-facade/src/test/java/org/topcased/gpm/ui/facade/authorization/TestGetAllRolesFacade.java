/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.authorization;

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;

/**
 * TestGetAvailableRolesFacade
 * 
 * @author jlouisy
 */
public class TestGetAllRolesFacade extends AbstractFacadeTestCase {

    private static final String[] ADMIN_ROOT_PRODUCT_ROLES =
            new String[] { "admin", "user", "role 2 (inst order 2)",
                          "role 4 (inst order 3)", "role 1 (inst order 4)" };

    /**
     * normal case
     */
    public void testNormalCase() {
        UiUserSession lUiUserSession = getAdminUserSession();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get all roles.");
        }
        List<String> lRoles = lAuthorizationFacade.getAllRoles(lUiUserSession);
        assertEquals(Arrays.asList(ADMIN_ROOT_PRODUCT_ROLES), lRoles);
    }

}
