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
 * TestGetProductWithHighRoleFacade
 * 
 * @author nveillet
 */
public class TestGetProductWithHighRoleFacade extends AbstractFacadeTestCase {

    /**
     * normal case
     */
    public void testNormalCase() {
        UiUserSession lUserSession = getAdminUserSession();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        String lExpectedProductName =
                lAuthorizationFacade.getAvailableProducts(lUserSession).get(0);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get product with high role");
        }
        String lProductName =
                lAuthorizationFacade.getProductWithHighRole(lUserSession);

        assertEquals(lExpectedProductName, lProductName);
    }
}
