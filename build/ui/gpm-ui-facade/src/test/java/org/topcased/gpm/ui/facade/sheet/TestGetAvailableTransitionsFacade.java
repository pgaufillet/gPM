/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.sheet;

import java.util.List;

import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

/**
 * TestGetAvailableTransitionsFacade
 * 
 * @author jlouisy
 */
public class TestGetAvailableTransitionsFacade extends AbstractFacadeTestCase {

    private static final String TRANSITION_NAME = "TO CLOSE";

    /**
     * Confidential sheet case
     */
    public void testConfidentialSheet() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get available transitions.");
        }
        UiSheet lUiSheet =
                lSheetFacade.getSheet(lUiSession, lSheetId, DisplayMode.EDITION);
        List<String> lTransitionsList =
                lSheetFacade.getAvailableTransitions(lUiSession, lUiSheet);

        assertEquals(0, lTransitionsList.size());

        logoutAsUser(lUiSession.getParent());
    }

    /**
     * Normal case
     */
    public void testNormalCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get available transitions.");
        }
        UiSheet lUiSheet =
                lSheetFacade.getSheet(lUiSession, lSheetId, DisplayMode.EDITION);
        List<String> lTransitionsList =
                lSheetFacade.getAvailableTransitions(lUiSession, lUiSheet);

        assertEquals(1, lTransitionsList.size());
        assertTrue(lTransitionsList.contains(TRANSITION_NAME));
    }

}
