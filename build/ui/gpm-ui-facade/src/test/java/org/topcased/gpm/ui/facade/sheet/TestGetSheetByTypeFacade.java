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
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

/**
 * SheetFacade.getSheetByType test
 * 
 * @author jlouisy
 */
public class TestGetSheetByTypeFacade extends AbstractFacadeTestCase {

    private static final String SHEET_STATE = "INIT";

    private static final String SHEET_TYPE_NAME = "SHEET_1";

    private static final String CONFIDENTIAL_SHEET_TYPE_NAME =
            "CONFIDENTIAL_SHEET";

    /**
     * Confidential sheet case
     */
    public void testConfidentialSheet() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Empty Confidential Sheet.");
        }
        try {
            lSheetFacade.getSheetByType(lUiSession,
                    CONFIDENTIAL_SHEET_TYPE_NAME);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the sheet type 'CONFIDENTIAL_SHEET' : "
                            + "the access is confidential ";
            String lExceptionMessage = lException.getMessage();
            assertEquals("Bad exception.", lMessage, lExceptionMessage);
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

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get empty sheet.");
        }
        UiSheet lUiSheet =
                lSheetFacade.getSheetByType(lUiSession, SHEET_TYPE_NAME);

        assertEquals(DEFAULT_PROCESS_NAME, lUiSheet.getBusinessProcessName());
        assertEquals(SHEET_TYPE_NAME, lUiSheet.getTypeName());
        assertTrue(lUiSheet.isDeletable());
        assertTrue(lUiSheet.isUpdatable());
        assertEquals(DEFAULT_PRODUCT_NAME, lUiSheet.getProductName());
        assertEquals(SHEET_STATE, lUiSheet.getState());
    }

}
