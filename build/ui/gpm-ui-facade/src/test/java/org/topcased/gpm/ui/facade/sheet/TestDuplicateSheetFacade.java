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
public class TestDuplicateSheetFacade extends AbstractFacadeTestCase {

    /**
     * Confidential sheet case
     */
    public void testConfidentialSheet() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Confidential_sheet");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Duplicate Confidential Sheet.");
        }
        try {
            lSheetFacade.duplicateSheet(lUiSession, lSheetId);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the sheet " + lSheetId
                            + " : the access is confidential";
            assertEquals("Bad exception.", lMessage, lException.getMessage());
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
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Origin Sheet.");
        }
        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        "REF_Origin_Sheet");

        UiSheet lUiSheet =
                lSheetFacade.getSheet(lUiSession, lSheetId, DisplayMode.EDITION);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Duplicate Origin Sheet.");
        }
        UiSheet lUiDuplicatedSheet =
                lSheetFacade.duplicateSheet(lUiSession, lSheetId);

        // Sheet attributes
        assertEquals(lUiSheet.getBusinessProcessName(),
                lUiDuplicatedSheet.getBusinessProcessName());
        assertEquals(lUiSheet.getTypeName(), lUiDuplicatedSheet.getTypeName());
        assertEquals(lUiSheet.isDeletable(), lUiDuplicatedSheet.isDeletable());
        assertEquals(lUiSheet.isUpdatable(), lUiDuplicatedSheet.isUpdatable());
        assertEquals(lUiSheet.getProductName(),
                lUiDuplicatedSheet.getProductName());

        // Check some fields
        assertEquals(lUiSheet.getStringField("STRING_SIMPLE_FIELD").get(),
                lUiDuplicatedSheet.getStringField("STRING_SIMPLE_FIELD").get());
        assertEquals(
                lUiSheet.getStringField("STRING_SIMPLE_FIELD").getFieldDescription(),
                lUiDuplicatedSheet.getStringField("STRING_SIMPLE_FIELD").getFieldDescription());
    }
}
