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
 * TestGetAvailableProcessesFacade
 * 
 * @author jlouisy
 */
public class TestGetAvailableProcessesFacade extends AbstractFacadeTestCase {

    private static final String[] AVAILABLE_PROCESS =
            new String[] { "TestInstance", "TestInstanceBis" };

    /**
     * normal case
     */
    public void testNormalCase() {
        UiUserSession lUiUserSession = getAdminUserSession();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get available process.");
        }
        List<String> lProcess =
                lAuthorizationFacade.getAvailableProcesses(lUiUserSession);
        assertEquals(Arrays.asList(AVAILABLE_PROCESS), lProcess);
    }

}
