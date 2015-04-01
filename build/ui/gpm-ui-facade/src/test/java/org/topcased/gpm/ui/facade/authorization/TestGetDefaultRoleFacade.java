/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.authorization;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;

/**
 * TestGetDefaultRoleFacade
 * 
 * @author nveillet
 */
public class TestGetDefaultRoleFacade extends AbstractFacadeTestCase {

    private static final String ROLE_NAME = getAdminRoleName();

    /**
     * normal case
     */
    public void testNormalCase() {
        UiUserSession lUserSession = getAdminUserSession();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get product with high role");
        }
        String lProductName =
                lAuthorizationFacade.getProductWithHighRole(lUserSession);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get default role for product '" + lProductName + "'.");
        }
        String lRoleName =
                lAuthorizationFacade.getDefaultRole(lUserSession, lProductName);

        assertEquals(ROLE_NAME, lRoleName);
    }
}
