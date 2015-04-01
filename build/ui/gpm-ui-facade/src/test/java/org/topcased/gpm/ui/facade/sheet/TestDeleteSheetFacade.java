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

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;

/**
 * SheetFacade.getSheet test
 * 
 * @author jlouisy
 */
public class TestDeleteSheetFacade extends AbstractFacadeTestCase {

    private static final String REFERENCE = "REF_Origin_Sheet";

    private static final String SHEET_TYPE_NAME = "SHEET_1";

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
            LOGGER.info("Delete Confidential Sheet.");
        }
        try {
            lSheetFacade.deleteSheet(lUiSession, lSheetId);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the sheet "
                            + lSheetId
                            + " : the access is confidential or deletion is not allowed";
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
        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        REFERENCE);

        // Deletion
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete Origin Sheet.");
        }
        lSheetFacade.deleteSheet(lUiSession, lSheetId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.debug("Try to get deleted sheet.");
        }
        List<SheetSummaryData> lSheetsList =
                lSheetService.getSheetsByType(
                        lUiSession.getParent().getProcessName(),
                        SHEET_TYPE_NAME);

        assertEquals("Sheet is not deleted", 1, lSheetsList.size());
        assertFalse("Wrong sheet is deleted",
                lSheetId.equals(lSheetsList.get(0).getId()));
    }

    /**
     * Normal case
     */
    public void testSheetNotDeletableCase() {
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();

        // Get origin sheet
        SheetService lSheetService = getSheetService();
        String lSheetId =
                lSheetService.getSheetIdByReference(lUiSession.getRoleToken(),
                        REFERENCE);

        // Deletion
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete Origin Sheet.");
        }
        try {
            lSheetFacade.deleteSheet(lUiSession, lSheetId);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the sheet "
                            + lSheetId
                            + " : the access is confidential or deletion is not allowed";
            assertEquals("Bad exception.", lMessage, lException.getMessage());
        }
        logoutAsUser(lUiSession.getParent());
    }

}
