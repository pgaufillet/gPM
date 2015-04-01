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
public class TestGetAvailableRolesFacade extends AbstractFacadeTestCase {

    private static final String[] ADMIN_ROOT_PRODUCT_ROLES =
            new String[] { "admin", "user" };

    private static final String[] USER_ROOT_PRODUCT_ROLES =
            new String[] { "user" };

    /**
     * normal case
     */
    public void testNormalCase() {
        UiUserSession lUiUserSession = getAdminUserSession();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get available roles.");
        }
        List<String> lRoles =
                lAuthorizationFacade.getAvailableRoles(lUiUserSession,
                        "ROOT_PRODUCT");
        assertEquals(Arrays.asList(ADMIN_ROOT_PRODUCT_ROLES), lRoles);
    }

    /**
     * restricted Rights case
     */
    public void testRestrictedRightsCase() {
        //login as USER
        UiUserSession lUiUserSession = loginAsUser();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get available roles.");
        }
        List<String> lRoles =
                lAuthorizationFacade.getAvailableRoles(lUiUserSession,
                        "ROOT_PRODUCT");
        assertEquals(Arrays.asList(USER_ROOT_PRODUCT_ROLES), lRoles);

        //logout USER
        logoutAsUser(lUiUserSession);
    }

}
