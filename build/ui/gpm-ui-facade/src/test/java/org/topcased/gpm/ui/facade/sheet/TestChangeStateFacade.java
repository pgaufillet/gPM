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

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

/**
 * SheetFacade.getSheet test
 * 
 * @author jlouisy
 */
public class TestChangeStateFacade extends AbstractFacadeTestCase {

    private static final String TRANSITION_TO_PERFORM = "TO CLOSE";

    private static final String NEW_STATE = "CLOSED";

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
            LOGGER.info("Perform transition.");
        }
        try {
            lSheetFacade.changeState(lUiSession, lSheetId,
                    TRANSITION_TO_PERFORM);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
        }
        catch (Exception e) {
            fail("Bad Exception : " + e.getMessage());
        }

        logoutAsUser(lUiSession.getParent());
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        // Get origin sheet
        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Origin Sheet.");
        }
        UiSheet lUiSheet =
                lSheetFacade.getSheet(lUiSession, lSheetId, DisplayMode.EDITION);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Perform transition.");
        }
        lSheetFacade.changeState(lUiSession, lSheetId, TRANSITION_TO_PERFORM);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get sheet.");
        }
        lUiSheet =
                lSheetFacade.getSheet(lUiSession, lSheetId, DisplayMode.EDITION);

        // Check state
        assertEquals(NEW_STATE, lUiSheet.getState());
    }

}
